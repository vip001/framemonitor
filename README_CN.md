# FrameMonitor

## 目录
* [简述](#简述)
* [接入说明](#接入说明)
* [版本历史](#版本历史)
* [问题](#问题)

## 简述
<p style="text-indent:2em">
FrameMonitor 相当于SDK版的Systrace,可以监控App的帧绘制时间，接入后可以通过小圆球看到App的实时帧绘制时间，当发生严重掉帧，小圆球颜色变红，并且会保存日志到本地，以供分析
</p>
<p style="text-indent:2em">
小圆球上的数字代表当前帧耗费的时间，单位是ms
</p>
<p style="text-indent:2em">
小圆球的颜色：绿色-流畅；黄色-可以接收的掉帧；红色-严重掉帧，默认:绿色 -(<=16ms)，黄色-(16-32ms)，红色(>32ms)，与Systrace的绿黄红定义保持一致；实际测试中由于机器性能差异同一份代码在不同机器上跑出来的帧绘制时间可能最高相差16ms，所以提供IConfig接口来定义掉帧的严重程度，使帧绘制时间可以反映主观上的卡顿
</p>
<p style="text-indent:2em">
日志的保存目录：优先SD卡目录（/mnt/sdcard/Android/data/【packagename】/cache/framemonitor/log),
再是手机内存(/data/data/【packagename】/cache/framemonitor/log)
</p>
<p style="text-indent:2em">
日志文件：文件名格式为"yyyy-MM-dd-HH-mm-ss"，内容格式如下：<br>
</p>
<pre><code>
    --------MainThread Message--------
	
	    <发生卡顿时主线程Looper里的所有消息>
		
    --------MainThread Stack Before JANK--------
	    
		<发生卡顿时主线程的调用堆栈>
		
	--------Thread Stack--------
	
	    <发生卡顿瞬间线程的调用堆栈>
	
</code></pre>

## 接入说明
<p style="text-indent:2em">在工程中先引入jcenter仓库</p>
<pre><code>
buildscript {
    repositories {
        jcenter()
    }
}
allprojects {
    repositories {
        jcenter()
    }
}
</code></pre>
<p style="text-indent:2em">在build.gradle文件中</p>
<pre><code>
dependencies {
     debugImplementation 'com.github.vip001:framemonitor-android:1.0.0'
     releaseImplementation 'com.github.vip001:framemonitor-android-no-op:1.0.0'
}
</code></pre>
<p style="text-indent:2em">在Application类中：</p>
<pre><code>
public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FrameMonitorManager.getInstance().init(this).start();
    }
}
</code></pre>
<p style="text-indent:2em">如果需要小圆球，在BaseActivity类中：</p>
<pre><code>
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameMonitorManager.getInstance().show(this);
    }
}
</code></pre>

## 版本历史
### 1.0
 1. 帧绘制时间检测
 2. 小圆球悬浮窗显示（无需悬浮窗权限)
 3. 日志信息：发生卡顿时MainLooper的Message信息，线程调用的堆栈信息
 4. 帧绘制时间检测开关控制和小圆球显示开关控制


##  问题
<p style="text-indent:2em"> 
<b>Android每帧的绘制时间为什么是16ms?</b>
</p>
<hr/>
<p style="text-indent:2em">
 Android屏幕设备的刷新频率为60Hz,通过做一道小学计算题 1÷60≈16.67 (ms)得到16ms
</p>
<p style="text-indent:2em"> 
<b>framemonitor与blockcanary对比</b>
</p>
<hr/>
<p style="text-indent:2em">
https://www.jianshu.com/p/1b0e4d79f511
</p>
<b>framemonitor原理分析</b>
</p>
<hr/>
<p style="text-indent:2em">
https://www.jianshu.com/p/9f200016d309
</p>

# FrameMonitor

## 目录
* [简述](#简述)
* [接入说明](#接入说明)
* [版本历史](#版本历史)
* [问题](#问题)

## 简述
<p style="text-indent:2em">
FrameMonitor 相当于SDK版的Systrace,可以监控App的帧率，接入后可以通过小圆球看到App的实时帧率，当发生严重掉帧，小圆球颜色变红，并且会保存日志到本地，以供分析
</p>
<p style="text-indent:2em">
小圆球上的数字代表当前帧耗费的时间，单位是ms
</p>
<p style="text-indent:2em">
小圆球的颜色：绿色-流畅；黄色-可以接收的掉帧；红色-严重掉帧，默认:绿色 -(<=16ms)，黄色-(16-32ms)，红色(>32ms)，与Systrace的绿黄红定义保持一致；如需自定义标准可使用IConfig接口
</p>
<p style="text-indent:2em">
日志的保存目录：优先SD卡目录（/mnt/sdcard/Android/data/【packagename】/cache/framemonitor/<file>),
再是手机内存(/data/data/cache/framemonitor)
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
<p style="text-indent:2em">在build.gradle文件中</p>
<pre><code>
dependencies {
     debugImplementation project(':framemonitor-android')
     releaseImplementation project(':framemonitor-android-no-op')
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
 1. 帧率检测
 2. 小圆球悬浮窗显示（无需悬浮窗权限)
 3. 日志信息：发生卡顿时MainLooper的Message信息，线程调用的堆栈信息
 4. 帧率检测开关控制和小圆球显示开关控制


##  问题
<p style="text-indent:2em"> 
<b>Android每帧的绘制时间为什么是16ms?</b>
</p>
<hr/>
<p style="text-indent:2em">
 Android屏幕设备的刷新频率为60Hz,通过做一道小学计算题 1÷60≈16.67 (ms)得到16ms
</p>
<p style="text-indent:2em"> 
<b>后续优化</b>
</p>
<hr/>
<p style="text-indent:2em">
log信息增加发生卡顿时的cpu信息，内存信息,GC耗时分析；demo做得更漂亮
</p>

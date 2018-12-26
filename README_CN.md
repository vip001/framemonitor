[英文文档](https://github.com/vip001/framemonitor/blob/master/README.md)

# 简述

FrameMonitor 是一个检测ui卡顿的sdk，也可以检测App消耗的流量，ui部分参考了leakcanary，readme文档参考了blockcanary

# 快速入门

<strong>在build.gradle(Project)文件中</strong>
```gradle
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
```
<strong>在build.gradle(Module)文件中</strong>
```gradle
dependencies {
     debugImplementation 'com.github.vip001:framemonitor-android:2.0.2'
     releaseImplementation 'com.github.vip001:framemonitor-android-no-op:2.0.2'
}
```
<strong>在 Application类中</strong>
<pre><code>
public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FrameMonitorManager.getInstance().init(this).start();
    }
}
</code></pre>
<strong>如果需要显示悬浮球</strong>
<pre><code>
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameMonitorManager.getInstance().show(this);
    }
}
</code></pre>

# 功能及原理

见[framemonitor原理分析](https://www.jianshu.com/p/9f200016d309)<br/>
或见下图:<br/><br/>
![flow](https://github.com/vip001/framemonitor/blob/master/instruction/framemonitor_principle.png)

# 演示

![screenshot1](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot1.png)
&nbsp;&nbsp;&nbsp;&nbsp;
![screenshot2](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot2.png)
<br/><br/>
![screenshot3](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot3.png)
&nbsp;&nbsp;&nbsp;&nbsp;
![screenshot4](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot4.png)
&nbsp;&nbsp;&nbsp;&nbsp;
![screenshot5](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot5.png)

# 版本历史

查看 [CHANGELOG](https://github.com/vip001/framemonitor/blob/master/CHANGELOG.md)

# 赞赏

如果你喜欢 FrameMonitor sdk，感觉 FrameMonitor 帮助到了你，可以点右上角 "Star" 支持一下 谢谢！ ^_^ 你也还可以扫描下面的二维码~ 请作者喝一杯咖啡。<br/>
![alipay](https://github.com/vip001/framemonitor/blob/master/instruction/alipay.png) 
![wechat](https://github.com/vip001/framemonitor/blob/master/instruction/weixin.png)

# 贡献

如果你希望贡献代码到FrameMonitor，你可以fork本repository然后发一个PR。

# 协议

    Copyright (C) 2018 vip001

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
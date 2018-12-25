# Brief
FrameMonitor is a transparent ui-block detection library for Android,can also detect the consumed traffic, ui part refers to the leakcanary, readme documentation refers to the blockcanary
# Getting started
<strong>In  build.gradle(Project)</strong>
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
<strong>In  build.gradle(Module)</strong>
```gradle
dependencies {
     debugImplementation 'com.github.vip001:framemonitor-android:1.0.0'
     releaseImplementation 'com.github.vip001:framemonitor-android-no-op:1.0.0'
}
```
<strong>In Application</strong>
<pre><code>
public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FrameMonitorManager.getInstance().init(this).start();
    }
}
</code></pre>
<strong>If you want to show float ball</strong>
<pre><code>
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameMonitorManager.getInstance().show(this);
    }
}
</code></pre>
# How does it work?
Blog in Chinese：[FrameMonitor](https://www.jianshu.com/p/9f200016d309)
Principle flow picture:
![flow](https://github.com/vip001/framemonitor/blob/master/instruction/framemonitor_principle.png)
# Screenshot
![screenshot1](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot1.png)
<strong>when double click the float ball</strong>
![screenshot2](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot2.png)
![screenshot3](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot3.png)
![screenshot4](https://github.com/vip001/framemonitor/blob/master/instruction/Screenshot4.png)
# Donation
If you find this repository helpful, you may make a donation to me via alipay or wechat.
![alipay](https://github.com/vip001/framemonitor/blob/master/instruction/alipay.png) 
![wechat](https://github.com/vip001/framemonitor/blob/master/instruction/weixin.png)
# Versions

Check [CHANGELOG](https://github.com/vip001/framemonitor/blob/master/CHANGELOG.md)

# Contribute

If you would like to contribute code to BlockCanary you can do so through GitHub by forking the repository and sending a pull request.

# License

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
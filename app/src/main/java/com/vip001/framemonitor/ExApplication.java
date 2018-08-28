package com.vip001.framemonitor;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.vip001.monitor.FrameMonitorManager;

/**
 * Created by xxd on 2018/8/9
 */
public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FrameMonitorManager.getInstance().init(this).start();
        BlockCanary.install(this,new BlockCanaryContext(){
            @Override
            public boolean stopWhenDebugging() {
                return false;
            }
        }).start();
    }
}

package com.vip001.framemonitor;

import android.app.Application;

import com.vip001.monitor.FrameMonitorManager;

/**
 * Created by xxd on 2018/8/9
 */
public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FrameMonitorManager.getInstance().init(this).start();
    }
}

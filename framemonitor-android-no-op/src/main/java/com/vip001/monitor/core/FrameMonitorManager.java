package com.vip001.monitor.core;

import android.app.Activity;
import android.app.Application;

import com.vip1002.framemonitor.IConfig;
import com.vip1002.framemonitor.IFrameMonitorManager;

/**
 * Created by xxd on 2018/8/7
 */
public class FrameMonitorManager implements IFrameMonitorManager {
    private static FrameMonitorManager sInstance = new FrameMonitorManager();
    private IConfig mConfig = new IConfig() {
        @Override
        public int sortTime(long time) {
            return 0;
        }
    };

    private FrameMonitorManager() {

    }


    public static FrameMonitorManager getInstance() {
        return sInstance;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public IFrameMonitorManager show(Activity activity) {
        return this;
    }

    @Override
    public IFrameMonitorManager init(Application application) {
        return this;
    }

    @Override
    public IFrameMonitorManager setConfig(IConfig config) {
        return this;
    }

    @Override
    public IConfig getConfig() {
        return mConfig;
    }

    @Override
    public IFrameMonitorManager setEnableShow(boolean enableShow) {
        return this;
    }

    @Override
    public IFrameMonitorManager start() {
        return this;
    }

    @Override
    public IFrameMonitorManager stop() {
        return this;
    }

    @Override
    public IFrameMonitorManager setEnableBackgroundMonitor(boolean enableBackgroundMonitor) {
        return this;
    }
}

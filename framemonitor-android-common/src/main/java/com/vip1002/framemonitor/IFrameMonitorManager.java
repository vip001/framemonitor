package com.vip1002.framemonitor;

import android.app.Activity;
import android.app.Application;

/**
 * Created by xxd on 2018/8/21
 */
public interface IFrameMonitorManager {
    boolean isStarted();

    IFrameMonitorManager show(Activity activity);

    IFrameMonitorManager init(Application application);

    IFrameMonitorManager setConfig(IConfig config);

    IConfig getConfig();

    IFrameMonitorManager setEnableShow(boolean enableShow);

    IFrameMonitorManager start();

    IFrameMonitorManager stop();
}

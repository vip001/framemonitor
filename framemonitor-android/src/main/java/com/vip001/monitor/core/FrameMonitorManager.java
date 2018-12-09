package com.vip001.monitor.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.vip001.framemonitor.IConfig;
import com.vip001.framemonitor.IFrameMonitorManager;

import java.util.List;

/**
 * Created by xxd on 2018/8/7
 */
public class FrameMonitorManager implements IFrameMonitorManager {
    private IFrameMonitorManager mFrameMonitorManagerImpl;
    private static final FrameMonitorManager sInstance = new FrameMonitorManager();

    private FrameMonitorManager() {

    }

    private boolean isMainProcess(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (packageName.equals(info.processName) && info.pid == Process.myPid()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isStarted() {
        return mFrameMonitorManagerImpl.isStarted();
    }

    @Override
    public IFrameMonitorManager show(Activity activity) {
        return mFrameMonitorManagerImpl.show(activity);
    }

    @Override
    public IFrameMonitorManager setConfig(IConfig config) {
        return mFrameMonitorManagerImpl.setConfig(config);
    }

    @Override
    public IConfig getConfig() {
        return mFrameMonitorManagerImpl.getConfig();
    }

    @Override
    public IFrameMonitorManager init(Application application) {
        if (isMainProcess(application)) {
            mFrameMonitorManagerImpl = new ForeGroundProcessImpl();
        } else {
            mFrameMonitorManagerImpl = new BackgroundProcessImpl();
        }
        mFrameMonitorManagerImpl.init(application);
        return this;
    }

    @Override
    public IFrameMonitorManager setEnableShow(boolean enableShow) {
        return mFrameMonitorManagerImpl.setEnableShow(enableShow);
    }

    @Override
    public IFrameMonitorManager start() {
        return mFrameMonitorManagerImpl.start();
    }

    @Override
    public IFrameMonitorManager stop() {
        return mFrameMonitorManagerImpl.stop();
    }

    @Override
    public IFrameMonitorManager setEnableBackgroundMonitor(boolean enableBackgroundMonitor) {
        return mFrameMonitorManagerImpl.setEnableBackgroundMonitor(enableBackgroundMonitor);
    }

    @Override
    public boolean startFlowCal() {
        return mFrameMonitorManagerImpl.startFlowCal();
    }

    @Override
    public boolean stopFlowCal() {
        return mFrameMonitorManagerImpl.stopFlowCal();
    }

    @Override
    public boolean hasStartFlowCal() {
        return mFrameMonitorManagerImpl.hasStartFlowCal();
    }


    public static FrameMonitorManager getInstance() {
        return sInstance;
    }


}

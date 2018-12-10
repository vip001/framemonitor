package com.vip001.monitor.core;

import android.app.Activity;
import android.app.Application;

import com.vip001.framemonitor.IConfig;
import com.vip001.framemonitor.IFrameMonitorManager;
import com.vip001.monitor.utils.ProcessUtils;

/**
 * Created by xxd on 2018/8/7
 */
public class FrameMonitorManager implements IFrameMonitorManager {
    private IFrameMonitorManager mFrameMonitorManagerImpl;
    private static final FrameMonitorManager sInstance = new FrameMonitorManager();

    private FrameMonitorManager() {

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
        int type = ProcessUtils.classifyProcess(application);
        mFrameMonitorManagerImpl = null;
        switch (type) {
            case ProcessUtils.Type.PROCESS_FOREGROUND:
                mFrameMonitorManagerImpl = new ForeGroundProcessImpl();
                break;
            case ProcessUtils.Type.PROCESS_BACKGROUND:
                mFrameMonitorManagerImpl = new BackgroundProcessImpl();
                break;
            case ProcessUtils.Type.PROCESS_OTHER:
                mFrameMonitorManagerImpl = new EmptyProcessImpl();
                break;
            default:
                mFrameMonitorManagerImpl = new EmptyProcessImpl();
                break;

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

package com.vip001.monitor.core;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;

import com.vip001.framemonitor.IConfig;
import com.vip001.framemonitor.IFrameMonitorManager;
import com.vip001.monitor.bean.DropFramesBean;
import com.vip001.monitor.common.FileManager;
import com.vip001.monitor.common.MsgDef;
import com.vip001.monitor.common.StateDef;
import com.vip001.monitor.services.IPCBinder;
import com.vip001.monitor.services.stack.LogThread;
import com.vip001.monitor.utils.DimentionUtils;
import com.vip001.monitor.view.DisableShowStatus;
import com.vip001.monitor.view.IShowStatus;
import com.vip001.monitor.view.ShowStatus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxd on 2018/9/20
 */
class ForeGroundProcessImpl implements FrameCore.FrameCoreCallback, Application.ActivityLifecycleCallbacks, IFrameMonitorManager, LogThread.Callback {
    private Application mApp;
    private List<String> mReuestShowActities;
    private WeakReference<Activity> mCurrentActivity;
    private long mVisibleCount;
    private boolean mLastForeground = false;
    private boolean hasStart;
    private LogThread mLogThread;

    private boolean enableBackgroundMonitor;
    private IShowStatus mShowStatus;
    private IPCBinder mBinder;
    private IConfig mConfig;
    private boolean hasStartFlow;

    ForeGroundProcessImpl() {
        mReuestShowActities = new ArrayList<>();
        mBinder = new IPCBinder();

    }

    @Override
    public boolean isStarted() {
        return hasStart;
    }

    @Override
    public IFrameMonitorManager setConfig(IConfig config) {
        if (config != null) {
            mConfig = config;
        }
        return this;
    }

    @Override
    public IConfig getConfig() {
        return mConfig;
    }

    @Override
    public IFrameMonitorManager init(Application application) {
        mApp = application;
        mApp.registerActivityLifecycleCallbacks(this);
        mApp.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                DisplayMetrics metrics = mApp.getResources().getDisplayMetrics();

                DimentionUtils.SCREEN_WIDTH = metrics.widthPixels;
                DimentionUtils.SCREEN_HEIGHT = metrics.heightPixels;
            }

            @Override
            public void onLowMemory() {

            }
        });
        FileManager.getInstance().init(application);
        DisplayMetrics metrics = application.getResources().getDisplayMetrics();
        DimentionUtils.SCREEN_WIDTH = metrics.widthPixels;
        DimentionUtils.SCREEN_HEIGHT = metrics.heightPixels;
        FileManager.getInstance().checkLogDir();
        mBinder.connect(application);
        FrameCoreConfigPersistence.getInstance().init(application);
        mConfig = new IConfig() {
            @Override
            public int sortTime(long time) {
                FrameCoreConfigPersistence.Config config = FrameCoreConfigPersistence.getInstance().getConfig();
                if (time > config.redTime) {
                    return RESULT_RED;
                } else if (time > config.yellowTime) {
                    return RESULT_YELLOW;
                } else {
                    return RESULT_GREEN;
                }
            }
        };
        if (FrameCoreConfigPersistence.getInstance().hasState(StateDef.ENABLE_SHOW_BALL)) {
            setEnableShow(true);
        }
        if (FrameCoreConfigPersistence.getInstance().hasState(StateDef.ENABLE_MONITOR_BACKGROUND)) {
            setEnableBackgroundMonitor(true);
        }
        if (FrameCoreConfigPersistence.getInstance().hasState(StateDef.ENABLE_MONITOR_WORK)) {
            start();
        }

        return this;
    }

    @Override
    public IFrameMonitorManager setEnableShow(boolean enableShow) {
        if (enableShow) {
            if (!(mShowStatus instanceof ShowStatus)) {
                if (mShowStatus != null) {
                    mShowStatus.onCleared();
                }
                mShowStatus = new ShowStatus();
                mShowStatus.init(mReuestShowActities, mCurrentActivity);
                FrameCoreConfigPersistence.getInstance().setState(StateDef.ENABLE_SHOW_BALL);
            }
        } else {
            if (!(mShowStatus instanceof DisableShowStatus)) {
                if (mShowStatus != null) {
                    mShowStatus.onCleared();
                }
                mShowStatus = new DisableShowStatus();
                mShowStatus.init(mReuestShowActities, mCurrentActivity);
                FrameCoreConfigPersistence.getInstance().clearState(StateDef.ENABLE_SHOW_BALL);
            }
        }
        return this;
    }

    @Override
    public IFrameMonitorManager start() {
        if (hasStart) {
            return this;
        }
        hasStart = true;
        if (mLogThread != null) {
            try {
                mLogThread.quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mLogThread = null;
        }
        File parentFile = FileManager.getInstance().checkLogDir();
        if (parentFile != null) {
            mLogThread = new LogThread(parentFile, this);
            mLogThread.start();
        }
        FrameCore.getInstance().start().registerCallback(this);
        if (mShowStatus != null && mShowStatus instanceof ShowStatus) {
            mShowStatus.init(mReuestShowActities, mCurrentActivity);
        }
        FrameCoreConfigPersistence.getInstance().setState(StateDef.ENABLE_MONITOR_WORK);
        return this;
    }

    @Override
    public IFrameMonitorManager stop() {
        if (!hasStart) {
            return this;
        }
        //core
        if (mLogThread != null) {
            try {
                mLogThread.quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mLogThread = null;
        }
        //ui
        FrameCore.getInstance().stop().unregisterCallback(this);
        setEnableShow(false);
        //flag
        hasStart = false;
        FrameCoreConfigPersistence.getInstance().clearState(StateDef.ENABLE_MONITOR_WORK);
        return this;
    }

    @Override
    public IFrameMonitorManager setEnableBackgroundMonitor(boolean enableBackgroundMonitor) {
        this.enableBackgroundMonitor = enableBackgroundMonitor;
        if (enableBackgroundMonitor) {
            FrameCoreConfigPersistence.getInstance().setState(StateDef.ENABLE_MONITOR_BACKGROUND);
        } else {
            FrameCoreConfigPersistence.getInstance().clearState(StateDef.ENABLE_MONITOR_BACKGROUND);
        }
        return this;
    }

    @Override
    public boolean startFlowCal() {
        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_START_FLOW;
        hasStartFlow = mBinder.sendMessage(msg);
        return hasStartFlow;
    }

    @Override
    public boolean stopFlowCal() {
        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_END_FLOW;
        boolean result = mBinder.sendMessage(msg);
        hasStartFlow = !result;
        return result;
    }

    @Override
    public boolean hasStartFlowCal() {
        return hasStartFlow;
    }

    @Override
    public IFrameMonitorManager show(Activity activity) {
        if (activity == null) {
            return this;
        }
        mReuestShowActities.add(activity.getClass().getName());
        if (canExecStatus()) {
            mShowStatus.show(activity);
        }
        return this;
    }

    private boolean canExecStatus() {
        return hasStart && mShowStatus != null;
    }

    @Override
    public void update(long ns) {
        if (mConfig.sortTime(ns) == IConfig.RESULT_RED && mLogThread != null && mLogThread.canWriteLog()) {
            saveDropFramesBean(ns);
            mLogThread.writeLog();
        }
        if (canExecStatus()) {
            mShowStatus.update(ns);
        }

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mVisibleCount++;
        mCurrentActivity = new WeakReference<Activity>(activity);
        if (!mLastForeground && mVisibleCount > 0) {
            mLastForeground = true;
            onAppChangeToForeGround();
        }
        if (canExecStatus()) {
            mShowStatus.onActivityStarted(activity);
        }
    }


    @Override
    public void onActivityResumed(Activity activity) {
        if (canExecStatus()) {
            mShowStatus.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (canExecStatus()) {
            mShowStatus.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mVisibleCount--;
        if (mLastForeground && mVisibleCount <= 0) {
            mLastForeground = false;
            onAppChangeToBackground();
        }

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mReuestShowActities.remove(activity.getClass().getName());
        if (canExecStatus()) {
            mShowStatus.onActivityDestroyed(activity);
        }

    }

    private void onAppChangeToBackground() {
        if (!enableBackgroundMonitor && hasStart && FrameCore.getInstance().isStarted()) {
            FrameCore.getInstance().stop();
        }
    }

    private void onAppChangeToForeGround() {
        if (hasStart && !FrameCore.getInstance().isStarted()) {
            FrameCore.getInstance().start();
        }
    }

    private void saveDropFramesBean(long framecostTime) {
        DropFramesBean.getInstance().frameCostTime = framecostTime;
        DropFramesBean.getInstance().happensTime = System.currentTimeMillis();
        if (mCurrentActivity.get() != null) {
            DropFramesBean.getInstance().topActivityName = mCurrentActivity.get().getClass().getName();
            DropFramesBean.getInstance().topActivitySimpleName = mCurrentActivity.get().getClass().getSimpleName();
        }
        DropFramesBean.getInstance().isForeground = mVisibleCount > 0;
    }

    @Override
    public void writeSuccess(String path) {
        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_WRITE_LOG_SUCCESS;
        Bundle bundle = new Bundle();
        bundle.putParcelable(DropFramesBean.KEY_TRANSACT, DropFramesBean.getInstance());
        msg.obj = bundle;
        mBinder.sendMessage(msg);
    }
}

package com.vip001.monitor;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.vip001.monitor.log.LogThread;
import com.vip001.monitor.utils.DimentionUtils;
import com.vip001.monitor.view.DisableShowStatus;
import com.vip001.monitor.view.IShowStatus;
import com.vip001.monitor.view.ShowStatus;
import com.vip1002.framemonitor.IConfig;
import com.vip1002.framemonitor.IFrameMonitorManager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxd on 2018/8/7
 */
public class FrameMonitorManager implements FrameMonitor.FrameMonitorCallback, Application.ActivityLifecycleCallbacks, IFrameMonitorManager {
    private Application mApp;
    private static FrameMonitorManager sInstance = new FrameMonitorManager();
    private static final String DIR_LOG_CACHE = "framemonitor";
    private File mParentFile;
    private List<String> mReuestShowActities;
    private WeakReference<Activity> mCurrentActivity;
    private long mVisibleCount;
    private boolean mLastForeground = false;
    private boolean hasStart;
    private static final String TAG = "FrameMonitor";

    private LogThread mLogThread;

    private boolean enableBackgroundMonitor;
    private IShowStatus mShowStatus;

    private FrameMonitorManager() {
        mReuestShowActities = new ArrayList<>();
        //default enableshow
        setEnableShow(true);
    }

    @Override
    public boolean isStarted() {
        return hasStart;
    }

    @Override
    public IFrameMonitorManager setConfig(IConfig config) {
        FrameMonitorConfig.getInstance().setConfig(config);
        return this;
    }

    @Override
    public IConfig getConfig() {
        return FrameMonitorConfig.getInstance().getConfig();
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
        DisplayMetrics metrics = application.getResources().getDisplayMetrics();
        DimentionUtils.SCREEN_WIDTH = metrics.widthPixels;
        DimentionUtils.SCREEN_HEIGHT = metrics.heightPixels;
        checkLogDir();
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
            }
        } else {
            if (!(mShowStatus instanceof DisableShowStatus)) {
                if (mShowStatus != null) {
                    mShowStatus.onCleared();
                }
                mShowStatus = new DisableShowStatus();
                mShowStatus.init(mReuestShowActities, mCurrentActivity);
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
        if (mParentFile != null) {
            mLogThread = new LogThread(mParentFile);
            mLogThread.start();
        }
        FrameMonitor.getInstance().start().registerCallback(this);
        if (mShowStatus != null && mShowStatus instanceof ShowStatus) {
            mShowStatus.init(mReuestShowActities, mCurrentActivity);
        }
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
        FrameMonitor.getInstance().stop().unregisterCallback(this);
        setEnableShow(false);
        //flag
        hasStart = false;
        return this;
    }

    @Override
    public IFrameMonitorManager setEnableBackgroundMonitor(boolean enableBackgroundMonitor) {
        this.enableBackgroundMonitor = enableBackgroundMonitor;
        return this;
    }

    private void checkLogDir() {
        checkLogDir(new File(mApp.getExternalCacheDir(), DIR_LOG_CACHE), false);
    }

    private void checkLogDir(File file, boolean stop) {
        if (stop) {
            return;
        }
        if (!file.exists()) {
            if (file.mkdirs()) {
                mParentFile = file;
                Log.i(TAG, "FrameMonitor log path=" + mParentFile.getAbsolutePath());
            } else {
                checkLogDir(new File(mApp.getCacheDir(), DIR_LOG_CACHE), true);
            }
        } else {
            mParentFile = file;
            Log.i(TAG, "FrameMonitor log path=" + mParentFile.getAbsolutePath());
        }
    }

    public static FrameMonitorManager getInstance() {
        return sInstance;
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
        if (FrameMonitorConfig.getInstance().sortTime(ns) == IConfig.RESULT_RED && mLogThread != null) {
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
        if (!enableBackgroundMonitor && hasStart && FrameMonitor.getInstance().isStarted()) {
            FrameMonitor.getInstance().stop();
        }
    }

    private void onAppChangeToForeGround() {
        if (hasStart && !FrameMonitor.getInstance().isStarted()) {
            FrameMonitor.getInstance().start();
        }
    }

}

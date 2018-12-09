package com.vip001.monitor.view;

import android.app.Activity;

import com.vip001.monitor.view.snackbar.SnackBarManagerType;
import com.vip001.monitor.view.snackbar.TSnackbar;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xxd on 2018/8/20.
 */

public class ShowStatus implements IShowStatus {
    private long mLastUpdateTime;
    private HashMap<String, TSnackbar> mSnackBars;
    private String mCurrentActivity;
    private int[] mPos;
    private List<String> mRequestShowActivities;

    @Override
    public void init(List<String> requestShowActivities, WeakReference<Activity> currentActivity) {
        mSnackBars = new HashMap<>();
        mRequestShowActivities = requestShowActivities;
        if (currentActivity != null && currentActivity.get() != null) {
            mCurrentActivity = currentActivity.get().getClass().getName();
            restoreShow(currentActivity.get());
        }
    }

    public void restoreShow(Activity activity) {
        String activityName = activity.getClass().getName();
        if (mRequestShowActivities != null && mSnackBars.get(activityName) == null && mRequestShowActivities.contains(activityName)) {
            int type = SnackBarManagerType.generateType();
            TSnackbar snackbar = TSnackbar.make(activity.getWindow().getDecorView(), new UserInterfaceHolder(activity), TSnackbar.LENGTH_INDEFINITE).show(type);
            mSnackBars.put(activity.getClass().getName(), snackbar);
        }
    }

    @Override
    public void show(Activity activity) {
        int type = SnackBarManagerType.generateType();
        TSnackbar snackbar = TSnackbar.make(activity.getWindow().getDecorView(), new UserInterfaceHolder(activity), TSnackbar.LENGTH_INDEFINITE).show(type);
        mCurrentActivity = activity.getClass().getName();
        mSnackBars.put(activity.getClass().getName(), snackbar);
        updateCurrentSnackBarPos();
    }

    @Override
    public void update(long ns) {
        TSnackbar snackbar = mSnackBars.get(mCurrentActivity);
        if (snackbar != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastUpdateTime > 100) {
                snackbar.setData(ns);
                mLastUpdateTime = currentTime;
            }

        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        updateCurrentSnackBarPos();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        TSnackbar snackbar = mSnackBars.get(mCurrentActivity);
        if (snackbar != null) {
            mPos = ((UserInterfaceHolder) snackbar.getHolder()).getBallPos();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mCurrentActivity = activity.getClass().getName();
        restoreShow(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        TSnackbar tSnackbar = mSnackBars.remove(activity.getClass().getName());
        if (tSnackbar != null) {
            tSnackbar.dismiss();
        }
    }

    @Override
    public void onCleared() {
        for (Map.Entry<String, TSnackbar> entry : mSnackBars.entrySet()) {
            entry.getValue().dismiss();
        }
        mSnackBars = null;
        mPos = null;
        mLastUpdateTime = 0;
    }


    private void updateCurrentSnackBarPos() {
        if (mPos != null) {
            TSnackbar snackbar = mSnackBars.get(mCurrentActivity);
            if (snackbar != null) {
                UserInterfaceHolder view = (UserInterfaceHolder) snackbar.getHolder();
                view.updateBall(mPos);
                mPos = null;
            }

        }
    }
}

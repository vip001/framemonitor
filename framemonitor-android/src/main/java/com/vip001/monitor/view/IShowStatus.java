package com.vip001.monitor.view;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by xxd on 2018/8/19.
 */

public interface IShowStatus {
    void init(List<String> requestShowActivities, WeakReference<Activity> currentActivity);

    void show(Activity activity);

    void update(long ns);

    void onActivityResumed(Activity activity);

    void onActivityPaused(Activity activity);

    void onActivityStarted(Activity activity);

    void onActivityDestroyed(Activity activity);

    void onCleared();

}

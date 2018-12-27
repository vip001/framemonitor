package com.vip001.monitor.utils;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by xxd on 2018/12/27
 */
public class ServiceStartUtils {
    public static void setForeground(final Service keepLiveService, final Service innerService) {
        final int foregroundPushId = 1002;

        if (keepLiveService != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                keepLiveService.startForeground(foregroundPushId, new Notification());
            } else {
                keepLiveService.startForeground(foregroundPushId, new Notification());
                if (innerService != null) {
                    innerService.startForeground(foregroundPushId, new Notification());
                    innerService.stopSelf();
                }
            }
        }
    }

    public static boolean startService(Context context, Intent intent) {
        if (context == null || intent == null || intent.getComponent() == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        return true;
    }
}

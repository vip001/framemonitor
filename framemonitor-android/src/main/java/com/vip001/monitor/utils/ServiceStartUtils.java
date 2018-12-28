package com.vip001.monitor.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by xxd on 2018/12/27
 */
public class ServiceStartUtils {
    public static void setForeground(final Service keepLiveService, final Service innerService) {
        final int foregroundPushId = 1002;

        if (keepLiveService != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                keepLiveService.startForeground(foregroundPushId, generateNotification(keepLiveService.getApplication()));
            } else {
                keepLiveService.startForeground(foregroundPushId, generateNotification(keepLiveService.getApplication()));
                if (innerService != null) {
                    innerService.startForeground(foregroundPushId, generateNotification(keepLiveService.getApplication()));
                    innerService.stopSelf();
                }
            }
        }


    }

    private static Notification generateNotification(Context context) {
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(context.getPackageName(), context.getPackageName(), NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            notification = new NotificationCompat.Builder(context, context.getPackageName())
                    .setChannelId(context.getPackageName())
                    .build();
        } else {
            notification = new Notification();
        }
        return notification;
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

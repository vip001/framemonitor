package com.vip001.monitor.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.vip001.monitor.R;
import com.vip001.monitor.activity.DisplayDropFramesActivity;
import com.vip001.monitor.bean.DropFramesBean;
import com.vip001.monitor.common.MsgDef;
import com.vip001.monitor.utils.BussinessUtils;

public class WriteLogMessageHandler implements IMessageHandler {
    private NotificationManagerCompat mNotificationManager;

    public WriteLogMessageHandler() {

    }

    @Override
    public boolean handleMessage(Message msg, RemoteBackgroundService.Env env) {
        boolean result = false;
        switch (msg.what) {
            case MsgDef.MSG_WRITE_LOG_SUCCESS:
                result = true;
                Bundle bundle = (Bundle) msg.obj;
                bundle.setClassLoader(Thread.currentThread().getContextClassLoader());
                DropFramesBean bean = bundle.getParcelable(DropFramesBean.KEY_TRANSACT);
                nofityBlock(env, bean);
                break;
            default:
                break;
        }
        return result;
    }

    private void nofityBlock(RemoteBackgroundService.Env env, DropFramesBean bean) {
        if (mNotificationManager == null) {
            mNotificationManager = NotificationManagerCompat.from(env.applicationContext);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(env.applicationContext.getPackageName(), env.applicationContext.getPackageName(), NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) env.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        String notifycontent = BussinessUtils.getDropFramesTarget(bean, true);
        Intent intent = new Intent(env.applicationContext, DisplayDropFramesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(env.applicationContext, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(env.applicationContext, env.applicationContext.getPackageName())
                .setContentTitle(notifycontent + " dropframes")
                .setContentText("Click for more details")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.monitor_ic_launcher)
                .setChannelId(env.applicationContext.getPackageName())
                .build();
        mNotificationManager.notify((int) System.currentTimeMillis(), notification);

    }
}

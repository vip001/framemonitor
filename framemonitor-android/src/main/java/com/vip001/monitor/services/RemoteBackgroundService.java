package com.vip001.monitor.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vip001.monitor.common.MsgDef;
import com.vip001.monitor.core.FrameMonitorManager;
import com.vip001.monitor.services.flow.FlowMessageHandler;
import com.vip001.monitor.utils.ServiceStartUtils;

import java.util.ArrayList;

/**
 * Created by xxd on 2018/9/19
 */
public class RemoteBackgroundService extends Service {
    private static final String TAG = "RemoteBackgroundService";
    private volatile static Service mInstance;
    private ArrayList<IMessageHandler> mMessageHandlers;
    private Env mEnv;
    private Handler mForegroundMsgHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            baseMsgHandler(msg);
            notifyMessageHandler(msg);

        }
    };

    private void baseMsgHandler(Message msg) {
        switch (msg.what) {
            case MsgDef.MSG_INIT:
                Log.i(TAG, "RemoteBackgroundService init success");
                if (msg.replyTo != null) {
                    mEnv.sender = msg.replyTo;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Messenger(mForegroundMsgHandler).getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FrameMonitorManager.getInstance().init(this.getApplication());
        ServiceStartUtils.startService(this, new Intent(RemoteBackgroundService.this, InnerService.class));
        initEnv();
        initMessageHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInstance = null;
    }

    private void initEnv() {
        mEnv = new Env();
        mEnv.applicationContext = getApplicationContext();
    }

    private void initMessageHandler() {
        mMessageHandlers = new ArrayList<>();
        mMessageHandlers.add(new FlowMessageHandler());
        mMessageHandlers.add(new WriteLogMessageHandler());
    }

    private void notifyMessageHandler(Message msg) {
        for (int i = 0, len = mMessageHandlers.size(); i < len; i++) {
            if (mMessageHandlers.get(i).handleMessage(msg, mEnv)) {
                break;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public static class Env {
        public Messenger sender;
        public Context applicationContext;
    }

    public static class InnerService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            ServiceStartUtils.setForeground(mInstance, this);
            return START_NOT_STICKY;
        }
    }
}

package com.vip001.monitor.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.vip001.monitor.common.MsgDef;
import com.vip001.monitor.services.flow.FlowMessageHandler;

import java.util.ArrayList;

/**
 * Created by xxd on 2018/9/19
 */
public class RemoteBackgroundService extends Service {
    private static final String TAG = "RemoteBackgroundService";
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
        initEnv();
        initMessageHandler();
    }


    private void initEnv() {
        mEnv = new Env();
        mEnv.applicationContext = getApplicationContext();
    }

    private void initMessageHandler() {
        mMessageHandlers = new ArrayList<>();
        mMessageHandlers.add(new FlowMessageHandler());
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
}

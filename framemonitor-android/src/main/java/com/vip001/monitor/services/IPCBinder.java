package com.vip001.monitor.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.vip001.monitor.common.MsgDef;
import com.vip001.monitor.utils.ServiceStartUtils;

import java.util.UUID;

/**
 * Created by xxd on 2018/9/20
 */
public class IPCBinder implements ServiceConnection {
    private static final String TAG = "IPCBinder";
    private Context mContext;
    private Handler mBackgroundMsgHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Messenger mSender = new Messenger(mBackgroundMsgHandler);
    private boolean isConnected;
    private String mTag = UUID.randomUUID().toString();

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public void connect(Context context) {
        if (context == null || isConnected) {
            return;
        }
        mContext = context.getApplicationContext();
        Intent intent = new Intent(mContext, RemoteBackgroundService.class);
        ServiceStartUtils.startService(mContext, intent);
        mContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        if (!isConnected) {
            return;
        }
        Intent intent = new Intent(mContext, RemoteBackgroundService.class);
        mContext.stopService(intent);
        mContext.unbindService(this);
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean sendMessage(Message msg) {
        if (isConnected) {
            try {
                mSender.send(msg);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendInitMsg() {
        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_INIT;
        msg.replyTo = new Messenger(mBackgroundMsgHandler);
        try {
            mSender.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "IPCBinder onServiceConnected");
        mSender = new Messenger(service);
        sendInitMsg();
        isConnected = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "IPCBinder onServiceDisconnected");
        isConnected = false;
        connect(mContext);

    }
}

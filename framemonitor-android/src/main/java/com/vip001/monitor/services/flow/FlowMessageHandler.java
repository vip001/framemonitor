package com.vip001.monitor.services.flow;

import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.vip001.monitor.common.MsgDef;
import com.vip001.monitor.services.IMessageHandler;
import com.vip001.monitor.services.RemoteBackgroundService;
import com.vip001.monitor.utils.ThreadUtils;

/**
 * Created by xxd on 2018/10/5.
 */

public class FlowMessageHandler implements IMessageHandler {
    private FlowCaculater mCaculater;
    private FlowWriteTask mTask;

    public FlowMessageHandler() {
        mCaculater = new FlowCaculater();
        mTask = new FlowWriteTask();
    }

    @Override
    public boolean handleMessage(Message msg, RemoteBackgroundService.Env env) {
        boolean result;
        switch (msg.what) {
            case MsgDef.MSG_START_FLOW:
                mCaculater.start();
                result = true;
                break;
            case MsgDef.MSG_END_FLOW:
                String text = mCaculater.end();
                mTask.setText(text);
                Log.i("xxd","endflow="+text);
               ThreadUtils.getInstance().execute(mTask);
                Toast.makeText(env.applicationContext, text, Toast.LENGTH_LONG).show();
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }
}

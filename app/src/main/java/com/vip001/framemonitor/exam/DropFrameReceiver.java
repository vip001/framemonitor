package com.vip001.framemonitor.exam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vip001.framemonitor.JANKSwitch;

/**
 * @author xxd
 * @date 2020-01-16
 */
public class DropFrameReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JANKSwitch.DEBUG_JANK) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

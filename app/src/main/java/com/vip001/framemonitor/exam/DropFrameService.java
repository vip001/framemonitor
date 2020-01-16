package com.vip001.framemonitor.exam;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.vip001.framemonitor.JANKSwitch;

/**
 * @author xxd
 * @date 2020-01-16
 */
public class DropFrameService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (JANKSwitch.DEBUG_JANK) {
                Thread.sleep(300);
            }
            Log.i("DropFrameService", "onStartCommand(Intent intent, int flags, int startId)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

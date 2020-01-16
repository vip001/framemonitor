package com.vip001.framemonitor;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.vip001.monitor.core.FrameMonitorManager;

import java.util.List;

/**
 * Created by xxd on 2018/8/9
 */
public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



        if (!isMainProcess()) {
            return;
        }
        FrameMonitorManager.getInstance().init(this).start();
        BlockCanary.install(this, new BlockCanaryContext(){
            @Override
            public int provideBlockThreshold() {
                return (int) (IConfig.FRAME_INTERVALS * 8)/1000000;
            }
        }).start();
        FrameMonitorUtils.mLastPrinter = FrameMonitorUtils.getMessageLogging();
    }

    private boolean isMainProcess() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
        String packageName = getPackageName();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (packageName.equals(info.processName) && info.pid == Process.myPid()) {
                return true;
            }
        }
        return false;
    }
}

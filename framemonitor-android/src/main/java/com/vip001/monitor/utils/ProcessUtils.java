package com.vip001.monitor.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.vip001.monitor.BuildConfig;

import java.util.List;

/**
 * Created by xxd on 2018/12/10
 */
public class ProcessUtils {
    public interface Type {
        int PROCESS_FOREGROUND = 0;
        int PROCESS_BACKGROUND = 1;
        int PROCESS_OTHER = 2;
    }

    public static int classifyProcess(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
        String foregroundProcessName = context.getPackageName();
        String backgroundProcessName = new StringBuilder(foregroundProcessName).append(BuildConfig.BACKGROUND_PROCESS_NAME).toString();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == Process.myPid()) {
                if (foregroundProcessName.equals(info.processName)) {
                    return Type.PROCESS_FOREGROUND;
                } else if (backgroundProcessName.equals(info.processName)) {
                    return Type.PROCESS_BACKGROUND;
                } else {
                    return Type.PROCESS_OTHER;
                }
            }
        }
        return Type.PROCESS_OTHER;
    }

}

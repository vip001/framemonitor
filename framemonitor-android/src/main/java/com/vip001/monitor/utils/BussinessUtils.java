package com.vip001.monitor.utils;

import com.vip001.monitor.bean.DropFramesBean;

/**
 * Created by xxd on 2018/11/25.
 */

public class BussinessUtils {
    private static final String[] sFilterStr = new String[]{
            "android.os.Parcel.nativeWriteInterfaceToken(Native Method)",
            "android.os.MessageQueue.nativePollOnce(Native Method)",
            "android.view.ThreadedRenderer.nInitialize(Native Method)"
    };

    public static String getDropFramesTarget(DropFramesBean bean, boolean simple) {
        if (bean == null) return "";
        return bean.isForeground ? (simple ? bean.topActivitySimpleName : bean.topActivityName) : "Background";
    }


    public static final String META_LOG_MAIN_STACK = "--------MainThread Stack Before JANK--------";
    public static final String META_LOG_MESSAGE = "--------MainThread Message--------";
    public static final String META_THREAD_STACK = "--------Thread Stack--------";
    public static final String META_BASE_MESSAGE = "--------Base Message--------";
    public static final String META_STACK_TIME_START = "#timestart";
    public static final String META_STACK_TIME_END = "#timeend";
    public static final String META_STACK_START = "#stackstart";
    public static final String META_STACK_END = "#stackend";
}

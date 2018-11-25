package com.vip001.monitor.utils;

/**
 * Created by xxd on 2018/11/25.
 */

public class BussinessUtils {
    private static final String[] sFilterStr = new String[]{
            "android.os.Parcel.nativeWriteInterfaceToken(Native Method)",
            "android.os.MessageQueue.nativePollOnce(Native Method)",
            "android.view.ThreadedRenderer.nInitialize(Native Method)"
    };

    public static String getTitle() {
        String.format("Thread.");
        return null;
    }

    public static String getItem(String activity) {
        return new StringBuilder().append(activity).append(" drop frames").toString();
    }

}

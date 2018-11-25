package com.vip001.monitor.utils;

/**
 * Created by xxd on 2018/11/25.
 */

public class BussinessUtils {
    public static String getTitle() {
        String.format("Thread.");
        return null;
    }

    public static String getItem(String activity) {
        return new StringBuilder().append(activity).append(" drop frames").toString();
    }

}

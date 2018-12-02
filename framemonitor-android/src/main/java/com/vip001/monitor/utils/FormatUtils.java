package com.vip001.monitor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xxd on 2018/8/7
 */
public class FormatUtils {
    public static CharSequence formatFrameCostTime(long ns) {
        return String.format("%.3f", ns * 1.0f / 1000000);
    }

    public static CharSequence formatStandartFrameTime(float ns) {
        return String.format("%.2f", ns / 1000000);
    }

    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final SimpleDateFormat sTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String formatDate(Date date) {
        return sDateFormat.format(date);
    }

    public static String formatDate(long time) {
        Date date = new Date();
        date.setTime(time);
        return sTimeFormat.format(date);
    }

}

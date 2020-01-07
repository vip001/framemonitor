package com.vip001.framemonitor;

import android.os.Looper;
import android.util.Printer;

import java.lang.reflect.Field;

/**
 * @author xxd
 * @date 2020-01-07
 */
public class FrameMonitorUtils {
    public static Printer getMessageLogging() {

        Looper looper = Looper.getMainLooper();
        Field field = null;
        try {
            field = looper.getClass().getDeclaredField("mLogging");
        } catch (Exception e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        try {
            return (Printer) field.get(looper);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Printer mLastPrinter;
}

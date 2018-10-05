package com.vip001.monitor.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by xxd on 2018/10/4.
 */

public class IOUtils {
    public static void closeQuietly(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stream = null;
            }
        }
    }
}

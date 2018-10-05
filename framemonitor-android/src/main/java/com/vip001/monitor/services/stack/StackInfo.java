package com.vip001.monitor.services.stack;

/**
 * Created by xxd on 2018/8/16
 */
public class StackInfo {
    String msg;
    long time;

    public StackInfo(String msg) {
        this.msg = msg;
        this.time = System.currentTimeMillis();
    }

    public StackInfo(String msg, long time) {
        this.msg = msg;
        this.time = time;
    }

}

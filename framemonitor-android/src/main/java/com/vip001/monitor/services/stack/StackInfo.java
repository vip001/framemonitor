package com.vip001.monitor.services.stack;

/**
 * Created by xxd on 2018/8/16
 */
public class StackInfo {
    public StringBuilder msg;
    public long time;
    public String timeString;

    public StackInfo(StringBuilder msg) {
        this.msg = msg;
        this.time = System.currentTimeMillis();
    }

    public StackInfo() {

    }

    public StackInfo(StringBuilder msg, long time) {
        this.msg = msg;
        this.time = time;
    }

}

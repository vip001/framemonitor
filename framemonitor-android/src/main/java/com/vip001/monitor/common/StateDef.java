package com.vip001.monitor.common;

/**
 * Created by xxd on 2018/12/14
 */
public interface StateDef {
    int ENABLE_SHOW_FLOW = 1 << 0;
    int ENABLE_SHOW_BALL = 1 << 1;
    int ENABLE_MONITOR_WORK = 1 << 2;
    int ENABLE_MONITOR_BACKGROUND = 1 << 3;
}

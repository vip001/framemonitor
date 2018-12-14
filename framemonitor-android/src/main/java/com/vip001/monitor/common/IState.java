package com.vip001.monitor.common;

/**
 * Created by xxd on 2018/12/14
 */
public interface IState {
    void setState(int state);
    boolean hasState(int state);
    void clearState(int state);
    void resetState();
}

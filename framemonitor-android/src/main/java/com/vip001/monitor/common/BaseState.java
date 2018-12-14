package com.vip001.monitor.common;

/**
 * Created by xxd on 2018/12/14
 */
public class BaseState implements IState {
    protected int mState = 0;

    @Override
    public void setState(int state) {
        mState = mState | state;
    }

    @Override
    public boolean hasState(int state) {
        return (mState & state) == state;
    }

    @Override
    public void clearState(int state) {
        mState = mState & ~state;
    }

    @Override
    public void resetState() {
        mState = 0;
    }

    public int getState() {
        return mState;
    }
}

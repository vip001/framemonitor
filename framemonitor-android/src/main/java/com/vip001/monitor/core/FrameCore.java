package com.vip001.monitor.core;

import android.view.Choreographer;

import com.vip001.monitor.utils.CallbackManager;

/**
 * Created by xxd on 2018/8/6
 */
class FrameCore implements Choreographer.FrameCallback, CallbackManager.TraversalCallback<FrameCore.FrameCoreCallback> {
    private boolean hasStarted;
    private CallbackManager<FrameCoreCallback> mCallbackManager;
    private static final FrameCore sInstance = new FrameCore();
    private long mLastFrameNanoTime;
    private long mFrameInterval;

    public static FrameCore getInstance() {
        return sInstance;
    }

    private FrameCore() {
        hasStarted = false;
        mCallbackManager = new CallbackManager<>();
    }

    public FrameCore registerCallback(FrameCoreCallback callback) {
        mCallbackManager.addCallback(callback);
        return this;
    }

    public FrameCore unregisterCallback(FrameCoreCallback callback) {
        mCallbackManager.removeCallback(callback);
        return this;
    }

    public boolean isStarted() {
        return hasStarted;
    }

    public FrameCore start() {
        Choreographer.getInstance().postFrameCallback(this);
        hasStarted = true;
        return this;
    }

    public FrameCore stop() {
        hasStarted = false;
        return this;
    }

    @Override
    public void doFrame(final long frameTimeNanos) {
        if (mLastFrameNanoTime == 0) {
            mLastFrameNanoTime = frameTimeNanos;
        } else {
            mFrameInterval = frameTimeNanos - mLastFrameNanoTime;
            mCallbackManager.traversal(this);
        }
        if (hasStarted) {
            mLastFrameNanoTime = frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
        } else {
            mLastFrameNanoTime = 0;
        }
    }

    @Override
    public void execute(FrameCoreCallback callback) {
        callback.update(mFrameInterval);
    }

    public interface FrameCoreCallback {
        void update(long ns);
    }
}

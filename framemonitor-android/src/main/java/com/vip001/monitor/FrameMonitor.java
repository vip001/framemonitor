package com.vip001.monitor;

import android.view.Choreographer;

import com.vip001.monitor.utils.CallbackManager;

/**
 * Created by xxd on 2018/8/6
 */
class FrameMonitor implements Choreographer.FrameCallback, CallbackManager.TraversalCallback<FrameMonitor.FrameMonitorCallback> {
    private boolean hasStarted;
    private CallbackManager<FrameMonitorCallback> mCallbackManager;
    private static final FrameMonitor sInstance = new FrameMonitor();
    private long mLastFrameNanoTime;
    private long mFrameInterval;

    public static FrameMonitor getInstance() {
        return sInstance;
    }

    private FrameMonitor() {
        hasStarted = false;
        mCallbackManager = new CallbackManager<>();
    }

    public FrameMonitor registerCallback(FrameMonitorCallback callback) {
        mCallbackManager.addCallback(callback);
        return this;
    }

    public FrameMonitor unregisterCallback(FrameMonitorCallback callback) {
        mCallbackManager.removeCallback(callback);
        return this;
    }

    public boolean isStarted() {
        return hasStarted;
    }

    public FrameMonitor start() {
        Choreographer.getInstance().postFrameCallback(this);
        hasStarted = true;
        return this;
    }

    public FrameMonitor stop() {
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
    public void execute(FrameMonitorCallback callback) {
        callback.update(mFrameInterval);
    }

    public interface FrameMonitorCallback {
        void update(long ns);
    }
}

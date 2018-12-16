package com.vip001.monitor.services.stack;

import android.os.Handler;
import android.os.Looper;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by xxd on 2018/8/16
 */
public class MainStackCollectTask implements Runnable {

    private static final HashSet<String> sFilterStack;

    static {
        sFilterStack = new HashSet<String>();
        sFilterStack.add("android.os.Parcel.nativeWriteInterfaceToken(Native Method)");
        sFilterStack.add("android.os.BinderProxy.transactNative(Native Method)");
        sFilterStack.add("android.os.MessageQueue.nativePollOnce(Native Method)");
        sFilterStack.add("android.view.Surface.nativeAllocateBuffers(Native Method)");
        sFilterStack.add("android.view.ThreadedRenderer.nInitialize(Native Method)");
        sFilterStack.add("android.view.ThreadedRenderer.nSyncAndDrawFrame(Native Method)");
        sFilterStack.add("android.view.ThreadedRenderer.nLoadSystemProperties(Native Method)");
        sFilterStack.add("android.view.ThreadedRenderer.nFence(Native Method)");
        sFilterStack.add("android.view.GraphicBuffer.nReadGraphicBufferFromParcel(Native Method)");
        sFilterStack.add("android.os.BinderProxy.transact(Native Method)");
        sFilterStack.add("android.view.ThreadedRenderer.nPauseSurface(Native Method)");
        sFilterStack.add("android.view.ThreadedRenderer.nDestroyHardwareResources(Native Method)");
    }

    private Handler mLaucherHandler;
    private long mLastDumpTime;
    private LinkedList<StackInfo> mStacks = new LinkedList<>();
    private boolean isDoing;
    private static final long INTERVAL_COLLECT = 50;

    public boolean isDoing() {
        return isDoing;
    }

    public void start(Handler laucher) {
        if (laucher == null) {
            return;
        }

        mLaucherHandler = laucher;
        isDoing = true;
        mLaucherHandler.post(this);
    }

    public void stop() {
        if (mLaucherHandler == null) {
            mLaucherHandler.removeCallbacks(this);
        }
        isDoing = false;
    }

    public LinkedList<StackInfo> getStacks() {
        return mStacks;
    }

    public void clearStack() {
        mStacks = new LinkedList<>();
    }

    @Override
    public void run() {
        if (!isDoing) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - mLastDumpTime;
        if (interval >= INTERVAL_COLLECT) {
            StackTraceElement[] elements = Looper.getMainLooper().getThread().getStackTrace();
            String targetText = elements[0].toString();
            if (!targetText.startsWith("com.vip001.monitor") && !sFilterStack.contains(elements[0].toString())) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0, len = elements.length; i < len; i++) {
                    if (i != 0) {
                        builder.append("\r\n");
                    }
                    builder.append(elements[i].toString());
                }
                if (mStacks.size() > 5) {
                    mStacks.removeFirst();
                }
                mStacks.addLast(new StackInfo(builder, currentTime));
            }
            mLastDumpTime = currentTime;
            mLaucherHandler.postDelayed(this, INTERVAL_COLLECT);

        } else {
            mLaucherHandler.postDelayed(this, INTERVAL_COLLECT - interval);
        }
    }
}

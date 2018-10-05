package com.vip001.monitor.services.stack;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;

/**
 * Created by xxd on 2018/8/16
 */
public class MainStackCollectTask implements Runnable {


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
            StringBuilder builder = new StringBuilder();
            StackTraceElement[] elements = Looper.getMainLooper().getThread().getStackTrace();
            for (int i = 0, len = elements.length; i < len; i++) {
                if (i != 0) {
                    builder.append("\r\n");
                }
                builder.append(elements[i].toString());
            }
            if (mStacks.size() > 10) {
                mStacks.removeFirst();
            } else {
                mStacks.addLast(new StackInfo(builder.toString(), currentTime));
            }
            mLastDumpTime = currentTime;
            mLaucherHandler.postDelayed(this, INTERVAL_COLLECT);

        } else {
            mLaucherHandler.postDelayed(this, INTERVAL_COLLECT - interval);
        }
    }
}

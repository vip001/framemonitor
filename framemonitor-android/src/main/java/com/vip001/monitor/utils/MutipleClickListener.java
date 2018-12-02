package com.vip001.monitor.utils;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by xxd on 2018/12/1.
 */

abstract public class MutipleClickListener implements View.OnClickListener {
    private long[] mHits;

    public MutipleClickListener(int count) {
        mHits = new long[count];
    }

    @Override
    public void onClick(View view) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= mHits[mHits.length - 1] - 500) {
            onTrigerClick(view);
        }
    }

    abstract public void onTrigerClick(View view);
}

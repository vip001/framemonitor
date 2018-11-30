package com.vip001.monitor.services.flow;

import android.net.TrafficStats;

import java.text.DecimalFormat;

/**
 * Created by xxd on 2018/9/21
 */
public class FlowCaculater {
    private long mOriginSendFlow;
    private long mOriginRecvFlow;
    private boolean mSendStarted;
    private boolean mRecvStarted;

    public FlowCaculater originSend(long bytes) {
        mOriginSendFlow = bytes;
        mSendStarted = true;
        return this;
    }

    public FlowCaculater originRecv(long bytes) {
        mOriginRecvFlow = bytes;
        mRecvStarted = true;
        return this;
    }

    public long endSend(long bytes) {
        if (mSendStarted) {
            mSendStarted = false;
            return bytes - mOriginSendFlow;
        } else {
            return 0;
        }

    }

    public long endRecv(long bytes) {
        if (mRecvStarted) {
            mRecvStarted = false;
            return bytes - mOriginRecvFlow;
        } else {
            return 0;
        }

    }

    public FlowCaculater startCalSend() {
        originSend(TrafficStats.getUidTxBytes(android.os.Process.myUid()));
        return this;
    }

    public long stopCalSend() {
        return endSend(TrafficStats.getUidTxBytes(android.os.Process.myUid()));
    }

    public FlowCaculater startCalRecv() {
        originRecv(TrafficStats.getUidRxBytes(android.os.Process.myUid()));
        return this;
    }

    public long stopCalRecv() {
        return endRecv(TrafficStats.getUidRxBytes(android.os.Process.myUid()));
    }

    public String endCalSend() {
        return formateFileSize(endSend(TrafficStats.getUidTxBytes(android.os.Process.myUid())));
    }

    public String endCalRecv() {
        return formateFileSize(endRecv(TrafficStats.getUidRxBytes(android.os.Process.myUid())));
    }

    public FlowCaculater start() {
        return startCalSend().startCalRecv();

    }

    public String end() {
        return new StringBuilder("下行流量：").append(endCalRecv()).append(";上行流量：").append(endCalSend()).toString();
    }

    private static final long KB = 1000;
    private static final long MB = 1000 * KB;
    private static final long GB = 1000 * MB;
    private static final DecimalFormat sFormat = new DecimalFormat("#.00");

    private String formateFileSize(long size) {
        if (size < KB) {
            return size + "B";
        } else if (size < MB) {
            return sFormat.format(size * 1.0f / KB) + "KB";
        } else if (size < GB) {
            return sFormat.format(size * 1.0f / MB) + "MB";
        } else {
            return sFormat.format(size * 1.0f / GB) + "GB";
        }
    }
}

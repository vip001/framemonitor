package com.vip001.monitor.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.vip001.monitor.utils.FormatUtils;

public class DropFramesBean implements Parcelable {
    public String topActivityName;
    public String topActivitySimpleName;
    public boolean isForeground;
    public long frameCostTime;
    public long happensTime;
    private static final DropFramesBean sInstance = new DropFramesBean();
    public static final String KEY_TRANSACT = "dropframesbean";

    public DropFramesBean() {

    }

    protected DropFramesBean(Parcel in) {
        topActivityName = in.readString();
        topActivitySimpleName = in.readString();
        isForeground = in.readByte() != 0;
        frameCostTime = in.readLong();
        happensTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topActivityName);
        dest.writeString(topActivitySimpleName);
        dest.writeByte((byte) (isForeground ? 1 : 0));
        dest.writeLong(frameCostTime);
        dest.writeLong(happensTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DropFramesBean> CREATOR = new Creator<DropFramesBean>() {
        @Override
        public DropFramesBean createFromParcel(Parcel in) {
            return new DropFramesBean(in);
        }

        @Override
        public DropFramesBean[] newArray(int size) {
            return new DropFramesBean[size];
        }
    };

    public static DropFramesBean getInstance() {
        return sInstance;
    }

    public String toMinString() {
        return new StringBuilder().append(topActivityName).append("#").append(isForeground).append("#").append(frameCostTime).append("#").append(happensTime).toString();
    }

    public String toExplicitString() {
        return new StringBuilder().append("topActivity:").append(topActivityName).append("\r\n")
                .append("isForeground=").append(isForeground).append("\r\n")
                .append("frameCostTime:").append(frameCostTime).append("\r\n")
                .append("happensTime:").append(FormatUtils.formatDate(happensTime)).toString();
    }
}

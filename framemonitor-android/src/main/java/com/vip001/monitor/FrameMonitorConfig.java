package com.vip001.monitor;

import com.vip1002.framemonitor.IConfig;

/**
 * Created by xxd on 2018/8/20
 */

class FrameMonitorConfig {

    private static FrameMonitorConfig sInstance = new FrameMonitorConfig();
    private IConfig mConfig = new IConfig() {
        @Override
        public int sortTime(long time) {
            long frames = time / IConfig.FRAME_INTERVALS;

            if (frames == 0) {
                return RESULT_GREEN;
            } else if (frames < 3) {
                return RESULT_YELLOW;
            } else {
                return RESULT_RED;
            }
        }
    };

    public static FrameMonitorConfig getInstance() {
        return sInstance;
    }

    public FrameMonitorConfig setConfig(IConfig config) {
        if (config == null) {
            return this;
        }
        this.mConfig = config;
        return this;
    }

    public IConfig getConfig() {
        return mConfig;
    }

    public int sortTime(long time) {
        return mConfig.sortTime(time);
    }

}

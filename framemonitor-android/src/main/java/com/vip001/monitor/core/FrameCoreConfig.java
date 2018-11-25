package com.vip001.monitor.core;

import com.vip001.framemonitor.IConfig;

/**
 * Created by xxd on 2018/8/20
 */

class FrameCoreConfig {

    private static FrameCoreConfig sInstance = new FrameCoreConfig();
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

    public static FrameCoreConfig getInstance() {
        return sInstance;
    }

    public FrameCoreConfig setConfig(IConfig config) {
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

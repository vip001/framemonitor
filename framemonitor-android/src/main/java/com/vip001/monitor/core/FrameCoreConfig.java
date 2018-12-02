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
            FrameCoreConfigPersistence.Config config = FrameCoreConfigPersistence.getInstance().getConfig();
            if (time > config.redTime) {
                return RESULT_RED;
            } else if (time > config.yellowTime) {
                return RESULT_YELLOW;
            } else {
                return RESULT_GREEN;
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

package com.vip001.monitor.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.vip001.framemonitor.IConfig;

/**
 * Created by xxd on 2018/12/2.
 */

public class FrameCoreConfigPersistence {
    private SharedPreferences mPreference;
    private static final String KEY_RED = "60c43c46f5201cd4f79150e867464636";
    private static final String KEY_YELLOW = "e93d645782a8e15022d3e4479b1351af";
    private static final FrameCoreConfigPersistence sIntance = new FrameCoreConfigPersistence();
    private Config mConfig;

    private FrameCoreConfigPersistence() {

    }

    public FrameCoreConfigPersistence init(Context context) {
        mPreference = context.getSharedPreferences("framecore", Context.MODE_PRIVATE);
        float redTime = mPreference.getFloat(KEY_RED, 0);
        float yellowTime = mPreference.getFloat(KEY_YELLOW, 0);
        Config config = new Config();
        if (redTime > 0 && yellowTime > 0 && redTime > yellowTime) {
            config.redTime = redTime;
            config.yellowTime = yellowTime;
        } else {
            config.redTime = IConfig.FRAME_INTERVALS * 4;
            config.yellowTime = IConfig.FRAME_INTERVALS;
        }
        mConfig = config;
        return this;
    }

    public static final FrameCoreConfigPersistence getInstance() {
        return sIntance;
    }


    public Config getConfig() {
        return mConfig;
    }

    public FrameCoreConfigPersistence applyConfig(Config config) {
        if (config == null) {
            return this;
        }
        mPreference.edit().putFloat(KEY_RED, config.redTime).putFloat(KEY_YELLOW, config.yellowTime).apply();

        if (config.redTime > 0 && config.yellowTime > 0 && config.redTime > config.yellowTime) {
            mConfig = config;
        }
        return this;
    }

    public FrameCoreConfigPersistence applyConfig(float redTime, float yellowTime) {
        Config config = new Config();
        config.redTime = redTime * 1000000;
        config.yellowTime = yellowTime * 1000000;
        return applyConfig(config);
    }

    public static class Config {
        public float redTime;
        public float yellowTime;
    }
}

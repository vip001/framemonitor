package com.vip001.framemonitor;

import android.app.Activity;
import android.os.Bundle;

import com.vip001.monitor.FrameMonitorManager;

/**
 * Created by xxd on 2018/8/14
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameMonitorManager.getInstance().show(this);
    }

}

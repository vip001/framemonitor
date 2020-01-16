package com.vip001.framemonitor.exam;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vip001.framemonitor.BaseActivity;
import com.vip001.framemonitor.JANKSwitch;
import com.vip001.framemonitor.R;
import com.vip001.monitor.core.FrameMonitorManager;

/**
 * @author xxd
 * @date 2020-01-16
 */
public class ReceiverDropFrameActivity extends BaseActivity {
    private Button mJANKBtn;
    private Button mStartBtn;
    private Button mSendBtn;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new DropFrameReceiver();
        this.registerReceiver(mReceiver, new IntentFilter("ReceiverDropFrameActivity"));
        setContentView(R.layout.activity_receiver);
        mSendBtn = this.findViewById(R.id.btn_send);

        mStartBtn = this.findViewById(R.id.stop);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FrameMonitorManager.getInstance().isStarted()) {
                    FrameMonitorManager.getInstance().stop();
                } else {
                    FrameMonitorManager.getInstance().start().setEnableShow(true);
                }
                updateStartText();
            }
        });


        mJANKBtn = (Button) this.findViewById(R.id.btn);

        mJANKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JANKSwitch.DEBUG_JANK = !JANKSwitch.DEBUG_JANK;
                updateJANKText();
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("ReceiverDropFrameActivity");
                ReceiverDropFrameActivity.this.sendBroadcast(intent);
            }
        });
        updateJANKText();
        updateStartText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void updateJANKText() {
        if (JANKSwitch.DEBUG_JANK) {
            mJANKBtn.setText("关闭掉帧");
        } else {
            mJANKBtn.setText("开启掉帧");
        }
    }

    private void updateStartText() {
        if (FrameMonitorManager.getInstance().isStarted()) {
            mStartBtn.setText("停止检测");
        } else {
            mStartBtn.setText("开启检测");
        }
    }
}

package com.vip001.framemonitor.exam;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vip001.framemonitor.BaseActivity;
import com.vip001.framemonitor.JANKSwitch;
import com.vip001.framemonitor.R;
import com.vip001.monitor.core.FrameMonitorManager;

/**
 * Created by xxd on 2018/8/14
 */
public class Example2Activity extends BaseActivity {
    private ImageView mImageView;
    private Button mJANKBtn;
    private Button mStartBtn;
    private ValueAnimator mValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valueanimator);
        mImageView = this.findViewById(R.id.imageView);
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
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(4000);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (JANKSwitch.DEBUG_JANK) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mImageView.setImageAlpha((int) (255 * animation.getAnimatedFraction()));
            }
        });
        mValueAnimator.start();
        mJANKBtn = (Button) this.findViewById(R.id.btn);

        mJANKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JANKSwitch.DEBUG_JANK = !JANKSwitch.DEBUG_JANK;
                updateJANKText();
            }
        });
        updateJANKText();
        updateStartText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mValueAnimator.cancel();
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

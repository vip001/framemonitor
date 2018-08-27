package com.vip001.monitor.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vip001.monitor.FrameMonitorManager;
import com.vip001.monitor.R;
import com.vip001.monitor.utils.DimentionUtils;
import com.vip001.monitor.utils.FormatUtils;
import com.vip001.monitor.view.snackbar.SnackbarBaseViewHolder;
import com.vip1002.framemonitor.IConfig;

/**
 * Created by xxd on 2018/8/7
 */
public class FloatBallViewHolder extends SnackbarBaseViewHolder<Object> {

    private TextView mTextView;
    private float mLastY;
    private float mLastX;

    public FloatBallViewHolder(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        mTextView = getRootView().findViewById(R.id.text);
        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastY = event.getY();
                        mLastX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:

                    case MotionEvent.ACTION_UP:
                        int dy = (int) (event.getY() - mLastY);
                        int dx = (int) (event.getX() - mLastX);
                        int left = mTextView.getLeft() + dx;
                        int top = mTextView.getTop() + dy;
                        int right = mTextView.getRight() + dx;
                        int bottom = mTextView.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + mTextView.getWidth();
                        }
                        if (right > DimentionUtils.SCREEN_WIDTH) {
                            right = DimentionUtils.SCREEN_WIDTH;
                            left = right - mTextView.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = mTextView.getHeight();
                        }
                        if (bottom > DimentionUtils.SCREEN_HEIGHT) {
                            bottom = DimentionUtils.SCREEN_HEIGHT;
                            top = bottom - mTextView.getHeight();
                        }

                        mTextView.layout(left, top, right, bottom);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public View onCreateView(Context context, ViewGroup group) {
        return LayoutInflater.from(context).inflate(R.layout.monitor_floatball, group, false);
    }

    @Override
    protected void update(Object bean) {
        long time = (long) bean;
        int type = FrameMonitorManager.getInstance().getConfig().sortTime(time);
        switch (type) {
            case IConfig.RESULT_GREEN:
                mTextView.setSelected(false);
                mTextView.setActivated(false);
                break;
            case IConfig.RESULT_YELLOW:
                mTextView.setActivated(false);
                mTextView.setSelected(true);
                break;
            case IConfig.RESULT_RED:
                mTextView.setActivated(true);
                mTextView.setSelected(false);
                break;
            default:
                break;
        }
        mTextView.setText(FormatUtils.formatTime(time));
    }

    public void updateBall(final int[] pos) {
        if (pos == null || pos.length != 2) {
            return;
        }
        if (mTextView.getWidth() == 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTextView.getLayoutParams();
            params.leftMargin = pos[0];
            params.topMargin = pos[1];
            params.bottomMargin = 0;
            params.rightMargin = 0;
            mTextView.setLayoutParams(params);
        } else {
            mTextView.layout(pos[0], pos[1], pos[0] + mTextView.getWidth(), pos[1] + mTextView.getHeight());
        }

    }

    public int[] getBallPos() {
        int[] pos = new int[2];
        pos[0] = mTextView.getLeft();
        pos[1] = mTextView.getTop();
        return pos;
    }
}

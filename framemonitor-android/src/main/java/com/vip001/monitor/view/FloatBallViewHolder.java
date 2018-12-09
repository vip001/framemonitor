package com.vip001.monitor.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vip001.framemonitor.IConfig;
import com.vip001.monitor.R;
import com.vip001.monitor.core.FrameMonitorManager;
import com.vip001.monitor.dialog.SettingsDialog;
import com.vip001.monitor.utils.DimentionUtils;
import com.vip001.monitor.utils.FormatUtils;
import com.vip001.monitor.utils.MutipleClickListener;
import com.vip001.monitor.view.snackbar.SnackbarBaseViewHolder;

/**
 * Created by xxd on 2018/8/7
 */
public class FloatBallViewHolder extends SnackbarBaseViewHolder<Object> {

    private TextView mTextView;
    private float mLastY;
    private float mLastX;
    private static int MAX_MARGIN_WIDTH = 0;
    private static int MAX_MARGIN_HEIGHT = 0;
    private SettingsDialog mSettingsDialog;

    public FloatBallViewHolder(Activity context) {
        super(context);
    }

    @Override
    public void initView() {
        mSettingsDialog = new SettingsDialog(mContext);
        mTextView = getRootView().findViewById(R.id.text);
        mTextView.setOnClickListener(new MutipleClickListener(2) {

            @Override
            public void onTrigerClick(View view) {
                mSettingsDialog.show();
            }
        });
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                MAX_MARGIN_WIDTH = DimentionUtils.SCREEN_WIDTH - mTextView.getWidth();
                MAX_MARGIN_HEIGHT = DimentionUtils.SCREEN_HEIGHT - mTextView.getHeight();
            }
        });
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
                        updateBall(dx, dy);
                          /*int left = mTextView.getLeft() + dx;
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

                        mTextView.layout(left, top, right, bottom);*/
                        break;
                    default:
                        break;
                }
                return false;
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
        mTextView.setText(FormatUtils.formatFrameCostTime(time));
    }

    public void updateBall(final int[] pos) {
        if (pos == null || pos.length != 2) {
            return;
        }
        updateBall(pos[0], pos[1]);
    }

    public void updateBall(int dx, int dy) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTextView.getLayoutParams();
        int start = params.getMarginStart();
        int newStart = start + dx;
        if (newStart > MAX_MARGIN_WIDTH - params.getMarginEnd()) {
            newStart = MAX_MARGIN_WIDTH - params.getMarginEnd();
        } else if (newStart < 0) {
            newStart = 0;
        }
        int top = params.topMargin;
        int newTop = top + dy;
        if (newTop > MAX_MARGIN_HEIGHT - params.bottomMargin) {
            newTop = MAX_MARGIN_HEIGHT - params.bottomMargin;
        }
        if (newTop < 0) {
            newTop = 0;
        }
        params.setMarginStart(newStart);
        params.topMargin = newTop;
        mTextView.setLayoutParams(params);
    }

    public int[] getBallPos() {
        int[] pos = new int[2];
        pos[0] = mTextView.getLeft();
        pos[1] = mTextView.getTop();
        return pos;
    }

}

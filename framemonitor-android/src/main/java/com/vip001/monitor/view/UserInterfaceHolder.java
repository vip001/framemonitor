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
import com.vip001.monitor.core.FrameCoreConfigPersistence;
import com.vip001.monitor.core.FrameMonitorManager;
import com.vip001.monitor.dialog.SettingsDialog;
import com.vip001.monitor.utils.DimentionUtils;
import com.vip001.monitor.utils.FormatUtils;
import com.vip001.monitor.utils.MutipleClickListener;
import com.vip001.monitor.view.snackbar.SnackbarBaseViewHolder;

/**
 * Created by xxd on 2018/8/7
 */
public class UserInterfaceHolder extends SnackbarBaseViewHolder<Object> implements SettingsDialog.Callback {

    private TextView mBallView;
    private float mBallLastY;
    private float mBallLastX;
    private static int BALL_MAX_MARGIN_WIDTH = 0;
    private static int BALL_MAX_MARGIN_HEIGHT = 0;
    private SettingsDialog mSettingsDialog;
    private TextView mFlowCalView;

    public UserInterfaceHolder(Activity context) {
        super(context);
    }

    @Override
    public void initView() {
        mSettingsDialog = new SettingsDialog(mContext).setCallback(this);
        mBallView = getRootView().findViewById(R.id.text);
        mFlowCalView = getRootView().findViewById(R.id.tv_flow);
        mFlowCalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FrameMonitorManager.getInstance().hasStartFlowCal()) {
                    FrameMonitorManager.getInstance().stopFlowCal();

                } else {
                    FrameMonitorManager.getInstance().startFlowCal();
                }
                updateFlowText();
            }
        });
        updateFlowVisible();
        mBallView.setOnClickListener(new MutipleClickListener(2) {

            @Override
            public void onTrigerClick(View view) {
                mSettingsDialog.show();
            }
        });
        mBallView.post(new Runnable() {
            @Override
            public void run() {
                BALL_MAX_MARGIN_WIDTH = DimentionUtils.SCREEN_WIDTH - mBallView.getWidth();
                BALL_MAX_MARGIN_HEIGHT = DimentionUtils.SCREEN_HEIGHT - mBallView.getHeight();
            }
        });
        mBallView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBallLastX = event.getX();
                        mBallLastY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                    case MotionEvent.ACTION_UP:
                        int dy = (int) (event.getY() - mBallLastY);
                        int dx = (int) (event.getX() - mBallLastX);
                        updateBall(dx, dy);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void updateFlowText() {
        if (FrameMonitorManager.getInstance().hasStartFlowCal()) {
            mFlowCalView.setText("结束流量统计");
            mFlowCalView.setSelected(true);
        } else {
            mFlowCalView.setText("开始流量统计");
            mFlowCalView.setSelected(false);
        }
    }

    private void updateFlowVisible() {
        if (FrameCoreConfigPersistence.getInstance().getConfig().isOpen) {
            mFlowCalView.setVisibility(View.VISIBLE);
            updateFlowText();
        } else {
            mFlowCalView.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(Context context, ViewGroup group) {
        return LayoutInflater.from(context).inflate(R.layout.monitor_userinterface, group, false);
    }

    @Override
    protected void update(Object bean) {
        long time = (long) bean;
        int type = FrameMonitorManager.getInstance().getConfig().sortTime(time);
        switch (type) {
            case IConfig.RESULT_GREEN:
                mBallView.setSelected(false);
                mBallView.setActivated(false);
                break;
            case IConfig.RESULT_YELLOW:
                mBallView.setActivated(false);
                mBallView.setSelected(true);
                break;
            case IConfig.RESULT_RED:
                mBallView.setActivated(true);
                mBallView.setSelected(false);
                break;
            default:
                break;
        }
        mBallView.setText(FormatUtils.formatFrameCostTime(time));
    }

    public void updateBall(final int[] pos) {
        if (pos == null || pos.length != 2) {
            return;
        }
        updateBall(pos[0], pos[1]);
    }

    public void updateBall(int dx, int dy) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBallView.getLayoutParams();
        int start = params.getMarginStart();
        int newStart = start + dx;
        if (newStart > BALL_MAX_MARGIN_WIDTH - params.getMarginEnd()) {
            newStart = BALL_MAX_MARGIN_WIDTH - params.getMarginEnd();
        } else if (newStart < 0) {
            newStart = 0;
        }
        int top = params.topMargin;
        int newTop = top + dy;
        if (newTop > BALL_MAX_MARGIN_HEIGHT - params.bottomMargin) {
            newTop = BALL_MAX_MARGIN_HEIGHT - params.bottomMargin;
        }
        if (newTop < 0) {
            newTop = 0;
        }
        params.setMarginStart(newStart);
        params.topMargin = newTop;
        mBallView.setLayoutParams(params);
    }


    public int[] getBallPos() {
        int[] pos = new int[2];
        pos[0] = mBallView.getLeft();
        pos[1] = mBallView.getTop();
        return pos;
    }

    @Override
    public void onDismiss() {
        updateFlowVisible();
    }
}

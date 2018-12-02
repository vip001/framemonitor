package com.vip001.monitor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.vip001.monitor.R;

/**
 * Created by xxd on 2018/6/19
 */
public abstract class BasicDialog extends Dialog {
    protected View mRootView;

    public BasicDialog(@NonNull Context context) {
        this(context, R.style.monitor_dialognonestyle);
    }

    public BasicDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }
        getWindow().setDimAmount(0.4f);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        FrameLayout container = new FrameLayout(getContext());
        container.setMinimumWidth(metrics.widthPixels);
        container.setMinimumHeight(metrics.heightPixels);
        View view = onCreateView(container);
        mRootView = view;
        if (mRootView != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
            if (params == null) {
                params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            if (params.gravity == FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                params.gravity = Gravity.CENTER;
            }
            container.addView(mRootView, params);
        }
        initFeature();
        setContentView(container);
        initView(savedInstanceState);
    }

    protected void initFeature() {

    }

    abstract protected void initView(Bundle savedInstanceState);

    abstract protected View onCreateView(FrameLayout container);

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

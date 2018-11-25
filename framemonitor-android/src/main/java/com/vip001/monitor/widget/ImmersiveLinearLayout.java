package com.vip001.monitor.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.vip001.monitor.R;
import com.vip001.monitor.utils.DimentionUtils;
import com.vip001.monitor.utils.StatusBarUtils;

/**
 * Created by xxd on 2018/7/19
 */
public class ImmersiveLinearLayout extends LinearLayout {
    private View mStatusBar;

    public ImmersiveLinearLayout(Context context) {
        this(context, null);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            StatusBarUtils.setImmersiveStatusBar(activity);
            mStatusBar = new View(activity);
            addView(mStatusBar, 0, new LayoutParams(LayoutParams.MATCH_PARENT, DimentionUtils.getStatusBarHeight(context)));
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImmersiveLinearLayout);
        int color = typedArray.getColor(R.styleable.ImmersiveLinearLayout_statusbarcolor, Color.parseColor("#171220"));
        mStatusBar.setBackgroundColor(color);
    }

    public ImmersiveLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmersiveLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ImmersiveLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }


    public void setStatusBarColor(int color) {
        if (mStatusBar != null) {
            mStatusBar.setBackgroundColor(color);
        }
    }
}

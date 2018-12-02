package com.vip001.monitor.view;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.utils.StatusBarUtils;
import com.vip001.monitor.view.snackbar.SnackbarBaseViewHolder;

/**
 * Created by xxd on 2018/12/2.
 */

public class SettingsSnackBarViewHolder extends SnackbarBaseViewHolder<Object> {
    private EditText mEtRed;
    private EditText mEtYellow;
    private Callback mCallback;
    private View mContentContainer;

    public SettingsSnackBarViewHolder(Activity context) {
        super(context);
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public SettingsSnackBarViewHolder setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    @Override
    public void initView() {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        mRootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    exit();
                    return true;
                }
                return false;
            }
        });
        mEtRed = mRootView.findViewById(R.id.et_red);
        mEtYellow = mRootView.findViewById(R.id.et_yellow);
        mEtYellow.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    exit();
                }
                return false;
            }
        });
        mContentContainer = mRootView.findViewById(R.id.contentContainer);
        mContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (StatusBarUtils.isNavigationBarShow(mContext)) {
            mContentContainer.setPaddingRelative(mContentContainer.getPaddingStart(), mContentContainer.getPaddingTop(), mContentContainer.getPaddingEnd(), StatusBarUtils.getNavigationBarHeight(mContext));
        }
        mEtRed.requestFocus();

    }

    private void exit() {
        saveSettings();
        if (mCallback != null) {
            mCallback.onExit();
        }
    }

    private void saveSettings() {

    }

    @Override
    public View onCreateView(Context context, ViewGroup group) {
        return LayoutInflater.from(context).inflate(R.layout.monitor_setting_dialog_layout, group, false);
    }

    @Override
    protected void update(Object bean) {

    }

    public interface Callback {
        void onExit();
    }
}

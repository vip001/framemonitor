package com.vip001.monitor.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.common.StateDef;
import com.vip001.monitor.core.FrameCoreConfigPersistence;
import com.vip001.monitor.utils.FormatUtils;

/**
 * Created by xxd on 2018/11/30
 */
public class SettingsDialog extends BasicDialog {
    private EditText mEtRed;
    private EditText mEtYellow;
    private int mOriginSoftInputMode;
    private RadioGroup mFlowChoice;
    private Callback mCallback;

    public SettingsDialog(@NonNull Context context) {
        super(context);
    }

    public SettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public SettingsDialog setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                SettingsDialog.this.cancel();
            }
        });
        mEtRed = this.findViewById(R.id.et_red);
        mEtYellow = this.findViewById(R.id.et_yellow);
        mFlowChoice = this.findViewById(R.id.flow_container);
        mEtYellow.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveSettings();
                    SettingsDialog.this.cancel();
                }
                return false;
            }
        });
        if (FrameCoreConfigPersistence.getInstance().hasState(StateDef.ENABLE_SHOW_FLOW)) {
            mFlowChoice.check(R.id.open);
        } else {
            mFlowChoice.check(R.id.close);
        }
    }

    private void readConfig() {
        FrameCoreConfigPersistence.Config config = FrameCoreConfigPersistence.getInstance().getConfig();
        mEtRed.setText("" + FormatUtils.formatStandartFrameTime(config.redTime));
        mEtRed.setSelection(mEtRed.getText().length());
        mEtYellow.setText("" + FormatUtils.formatStandartFrameTime(config.yellowTime));
        mFlowChoice.check(FrameCoreConfigPersistence.getInstance().hasState(StateDef.ENABLE_SHOW_FLOW) ? R.id.open : R.id.close);
    }

    @Override
    public void show() {
        mOriginSoftInputMode = getWindow().getAttributes().softInputMode;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        super.show();
        readConfig();
        mEtRed.requestFocus();
    }

    @Override
    public void cancel() {
        getWindow().setSoftInputMode(mOriginSoftInputMode);
        if (mCallback != null) {
            mCallback.onDismiss();
        }
        super.cancel();
    }

    @Override
    public void dismiss() {
        getWindow().setSoftInputMode(mOriginSoftInputMode);
        if (mCallback != null) {
            mCallback.onDismiss();
        }
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveSettings();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveSettings() {
        String red = mEtRed.getText().toString();
        String yellow = mEtYellow.getText().toString();
        try {
            float redValue = Float.parseFloat(red);
            float yellowValue = Float.parseFloat(yellow);
            if (mFlowChoice.getCheckedRadioButtonId() == R.id.open) {
                FrameCoreConfigPersistence.getInstance().setState(StateDef.ENABLE_SHOW_FLOW);
            } else if (mFlowChoice.getCheckedRadioButtonId() == R.id.close) {
                FrameCoreConfigPersistence.getInstance().clearState(StateDef.ENABLE_SHOW_FLOW);
            }
            FrameCoreConfigPersistence.getInstance().applyConfig(redValue, yellowValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected View onCreateView(FrameLayout container) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.monitor_setting_dialog_layout, container, false);
    }

    private boolean isMatchDecimal(String decimals) {
        if (TextUtils.isEmpty(decimals)) {
            return false;
        }
        return decimals.matches("^\\d+(\\.\\d+)?");
    }

    public interface Callback {
        void onDismiss();
    }
}

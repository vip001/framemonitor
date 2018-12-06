package com.vip001.monitor.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.vip001.monitor.R;

/**
 * Created by xxd on 2018/12/4
 */
public class DeleteDialog extends BasicDialog {
    private Callback mCallback;

    public DeleteDialog(@NonNull Context context) {
        super(context);
    }

    public DeleteDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        this.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.this.cancel();
            }
        });
        this.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.sureClick();
                }
                DeleteDialog.this.cancel();
            }
        });
    }

    public DeleteDialog setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    @Override
    protected View onCreateView(FrameLayout container) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.monitor_delete_dialog_layout, container, false);
    }

    public interface Callback {
        void sureClick();
    }

}

package com.vip001.monitor.view.snackbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xxd on 2018/6/21
 */
public abstract class SnackbarBaseViewHolder<T> {
    protected View mRootView;
    protected Context mContext;
    protected T mData;

    public SnackbarBaseViewHolder(Context context) {
        mContext = context;
    }

    public abstract void initView();

    public abstract View onCreateView(Context context, ViewGroup group);

    public SnackbarBaseViewHolder<T> inflateView(ViewGroup group) {
        mRootView = onCreateView(mContext, group);
        initView();
        return this;
    }

    public SnackbarBaseViewHolder<T> setData(T data) {
        mData = data;
        update(mData);
        return this;
    }

    public T getData() {
        return mData;
    }

    public SnackbarBaseViewHolder<T> setType(int type) {
        return this;
    }

    protected abstract void update(T bean);

    public View getRootView() {
        return mRootView;
    }

}

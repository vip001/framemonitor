package com.vip001.monitor.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vip001.monitor.adapter.RecyclerViewAdapter;

/**
 * Created by xxd on 2018/11/24.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected RecyclerViewAdapter mAdpater;
    protected T mData;

    public BaseViewHolder(View itemView, RecyclerViewAdapter adapter) {
        super(itemView);
        mAdpater = adapter;
    }

    public void bindView(int position, T data) {
        mData = data;
        if (data == null) {
            return;
        }
        initialData(position, data);
    }

    protected abstract void initialData(int position, T data);

}

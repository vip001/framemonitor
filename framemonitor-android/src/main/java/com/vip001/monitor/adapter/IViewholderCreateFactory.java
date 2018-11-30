package com.vip001.monitor.adapter;

import android.view.ViewGroup;

import com.vip001.monitor.viewholder.BaseViewHolder;

/**
 * Created by xxd on 2018/11/24.
 */

public interface IViewholderCreateFactory {
    BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType, RecyclerViewAdapter adapter);
}

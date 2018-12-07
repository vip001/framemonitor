package com.vip001.monitor.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.vip001.monitor.bean.BaseRecyclerViewBean;
import com.vip001.monitor.utils.CommonUtils;
import com.vip001.monitor.viewholder.BaseViewHolder;
import com.vip001.monitor.viewholder.DefaultViewHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxd on 2018/11/24.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<BaseRecyclerViewBean> mDatas = new ArrayList<>();
    private IViewholderCreateFactory mViewHolderFactory;

    public RecyclerViewAdapter setData(List<BaseRecyclerViewBean> beans) {
        mDatas = beans;
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        notifyDataSetChanged();
        return this;
    }

    public void clearData() {
        mDatas = new ArrayList<>();
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter setViewHolderFactory(IViewholderCreateFactory factory) {
        this.mViewHolderFactory = factory;
        return this;
    }

    public RecyclerViewAdapter addData(List<BaseRecyclerViewBean> beans) {
        return insertData(beans, mDatas.size());
    }

    public RecyclerViewAdapter insertData(List<BaseRecyclerViewBean> beans, int pos) {
        if (CommonUtils.isEmpty(beans)) {
            return this;
        }
        if (pos < 0 || pos > mDatas.size()) {
            return this;
        }
        int startPos = pos;
        mDatas.addAll(startPos, beans);
        notifyItemRangeInserted(startPos, beans.size());
        return this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mViewHolderFactory == null) {
            return new DefaultViewHolder(parent, this);
        }
        BaseViewHolder holder = mViewHolderFactory.onCreateViewHolder(parent, viewType, this);
        if (holder == null) {
            holder = new DefaultViewHolder(parent, this);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).type;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Type type = holder.getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new RuntimeException("ViewHolder must be generic class");
        }
        Class genericClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        Object object = mDatas.get(position).data;
        if (!genericClass.isInstance(object)) {
            object = null;
        }
        holder.bindView(position, object);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}

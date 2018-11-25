package com.vip001.monitor.viewholder;

import android.view.ViewGroup;

import com.vip001.monitor.adapter.IViewholderCreateFactory;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.common.ViewType;

/**
 * Created by xxd on 2018/11/24.
 */

public class DropFramesDetailViewHolderFactory implements IViewholderCreateFactory {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType, RecyclerViewAdapter adapter) {
        BaseViewHolder holder = null;
        switch (viewType) {
            case ViewType.TYPE_STACk:
                holder = new StackViewHolder(parent, adapter);
                break;
            case ViewType.TYPE_TOP_ROW:
                holder = new TopRowViewHolder(parent, adapter);
                break;
            default:
                holder = new DefaultViewHolder(parent, adapter);
                break;
        }
        return holder;
    }
}

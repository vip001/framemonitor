package com.vip001.monitor.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.adapter.RecyclerViewAdapter;

/**
 * Created by xxd on 2018/11/24.
 */

public class TopRowViewHolder extends BaseViewHolder<String> {
    private TextView mCenterText;

    public TopRowViewHolder(ViewGroup viewGroup, RecyclerViewAdapter adapter) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monitor_ref_top_row, viewGroup, false), adapter);
        mCenterText = itemView.findViewById(R.id.text);
    }

    @Override
    protected void initialData(int position, String data) {
        mCenterText.setText(data);
    }
}

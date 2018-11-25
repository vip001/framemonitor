package com.vip001.monitor.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.activity.DropFramesDetailActivity;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.bean.ReyclerViewDropFramesDisplayBean;

/**
 * Created by xxd on 2018/11/25.
 */

public class DisplayDropFramesViewHolder extends BaseViewHolder<ReyclerViewDropFramesDisplayBean> {
    private TextView mContent;
    private TextView mDate;

    public DisplayDropFramesViewHolder(ViewGroup viewGroup, RecyclerViewAdapter adapter) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monitor_display_drop_frames_row, viewGroup, false), adapter);
        mContent = itemView.findViewById(R.id.content);
        mDate = itemView.findViewById(R.id.date);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropFramesDetailActivity.start(v.getContext());
            }
        });
    }

    @Override
    protected void initialData(int position, ReyclerViewDropFramesDisplayBean data) {
        mContent.setText(data.content);
        mDate.setText(data.date);
    }
}

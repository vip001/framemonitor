package com.vip001.monitor.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vip001.framemonitor.IConfig;
import com.vip001.monitor.R;
import com.vip001.monitor.activity.DropFramesDetailActivity;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.bean.LoadDataBean;
import com.vip001.monitor.utils.FormatUtils;

import java.util.Locale;

/**
 * Created by xxd on 2018/11/25.
 */

public class DisplayDropFramesViewHolder extends BaseViewHolder<LoadDataBean> {
    private TextView mContent;
    private TextView mDate;

    public DisplayDropFramesViewHolder(ViewGroup viewGroup, RecyclerViewAdapter adapter) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.monitor_display_drop_frames_row, viewGroup, false), adapter);
        mContent = itemView.findViewById(R.id.content);
        mDate = itemView.findViewById(R.id.date);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropFramesDetailActivity.start(v.getContext(), mData.fileName);
            }
        });
    }

    @Override
    protected void initialData(int position, LoadDataBean data) {
        StringBuilder builder = new StringBuilder();
        builder.append(data.dropFramesBean.topActivityName).append(" drop ").append(data.dropFramesBean.frameCostTime / IConfig.FRAME_INTERVALS - 1).append(" frames");
        mContent.setText(builder);
        mDate.setText(FormatUtils.formatTime(data.dropFramesBean.happensTime));
    }
}

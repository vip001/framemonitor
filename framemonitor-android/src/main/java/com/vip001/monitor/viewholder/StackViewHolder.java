package com.vip001.monitor.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.bean.RecyclerViewStackBean;
import com.vip001.monitor.widget.DisplayLeakConnectorView;
import com.vip001.monitor.widget.MoreDetailsView;

/**
 * Created by xxd on 2018/11/24.
 */

public class StackViewHolder extends BaseViewHolder<RecyclerViewStackBean> {
    private DisplayLeakConnectorView mConnector;
    private MoreDetailsView mMoreBtn;
    private TextView mTitle;
    private TextView mDetail;

    public StackViewHolder(ViewGroup parent, RecyclerViewAdapter adapter) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.monitor_ref_row, parent, false), adapter);
        mConnector = itemView.findViewById(R.id.row_connector);
        mMoreBtn = itemView.findViewById(R.id.row_more);
        mTitle = itemView.findViewById(R.id.row_title);
        mDetail = itemView.findViewById(R.id.row_details);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOpen(!mMoreBtn.isOpened());
            }
        });
    }

    @Override
    protected void initialData(int position, RecyclerViewStackBean data) {
        setOpen(data.isOpen);
        mTitle.setText(data.title);
        mDetail.setText(data.details);
        mConnector.setType(data.type);
    }

    public void setOpen(boolean isOpen) {
        mMoreBtn.setOpened(isOpen);
        mDetail.setVisibility(isOpen ? View.VISIBLE : View.GONE);

    }
}

package com.vip001.monitor.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.services.stack.StackInfo;
import com.vip001.monitor.widget.DisplayLeakConnectorView;
import com.vip001.monitor.widget.MoreDetailsView;

/**
 * Created by xxd on 2018/11/24.
 */

public class StackViewHolder extends BaseViewHolder<StackInfo> {
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
    protected void initialData(int position, StackInfo data) {
        mTitle.setText(new StringBuilder(data.timeString).append(" interval:").append(data.intervalFromHappensTime).append("ms"));
        mDetail.setText(data.msg);
        mConnector.setType(getType(position));
    }

    private DisplayLeakConnectorView.Type getType(int pos) {
        if (pos == 2) {
            return DisplayLeakConnectorView.Type.START_LAST_REACHABLE;
        } else if (pos == mAdpater.getItemCount() - 1) {
            return DisplayLeakConnectorView.Type.END_FIRST_UNREACHABLE;
        } else {
            return DisplayLeakConnectorView.Type.NODE_UNKNOWN;
        }
    }

    public void setOpen(boolean isOpen) {
        mMoreBtn.setOpened(isOpen);
        mDetail.setVisibility(isOpen ? View.VISIBLE : View.GONE);

    }
}

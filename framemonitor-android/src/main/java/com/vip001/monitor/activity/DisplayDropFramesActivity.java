package com.vip001.monitor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.bean.BaseRecyclerViewBean;
import com.vip001.monitor.bean.ReyclerViewDropFramesDisplayBean;
import com.vip001.monitor.common.ViewType;
import com.vip001.monitor.viewholder.DisplayDropFramesViewHolderFactory;

import java.util.ArrayList;

/**
 * Created by xxd on 2018/11/25.
 */

public class DisplayDropFramesActivity extends Activity {
    private TextView mTitle;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView mRecycelerView;
    private TextView mDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.monitor_layout_drop_frames);
        mTitle = this.findViewById(R.id.title);
        mRecycelerView = this.findViewById(R.id.reyclerview);
        mDelete = this.findViewById(R.id.action);
        mTitle.setText(String.format("Drop Frames in %s", getPackageName()));

        mRecycelerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter();
        mAdapter.setViewHolderFactory(new DisplayDropFramesViewHolderFactory());
        mRecycelerView.setAdapter(mAdapter);
        testData();
    }

    private void testData() {
        ArrayList<BaseRecyclerViewBean> beans = new ArrayList<>();
        BaseRecyclerViewBean bean = new BaseRecyclerViewBean();
        bean.type = ViewType.TYPE_DISPLAY_DROP_FRAMES;
        ReyclerViewDropFramesDisplayBean one = new ReyclerViewDropFramesDisplayBean();
        one.content = "testfangfdanelajfldajrejojjljdlajfdlatestfangfdanelajfldajrejojjljdlajfdla";
        one.date = "2018-01-02 12:30.339";
        bean.data = one;
        beans.add(bean);

        mAdapter.setData(beans);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public static void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, DisplayDropFramesActivity.class);
        context.startActivity(intent);

    }
}

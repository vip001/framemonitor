package com.vip001.monitor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.vip001.monitor.R;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.bean.BaseRecyclerViewBean;
import com.vip001.monitor.bean.RecyclerViewStackBean;
import com.vip001.monitor.common.ViewType;
import com.vip001.monitor.viewholder.DropFramesDetailViewHolderFactory;
import com.vip001.monitor.widget.DisplayLeakConnectorView;

import java.util.ArrayList;

/**
 * Created by xxd on 2018/11/24.
 */

public class DropFramesDetailActivity extends Activity {
    private RecyclerView mReyclerView;
    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView mDelete;
    private TextView mTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.monitor_layout_drop_frames_detail);
        mReyclerView = this.findViewById(R.id.reyclerview);
        mTitle = this.findViewById(R.id.title);
        mAdapter = new RecyclerViewAdapter();
        mAdapter.setViewHolderFactory(new DropFramesDetailViewHolderFactory());
        mLayoutManager = new LinearLayoutManager(this);
        mReyclerView.setLayoutManager(mLayoutManager);
        mReyclerView.setAdapter(mAdapter);
        mDelete = this.findViewById(R.id.action);
        testData();
    }

    public void testData() {
        ArrayList<BaseRecyclerViewBean> beans = new ArrayList<>();
        BaseRecyclerViewBean bean = new BaseRecyclerViewBean();
        bean.type = ViewType.TYPE_TOP_ROW;
        bean.data = getPackageName();
        beans.add(bean);

        BaseRecyclerViewBean bean1 = new BaseRecyclerViewBean();
        bean1.type = ViewType.TYPE_STACk;
        RecyclerViewStackBean two = new RecyclerViewStackBean();
        two.title = Html.fromHtml("<font color='"
                + hexStringColor(getResources(), R.color.monitor_help)
                + "'>"
                + "<b>" + getResources().getString(R.string.monitor_help_title) + "</b>"
                + "</font>");

        SpannableStringBuilder detailText =
                (SpannableStringBuilder) Html.fromHtml(
                        getResources().getString(R.string.monitor_help_detail));
        two.details = detailText;
        two.type = DisplayLeakConnectorView.Type.HELP;
        bean1.data = two;
        beans.add(bean1);

        BaseRecyclerViewBean bean2 = new BaseRecyclerViewBean();
        bean2.type = ViewType.TYPE_STACk;
        RecyclerViewStackBean three = new RecyclerViewStackBean();
        three.title = "testData";
        three.details = "testtest";
        three.type = DisplayLeakConnectorView.Type.START_LAST_REACHABLE;
        bean2.data = three;
        beans.add(bean2);

        BaseRecyclerViewBean bean3 = new BaseRecyclerViewBean();
        bean3.type = ViewType.TYPE_STACk;
        RecyclerViewStackBean four = new RecyclerViewStackBean();
        four.title = "testData";
        four.details = "testtest";
        four.type = DisplayLeakConnectorView.Type.END_FIRST_UNREACHABLE;
        bean3.data = four;
        beans.add(bean3);

        mAdapter.setData(beans);
    }

    private static String hexStringColor(Resources resources, int colorResId) {
        return String.format("#%06X", (0xFFFFFF & resources.getColor(colorResId)));
    }

    public static void start(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, DropFramesDetailActivity.class);
        context.startActivity(intent);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}

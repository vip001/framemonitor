package com.vip001.monitor.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.vip001.framemonitor.IConfig;
import com.vip001.monitor.R;
import com.vip001.monitor.adapter.RecyclerViewAdapter;
import com.vip001.monitor.bean.BaseRecyclerViewBean;
import com.vip001.monitor.bean.InstructionBean;
import com.vip001.monitor.bean.LoadDataBean;
import com.vip001.monitor.common.FileManager;
import com.vip001.monitor.common.ViewType;
import com.vip001.monitor.services.stack.StackInfo;
import com.vip001.monitor.utils.BussinessUtils;
import com.vip001.monitor.utils.DataLoadHelper;
import com.vip001.monitor.viewholder.DropFramesDetailViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxd on 2018/11/24.
 */

public class DropFramesDetailActivity extends Activity {
    private RecyclerView mReyclerView;
    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView mDelete;
    private TextView mTitle;
    private String mFileName;
    private static final String KEY_FILE_NAME = "KEY_FILE_NAME";

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
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.getInstance().deleteLog(mFileName);
                DataLoadHelper.getInstance().clearData(mFileName);
                Intent intent = getIntent();
                intent.putExtra(BussinessUtils.KEY_DELETE_FILE, mFileName);
                setResult(1002, intent);
                finish();
            }
        });
        preloadData();
        resolveIntent();
    }


    private void resolveIntent() {
        String name = getIntent().getStringExtra(KEY_FILE_NAME);
        LoadDataBean bean = DataLoadHelper.getInstance().getData(name);
        if (bean == null) {
            return;
        }
        mTitle.setText(new StringBuilder(BussinessUtils.getDropFramesTarget(bean.dropFramesBean, true)).append(" drop ").append(bean.dropFramesBean.frameCostTime / IConfig.FRAME_INTERVALS).append(" frames"));
        mAdapter.addData(transformData(bean.listStackInfo));
        mFileName = name;
    }

    private List<BaseRecyclerViewBean> transformData(List<StackInfo> info) {
        ArrayList<BaseRecyclerViewBean> result = new ArrayList<>();
        BaseRecyclerViewBean bean = null;
        for (int i = 0, len = info.size(); i < len; i++) {
            bean = new BaseRecyclerViewBean();
            bean.type = ViewType.TYPE_STACk;
            bean.data = info.get(i);
            result.add(bean);
        }
        return result;
    }

    public void preloadData() {
        ArrayList<BaseRecyclerViewBean> beans = new ArrayList<>();
        BaseRecyclerViewBean bean = new BaseRecyclerViewBean();
        bean.type = ViewType.TYPE_TOP_ROW;
        bean.data = getPackageName();
        beans.add(bean);

        BaseRecyclerViewBean bean1 = new BaseRecyclerViewBean();
        bean1.type = ViewType.TYPE_INTRODUCE;
        InstructionBean instruction = new InstructionBean();
        instruction.title = Html.fromHtml("<font color='"
                + hexStringColor(getResources(), R.color.monitor_help)
                + "'>"
                + "<b>" + getResources().getString(R.string.monitor_help_title) + "</b>"
                + "</font>");

        SpannableStringBuilder detailText =
                (SpannableStringBuilder) Html.fromHtml(
                        getResources().getString(R.string.monitor_help_detail));
        instruction.details = detailText;
        bean1.data = instruction;
        beans.add(bean1);

       /* BaseRecyclerViewBean bean2 = new BaseRecyclerViewBean();
        bean2.type = ViewType.TYPE_STACk;
        InstructionBean three = new InstructionBean();
        three.title = "testData";
        three.details = "testtest";
        three.type = DisplayLeakConnectorView.Type.START_LAST_REACHABLE;
        bean2.data = three;
        beans.add(bean2);

        BaseRecyclerViewBean bean3 = new BaseRecyclerViewBean();
        bean3.type = ViewType.TYPE_STACk;
        InstructionBean four = new InstructionBean();
        four.title = "testData";
        four.details = "testtest";
        four.type = DisplayLeakConnectorView.Type.END_FIRST_UNREACHABLE;
        bean3.data = four;
        beans.add(bean3);*/
        mAdapter.setData(beans);
    }

    private static String hexStringColor(Resources resources, int colorResId) {
        return String.format("#%06X", (0xFFFFFF & resources.getColor(colorResId)));
    }

    public static void start(Activity context, String fileNames) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, DropFramesDetailActivity.class);
        intent.putExtra(KEY_FILE_NAME, fileNames);
        context.startActivityForResult(intent, 1001);

    }

    @Override
    public void onBackPressed() {
        setResult(1002, getIntent());
        super.onBackPressed();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}

package com.vip001.framemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vip001.framemonitor.exam.DropFrameService;
import com.vip001.framemonitor.exam.ListPageSlideDropFrameActivity;
import com.vip001.framemonitor.exam.ReceiverDropFrameActivity;
import com.vip001.framemonitor.exam.ServiceDropFrameActivity;
import com.vip001.framemonitor.exam.TouchDropFrameActivity;
import com.vip001.framemonitor.exam.FlowStatisticActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListPageSlideDropFrameActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.btn_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReceiverDropFrameActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.btn_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceDropFrameActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.btn_touch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TouchDropFrameActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.btn_flow_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlowStatisticActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }
}

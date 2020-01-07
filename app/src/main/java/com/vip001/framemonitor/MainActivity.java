package com.vip001.framemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vip001.framemonitor.exam.Example1Activity;
import com.vip001.framemonitor.exam.Example2Activity;
import com.vip001.framemonitor.exam.Example3Activity;
import com.vip001.framemonitor.exam.Example4Activity;
import com.vip001.framemonitor.exam.Example5Activity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.exam1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Example1Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.exam2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Example2Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.exam3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Example3Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.exam4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Example4Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        this.findViewById(R.id.exam5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Example5Activity.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }
}

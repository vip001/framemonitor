package com.vip001.framemonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by xxd on 2018/8/9
 */
public class ExampleApdater extends BaseAdapter {

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return new Object();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (JANKSwitch.DEBUG_JANK) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.text)).setText("Welcome to Demo " + position);

            return convertView;
        }

    }
}

package com.vip001.monitor.services.flow;

import android.text.TextUtils;

import com.vip001.monitor.common.FileManager;
import com.vip001.monitor.utils.FormatUtils;
import com.vip001.monitor.utils.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by xxd on 2018/10/4.
 */

public class FlowWriteTask implements Runnable {
    private String mText;

    public FlowWriteTask setText(String text) {
        this.mText = text;
        return this;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        File parentFile = FileManager.getInstance().checkFlowDir();
        if (parentFile == null) {
            return;
        }
        String filename = FormatUtils.formatDate(new Date());
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(new File(parentFile, filename)));
            os.write(mText.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
            mText = null;
        }
    }
}

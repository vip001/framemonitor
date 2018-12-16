package com.vip001.monitor.services.stack;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Printer;

import com.vip001.monitor.bean.DropFramesBean;
import com.vip001.monitor.utils.BussinessUtils;
import com.vip001.monitor.utils.FormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by xxd on 2018/8/14
 */
public class LogThread extends HandlerThread implements Handler.Callback {
    private Handler mHanlder;
    private File mParentFile;
    private long mLastWriteTime;
    private MainStackCollectTask mMainStackCollectTask;
    private boolean isWrite;
    private static final String TAG = "FrameMonitor";
    private static final long INTERVAL_WRITE_LOG = 2 * 1000;
    private static final int MSG_WRITE_LOG = 1;
    private Callback mCallback;

    public LogThread(File file, Callback callback) {
        super("LogThread");
        mParentFile = file;
        mCallback = callback;
    }

    @Override
    public synchronized void start() {
        super.start();
        if (mHanlder == null) {
            mHanlder = new Handler(getLooper(), this);
        }
        if (mMainStackCollectTask == null) {
            mMainStackCollectTask = new MainStackCollectTask();
        }
        mMainStackCollectTask.start(mHanlder);

    }

    @Override
    public boolean quit() {
        if (mMainStackCollectTask != null) {
            mMainStackCollectTask.stop();
        }
        mCallback = null;
        return super.quit();
    }

    public void writeLog() {

        mHanlder.sendEmptyMessage(MSG_WRITE_LOG);
    }

    public boolean canWriteLog() {
        return !isWrite && System.currentTimeMillis() - mLastWriteTime > INTERVAL_WRITE_LOG;
    }

    private boolean canDoLog() {
        if (mMainStackCollectTask.getStacks().size() < 3) {
            return false;
        }
        StackInfo info = mMainStackCollectTask.getStacks().get(mMainStackCollectTask.getStacks().size() - 1);
        long result1 = DropFramesBean.getInstance().happensTime - info.time;
        long result2 = DropFramesBean.getInstance().happensTime - mMainStackCollectTask.getStacks().get(mMainStackCollectTask.getStacks().size() - 2).time;
        long result3 = DropFramesBean.getInstance().happensTime - mMainStackCollectTask.getStacks().get(mMainStackCollectTask.getStacks().size() - 3).time;
        return result1 > 0 && result1 <= 100 && result2 < 1000 && result3 < 1000;
    }

    private void doLog() {
        isWrite = true;
        try {
            if (!canDoLog()) {
                Log.i(TAG, "abort write stacks!invalid dropframes!");
                return;
            }
            mLastWriteTime = System.currentTimeMillis();
            File file = new File(mParentFile, FormatUtils.formatDate(new Date()));
            if (!file.exists()) {
                file.createNewFile();
            }

            final PrintWriter printer = new PrintWriter(new FileOutputStream(file), true);
            printer.println(BussinessUtils.META_BASE_MESSAGE);
            printer.println(DropFramesBean.getInstance().toMinString());
            printer.println(DropFramesBean.getInstance().toExplicitString());
            printer.println();
            printer.println(BussinessUtils.META_LOG_MAIN_STACK);

            LinkedList<StackInfo> stackInfos = mMainStackCollectTask.getStacks();
            StackInfo info = null;
            for (int i = stackInfos.size() - 1, count = 1; i > 0; i--) {
                if (count > 3) {
                    break;
                }
                printer.println();
                info = stackInfos.get(i);
                printer.println(BussinessUtils.META_STACK_TIME_START);
                printer.println(info.time);
                printer.println(BussinessUtils.META_STACK_TIME_END);
                printer.println(BussinessUtils.META_STACK_START);
                printer.println(info.msg);
                printer.println(BussinessUtils.META_STACK_END);
                count++;
            }

            printer.println();
            printer.println(BussinessUtils.META_LOG_MESSAGE);
            printer.println();

            Looper.getMainLooper().dump(new Printer() {
                @Override
                public void println(String x) {
                    printer.println(x);
                }
            }, TAG);

            printer.println();
            printer.println(BussinessUtils.META_THREAD_STACK);

            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                printer.println();
                printer.println(entry.getKey().toString());
                printer.println();
                for (StackTraceElement element : entry.getValue()) {
                    printer.println(element.toString());
                }
            }

            printer.println();

            printer.flush();
            printer.close();
            if (mCallback != null) {
                mCallback.writeSuccess(file.getAbsolutePath());
            }
            Log.i(TAG, "The application may be doing too much work on its main thread.The log file is written to " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isWrite = false;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MSG_WRITE_LOG) {
            mMainStackCollectTask.stop();
            doLog();
            mMainStackCollectTask.clearStack();
            mMainStackCollectTask.start(mHanlder);
            return true;
        }
        return false;
    }

    public interface Callback {
        void writeSuccess(String path);
    }
}

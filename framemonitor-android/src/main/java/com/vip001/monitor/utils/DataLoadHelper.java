package com.vip001.monitor.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.vip001.monitor.bean.DropFramesBean;
import com.vip001.monitor.bean.LoadDataBean;
import com.vip001.monitor.common.FileManager;
import com.vip001.monitor.services.stack.StackInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by xxd on 2018/12/2.
 */

public class DataLoadHelper {
    private static final DataLoadHelper mHelper = new DataLoadHelper();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private DataLoadHelper() {
    }

    public static DataLoadHelper getInstance() {
        return mHelper;
    }

    private HashMap<String, LoadDataBean> mDatas = new HashMap<>();

    public List<LoadDataBean> getData() {
        return new ArrayList<>(mDatas.values());
    }

    public LoadDataBean getData(String key) {
        return mDatas.get(key);
    }

    public void clearData(String key) {
        mDatas.remove(key);
    }

    public void clearData() {
        mDatas.clear();
    }

    public void loadData(final Callback callback) {

        if (callback != null) {
            callback.onBefore();
        }
        ThreadUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                final List<LoadDataBean> results = loadStack();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(results);
                        }
                    }
                });
            }
        });

    }

    private List<LoadDataBean> loadStack() {
        File file = FileManager.getInstance().checkLogDir();

        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return mDatas.get(name) == null;
            }
        });
        BufferedReader br = null;
        List<LoadDataBean> loadDataBeans = new ArrayList<>();
        for (int i = 0, len = files.length; i < len; i++) {
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(files[i])));
                String text = null;
                boolean isBaseMessage = false;
                boolean isTimeStart = false;
                boolean isStackStart = false;
                LoadDataBean bean = new LoadDataBean();
                StackInfo stackInfo = null;
                while ((text = br.readLine()) != null) {
                    if (TextUtils.isEmpty(text)) {
                        continue;
                    } else if (text.equals(BussinessUtils.META_BASE_MESSAGE)) {
                        isBaseMessage = true;
                        continue;
                    } else if (text.equals(BussinessUtils.META_LOG_MAIN_STACK)) {
                        bean.listStackInfo = new ArrayList<>();
                    } else if (isBaseMessage) {
                        bean.dropFramesBean = DropFramesBean.parse(text);
                        isBaseMessage = false;
                    } else if (text.equals(BussinessUtils.META_STACK_TIME_START)) {
                        isTimeStart = true;
                        stackInfo = new StackInfo();
                    } else if (text.equals(BussinessUtils.META_STACK_START)) {
                        stackInfo.msg = new StringBuilder();
                        isStackStart = true;
                    } else if (text.equals(BussinessUtils.META_STACK_END)) {
                        isStackStart = false;
                        bean.listStackInfo.add(stackInfo);
                    } else if (isTimeStart) {
                        try {
                            stackInfo.time = Long.parseLong(text);
                            stackInfo.timeString = FormatUtils.formatTime(stackInfo.time);
                            stackInfo.intervalFromHappensTime = bean.dropFramesBean.happensTime - stackInfo.time;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        isTimeStart = false;
                    } else if (isStackStart) {
                        stackInfo.msg.append(text).append("\r\n");
                    } else if (text.equals(BussinessUtils.META_LOG_MESSAGE)) {
                        break;
                    }
                }
                bean.fileName = files[i].getName();
                mDatas.put(files[i].getName(), bean);
                loadDataBeans.add(bean);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(br);
            }
        }
        return loadDataBeans;
    }

    public interface Callback {
        void onBefore();

        void onFinish(List<LoadDataBean> beans);
    }
}

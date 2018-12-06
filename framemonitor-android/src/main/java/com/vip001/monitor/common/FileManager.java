package com.vip001.monitor.common;

import android.app.Application;

import java.io.File;

/**
 * Created by xxd on 2018/10/4.
 */

public class FileManager {
    private static final String TAG = "FileManager";
    private static final String PARENT = "framemonitor";
    private static final String DIR_LOG = "log";
    private static final String DIR_FLOW = "flow";
    private static final int TYPE_DIR_INTERNAL_LOG = 0;
    private static final int TYPE_DIR_INTERAL_FLOW = 1;
    private static final int TYPE_DIR_ABORT = -1;
    private Application mApp;
    private static FileManager sInstance = new FileManager();

    public static FileManager getInstance() {
        return sInstance;
    }

    public void init(Application app) {
        mApp = app;
    }

    public File checkLogDir() {
        return checkDir(new File(getExternalLogDir()), TYPE_DIR_INTERNAL_LOG);

    }

    public File checkFlowDir() {
        return checkDir(new File(getExternalFlowDir()), TYPE_DIR_INTERAL_FLOW);
    }

    private String getExternalLogDir() {
        return new StringBuilder().append(mApp.getExternalCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_LOG).toString();
    }

    private String getInternalLogDir() {
        return new StringBuilder().append(mApp.getCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_LOG).toString();
    }

    private String getExternalFlowDir() {
        return new StringBuilder().append(mApp.getExternalCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_FLOW).toString();
    }

    private String getInternalFlowDir() {
        return new StringBuilder().append(mApp.getCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_FLOW).toString();
    }

    private File checkDir(File file, int alter) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            } else {
                String dir = getDir(alter);
                if (dir == null) {
                    return null;
                }
                return checkDir(new File(getDir(alter)), TYPE_DIR_ABORT);
            }
        } else {
            return file;

        }
    }

    private String getDir(int type) {
        String dir = null;
        switch (type) {
            case TYPE_DIR_INTERNAL_LOG:
                dir = getInternalLogDir();
                break;
            case TYPE_DIR_INTERAL_FLOW:
                dir = getInternalFlowDir();
                break;
            case TYPE_DIR_ABORT:
                dir = null;
            default:
                dir = getInternalFlowDir();
                break;
        }
        return dir;
    }

    public void deleteAllLogFiles() {
        File logFile = checkLogDir();
        File[] files = logFile.listFiles();
        for (int i = 0, len = files.length; i < len; i++) {
            if (files[i].isFile()) {
                files[i].delete();
            }
        }
    }

    public void deleteLog(String fileName) {
        File parent = checkLogDir();
        File logFile = new File(parent, fileName);
        if (logFile.isFile() && logFile.exists()) {
            logFile.delete();
        }
    }
}

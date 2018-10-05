package com.vip1002.framemonitor;

import android.app.Activity;
import android.app.Application;

/**
 * Created by xxd on 2018/8/21
 */
public interface IFrameMonitorManager {
    boolean isStarted();

    /**
     * 记录需要显示小圆球悬浮窗的Activity
     *
     * @param activity
     * @return
     */
    IFrameMonitorManager show(Activity activity);

    /**
     * sdk初始化调用的方法
     *
     * @param application
     * @return
     */
    IFrameMonitorManager init(Application application);

    /**
     * 定义卡顿的标准
     *
     * @param config
     * @return
     */
    IFrameMonitorManager setConfig(IConfig config);

    /**
     * 获取定义卡顿的配置
     *
     * @return
     */
    IConfig getConfig();

    /**
     * 小圆球悬浮窗是否展示的开关
     *
     * @param enableShow
     * @return
     */
    IFrameMonitorManager setEnableShow(boolean enableShow);

    /**
     * 启动卡顿检测
     *
     * @return
     */
    IFrameMonitorManager start();

    /**
     * 停止卡断检测
     *
     * @return
     */
    IFrameMonitorManager stop();

    /**
     * 设置App退到后台是否继续检测卡顿，默认否
     *
     * @param enableBackgroundMonitor
     * @return
     */
    IFrameMonitorManager setEnableBackgroundMonitor(boolean enableBackgroundMonitor);

    /**
     * 开始流量统计
     * @return true为成功，false为失败，失败原因可能是IPC通讯未建立
     */
    boolean startFlowCal();

    /**
     * 停止流量统计
     * @return true为成功，false为失败，失败原因可能是IPC通讯未建立
     */
    boolean stopFlowCal();
}

package com.vip001.monitor.view.snackbar;

/**
 * Created by xxd on 2018/7/23
 * 默认值1已被TSnackbar show()使用，请不要定义
 */
public class SnackBarManagerType {
    private static int type = 2;

    public static int generateType() {
        return type++;
    }
}

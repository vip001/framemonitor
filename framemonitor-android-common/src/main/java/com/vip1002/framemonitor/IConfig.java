package com.vip1002.framemonitor;

/**
 * Created by xxd on 2018/8/21
 */
public interface IConfig {
    /**
     * Unit nanotimes
     */
    long FRAME_INTERVALS = (long) (1000000000 / 60);
    int RESULT_RED = 1;
    int RESULT_GREEN = -1;
    int RESULT_YELLOW = 0;

    int sortTime(long time);
}

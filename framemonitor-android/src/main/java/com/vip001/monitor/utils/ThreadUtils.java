package com.vip001.monitor.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xxd on 2018/10/4.
 */

public class ThreadUtils implements Executor {
    private static ThreadUtils sInstance = new ThreadUtils();
    private final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private final int KEEP_ALIVE_SECONDS = 30;

    private final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    private final Executor mThreadPool;

    private ThreadUtils() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        mThreadPool = threadPoolExecutor;
    }

    @Override
    public void execute(Runnable command) {
        mThreadPool.execute(command);
    }

    public static ThreadUtils getInstance() {
        return sInstance;
    }
}

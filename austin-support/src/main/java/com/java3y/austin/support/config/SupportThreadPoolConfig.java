package com.java3y.austin.support.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.java3y.austin.common.constant.ThreadPoolConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 3y
 * support 线程池配置类
 */
public class SupportThreadPoolConfig {

    private SupportThreadPoolConfig() {
    }

    /**
     * 业务：实现pending队列的单线程池
     * 配置：核心线程可以被回收，当线程池无被引用且无核心线程数，应当被回收
     * 动态线程池且被Spring管理：false
     */
    public static ExecutorService getPendingSingleThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.SINGLE_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.SINGLE_MAX_POOL_SIZE)
                .setWorkQueue(new LinkedBlockingQueue(ThreadPoolConstant.BIG_QUEUE_SIZE))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }

}

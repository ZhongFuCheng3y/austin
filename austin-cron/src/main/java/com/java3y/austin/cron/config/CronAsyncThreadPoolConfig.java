package com.java3y.austin.cron.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.dtp.common.em.QueueTypeEnum;
import com.dtp.common.em.RejectedTypeEnum;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import com.java3y.austin.common.constant.ThreadPoolConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 3y
 * 动态线程池配置。实际的具体配置以apollo的为准！实际的具体配置以apollo的为准！实际的具体配置以apollo的为准
 */
public class CronAsyncThreadPoolConfig {

    /**
     * 接收到xxl-job请求的线程池名
     */
    public static final String EXECUTE_XXL_THREAD_POOL_NAME = "execute-xxl-thread-pool";

    private CronAsyncThreadPoolConfig() {
    }

    /**
     * 业务：消费pending队列实际的线程池
     * 配置：核心线程可以被回收，当线程池无被引用且无核心线程数，应当被回收
     * 动态线程池且被Spring管理：false
     *
     * @return
     */
    public static ExecutorService getConsumePendingThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE)
                .setWorkQueue(new LinkedBlockingQueue(ThreadPoolConstant.BIG_QUEUE_SIZE))
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }


    /**
     * 业务：接收到xxl-job请求的线程池
     * 配置：不丢弃消息，核心线程数不会随着keepAliveTime而减少(不会被回收)
     * 动态线程池且被Spring管理：true
     *
     * @return
     */
    public static DtpExecutor getXxlCronExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(EXECUTE_XXL_THREAD_POOL_NAME)
                .corePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .maximumPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE)
                .keepAliveTime(ThreadPoolConstant.COMMON_KEEP_LIVE_TIME)
                .timeUnit(TimeUnit.SECONDS)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .allowCoreThreadTimeOut(false)
                .workQueue(QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE.getName(), ThreadPoolConstant.COMMON_QUEUE_SIZE, false)
                .buildDynamic();
    }

}

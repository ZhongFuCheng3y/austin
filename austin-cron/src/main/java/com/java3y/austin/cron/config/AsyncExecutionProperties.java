package com.java3y.austin.cron.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: austin
 * @description: spring 自定义线程池配置类
 * @author: WhyWhatHow
 * @create: 2022-02-27 09:41
 * @see TaskExecutionProperties
 **/
@Data
@ConfigurationProperties("austin.async.task")
public class AsyncExecutionProperties {
    /**
     * 核心线程数,默认数量当前cpu核心线程数
     */
    int coreSize;
    /**
     * 最大线程数 ,默认coreSize*2
     */
    int maxSize;
    /**
     * 线程名前缀 eg: "austinAsyncExecutor-"
     */
    private String threadNamePrefix = "austinAsyncExecutor-";

    /**
     * queue capacity
     */
    private int queueCapacity = 1000;

    /**
     * 线程最大存活时间,单位s
     */
    private int keepAlive = 60;

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeout = false;

    /**
     * 拒绝策略 ,默认callRun
     */
    private RejectedEnum rejectedHandler = RejectedEnum.CALLRUNSPOLICY;


    /**
     * 是否在关机时等待任务完成 ,默认为true
     */
    private boolean waitForTasksToCompleteOnShutDown = true;

    /**
     * 阻止关机的最大秒数 ,默认10s
     */
    private int awaitTerminationSeconds = 10;

    /**
     * 初始化 核心线程数, 最大线程数, 以用户配置为主
     */
    @PostConstruct
    void init() {
        if (coreSize <= 0) {
            this.coreSize = Runtime.getRuntime().availableProcessors();
        }
        if (maxSize <= 0) {
            this.maxSize = coreSize << 1;
        }
    }

    /**
     * 拒绝策略枚举
     */
    public enum RejectedEnum {
        /**
         * 直接抛出异常
         */
        ABORTPOLICY(new ThreadPoolExecutor.AbortPolicy()),
        /**
         * 交给当前run_thread 运行
         */
        CALLRUNSPOLICY(new ThreadPoolExecutor.CallerRunsPolicy()),
        /***
         * 直接丢掉
         */
        DISCARDPOLICY(new ThreadPoolExecutor.DiscardPolicy()),
        /**
         * 丢掉队列中排队时间最久的任务
         */
        DISCARDOLDESTPOLICY(new ThreadPoolExecutor.DiscardOldestPolicy());
        /**
         * 线程池默认拒绝策略
         */
        private RejectedExecutionHandler handler;

        RejectedEnum(RejectedExecutionHandler handler) {
            this.handler = handler;
        }


        public RejectedExecutionHandler getHandler() {
            return handler;
        }

        public void setHandler(RejectedExecutionHandler handler) {
            this.handler = handler;
        }
    }
}

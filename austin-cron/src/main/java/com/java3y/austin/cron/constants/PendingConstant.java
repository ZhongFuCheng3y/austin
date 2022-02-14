package com.java3y.austin.cron.constants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 3y
 * @date 2022/2/13
 * 延迟缓冲 pending 常量信息
 */
public class PendingConstant {

    /**
     * 阻塞队列大小
     */
    public static final Integer QUEUE_SIZE = 100;

    /**
     * 触发执行的数量阈值
     */
    public static final Integer NUM_THRESHOLD = 100;

    /**
     * batch 触发执行的时间阈值，单位毫秒【必填】
     */
    public static final Long TIME_THRESHOLD = 1000L;

    /**
     * 真正消费线程池配置的信息
     */
    public static final Integer CORE_POOL_SIZE = 2;
    public static final Integer MAX_POOL_SIZE = 2;
    public static final BlockingQueue BLOCKING_QUEUE = new LinkedBlockingQueue<>(5);

}

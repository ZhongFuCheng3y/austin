package com.java3y.austin.cron.constants;

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

    private PendingConstant() {
    }


}

package com.java3y.austin.support.pending;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 延迟消费的线程 实现
 * 积攒一定的数量 或 时间 才消费，达到批量消费的效果
 *
 * @author 3y
 */
@Data
@Accessors(chain = true)
@Slf4j
public class BatchPendingThread<T> extends Thread {

    private PendingParam<T> pendingParam;

    /**
     * 批量装载任务
     */
    private List<T> tasks = new ArrayList<>();

    /**
     * 当前装载任务的大小
     */
    private Integer total = 0;

    /**
     * 上次执行的时间
     */
    private Long lastHandleTime = System.currentTimeMillis();


    @Override
    public void run() {
        while (true) {
            try {
                T obj = pendingParam.getQueue().poll(pendingParam.getTimeThreshold(), TimeUnit.MILLISECONDS);
                if (null != obj) {
                    tasks.add(obj);
                }

                // 处理条件：1. 数量超限 2. 时间超限
                if ((tasks.size() >= pendingParam.getNumThreshold())
                        || (System.currentTimeMillis() - lastHandleTime >= pendingParam.getTimeThreshold())) {
                    List<T> taskRef = tasks;
                    tasks = Lists.newArrayList();
                    lastHandleTime = System.currentTimeMillis();
                    pendingParam.getPending().handle(taskRef);
                }
            } catch (Exception e) {
                log.error("BatchPendingThread#run failed:{}", Throwables.getStackTraceAsString(e));
            }
        }
    }
}

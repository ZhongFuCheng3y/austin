package com.java3y.austin.support.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 优雅关闭线程池
 *
 * @author 3y
 */
@Component
@Slf4j
public class ThreadPoolExecutorShutdownDefinition implements ApplicationListener<ContextClosedEvent> {

    /**
     * 线程中的任务在接收到应用关闭信号量后最多等待多久就强制终止，其实就是给剩余任务预留的时间， 到时间后线程池必须销毁
     */
    private static final long AWAIT_TERMINATION = 20;
    /**
     * awaitTermination的单位
     */
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private final List<ExecutorService> POOLS = Collections.synchronizedList(new ArrayList<>(12));

    public void registryExecutor(ExecutorService executor) {
        POOLS.add(executor);
    }

    /**
     * 参考{@link org.springframework.scheduling.concurrent.ExecutorConfigurationSupport#shutdown()}
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("容器关闭前处理线程池优雅关闭开始, 当前要处理的线程池数量为: {} >>>>>>>>>>>>>>>>", POOLS.size());
        if (CollectionUtils.isEmpty(POOLS)) {
            return;
        }
        for (ExecutorService pool : POOLS) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(AWAIT_TERMINATION, TIME_UNIT)) {
                    log.warn("Timed out while waiting for executor [{}] to terminate", pool);
                }
            } catch (InterruptedException ex) {
                log.warn("Timed out while waiting for executor [{}] to terminate", pool);
                Thread.currentThread().interrupt();
            }
        }
    }
}

package com.java3y.austin.config;

import cn.hutool.core.thread.ExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * 线程池配置信息
 * @author 3y
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("smsThreadPool")
    public static ExecutorService getSmsThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = ExecutorBuilder.create()
                .setCorePoolSize(4)
                .setMaxPoolSize(4)
                .setKeepAliveTime(60)
                .setWorkQueue(new LinkedBlockingQueue<>(1000))
                .setHandler((r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                    }
                })
                .build();

        return threadPoolExecutor;
    }

    @Bean("emailThreadPoll")
    public static ExecutorService getEmailThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = ExecutorBuilder.create()
                .setCorePoolSize(2)
                .setMaxPoolSize(2)
                .setKeepAliveTime(60)
                .setWorkQueue(new LinkedBlockingQueue<>(1000))
                .setHandler((r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                    }
                })
                .build();
        return threadPoolExecutor;
    }
}

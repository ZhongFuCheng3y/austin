package com.java3y.austin.support.config;

import com.dtp.common.em.QueueTypeEnum;
import com.dtp.core.support.ThreadPoolCreator;
import com.dtp.core.thread.DtpExecutor;
import com.dtp.core.thread.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Redick01
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    public DtpExecutor dtpExecutor() {

        return ThreadPoolCreator.createDynamicFast("dynamic-tp-test-1");
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("dynamic-tp-test-2")
                .corePoolSize(10)
                .maximumPoolSize(15)
                .keepAliveTime(15000)
                .timeUnit(TimeUnit.MILLISECONDS)
                .workQueue(QueueTypeEnum.SYNCHRONOUS_QUEUE.getName(), null, false)
                .buildDynamic();
    }
}

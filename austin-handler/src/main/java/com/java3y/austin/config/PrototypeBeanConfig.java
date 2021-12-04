package com.java3y.austin.config;

import com.java3y.austin.pending.Task;
import com.java3y.austin.receiver.Receiver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Handler模块的配置信息
 *
 * @author 3y
 */
@Configuration
public class PrototypeBeanConfig {

    /**
     * 定义多例的Receiver
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Receiver receiver() {
        return new Receiver();
    }

    /**
     * 定义多例的Task
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Task task() {
        return new Task();
    }

}

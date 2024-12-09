package com.java3y.austin.support.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig implements ApplicationContextAware {

    /**
     * 添加ReturnCallBack
     * 投递到队列失败的消息，可以在这里查看原因和做失败处理策略
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnsCallback(returnedMessage ->
                log.error("消息投递到队列失败， 状态码：{}，失败原因：{}，交换机：{}，routingKey：{}，消息：{}",
                returnedMessage.getReplyCode(), returnedMessage.getReplyText(), returnedMessage.getExchange(),
                returnedMessage.getRoutingKey(), returnedMessage.getMessage()));
    }
}

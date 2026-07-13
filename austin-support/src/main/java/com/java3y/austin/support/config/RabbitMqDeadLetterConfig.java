package com.java3y.austin.support.config;

import com.java3y.austin.support.constans.MessageQueuePipeline;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 死信队列配置
 * 重试耗尽后消息进入 DLQ，避免消息丢失，便于人工排查与补偿
 *
 * @author Rangsh
 */
@Configuration
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitMqDeadLetterConfig {

    @Value("${austin.rabbitmq.dlx.exchange}")
    private String dlxExchangeName;

    @Value("${austin.rabbitmq.queues.send.dead}")
    private String sendDeadQueueName;

    @Value("${austin.rabbitmq.queues.recall.dead}")
    private String recallDeadQueueName;

    @Value("${austin.rabbitmq.routing.send.dead}")
    private String sendDeadRoutingKey;

    @Value("${austin.rabbitmq.routing.recall.dead}")
    private String recallDeadRoutingKey;

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlxExchangeName, true, false);
    }

    @Bean
    public Queue sendDeadLetterQueue() {
        return new Queue(sendDeadQueueName, true);
    }

    @Bean
    public Queue recallDeadLetterQueue() {
        return new Queue(recallDeadQueueName, true);
    }

    @Bean
    public Binding sendDeadLetterBinding() {
        return BindingBuilder.bind(sendDeadLetterQueue())
                .to(deadLetterExchange())
                .with(sendDeadRoutingKey);
    }

    @Bean
    public Binding recallDeadLetterBinding() {
        return BindingBuilder.bind(recallDeadLetterQueue())
                .to(deadLetterExchange())
                .with(recallDeadRoutingKey);
    }
}

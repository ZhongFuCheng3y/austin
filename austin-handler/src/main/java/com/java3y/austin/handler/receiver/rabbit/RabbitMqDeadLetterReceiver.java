package com.java3y.austin.handler.receiver.rabbit;

import com.java3y.austin.support.constans.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列消费者
 * 消费重试耗尽后进入 DLQ 的消息，记录日志便于排查与人工补偿
 *
 * @author Rangsh
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitMqDeadLetterReceiver {

    @RabbitListener(queues = "${austin.rabbitmq.queues.send.dead}")
    public void onSendDeadLetter(Message message) {
        log.error("[DLQ][send] headers={}, body={}",
                message.getMessageProperties().getHeaders(),
                new String(message.getBody(), StandardCharsets.UTF_8));
        // TODO: 后续可扩展落库 / 告警 / 人工补偿入口
    }

    @RabbitListener(queues = "${austin.rabbitmq.queues.recall.dead}")
    public void onRecallDeadLetter(Message message) {
        log.error("[DLQ][recall] headers={}, body={}",
                message.getMessageProperties().getHeaders(),
                new String(message.getBody(), StandardCharsets.UTF_8));
        // TODO: 后续可扩展落库 / 告警 / 人工补偿入口
    }
}

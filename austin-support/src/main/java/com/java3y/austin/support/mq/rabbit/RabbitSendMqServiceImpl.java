package com.java3y.austin.support.mq.rabbit;

import com.java3y.austin.support.constans.MessageQueuePipeline;
import com.java3y.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;



/**
 * @author xzcawl
 * @Date 2022/7/15 17:29
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.RABBIT_MQ)
public class RabbitSendMqServiceImpl implements SendMqService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${austin.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${austin.business.topic.name}")
    private String sendMessageTopic;

    @Value("${austin.business.recall.topic.name}")
    private String austinRecall;

    @Value("${austin.rabbitmq.routing.send}")
    private String sendRoutingKey;

    @Value("${austin.rabbitmq.routing.recall}")
    private String recallRoutingKey;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (topic.equals(sendMessageTopic)){
            rabbitTemplate.convertAndSend(exchangeName, sendRoutingKey, jsonValue);
        }else if (topic.equals(austinRecall)){
            rabbitTemplate.convertAndSend(exchangeName, recallRoutingKey, jsonValue);
        }else {
            log.error("RabbitSendMqServiceImpl send topic error! topic:{}", topic);
        }
    }

    @Override
    public void send(String topic, String jsonValue) {
        send(topic, jsonValue, null);
    }
}

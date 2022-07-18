package com.java3y.austin.support.mq.rocket;

import cn.hutool.core.util.StrUtil;
import com.java3y.austin.support.constans.MessageQueuePipeline;
import com.java3y.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: austin
 * @description:
 * @author: Giorno
 * @create: 2022-07-16
 **/
@Slf4j
@Service
@ConditionalOnProperty(name = "austin-mq-pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
public class RocketSendMqServiceImpl implements SendMqService {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Value("${austin.business.topic.name}")
    private String sendMessageTopic;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (StrUtil.isBlank(topic) || !sendMessageTopic.equals(topic)) {
            log.error("RocketSendMqServiceImpl err:{}", topic);
            return;
        }
        if (StrUtil.isBlank(tagId)) {
            rocketMQTemplate.convertAndSend(topic, jsonValue);
            return;
        }
        rocketMQTemplate.send(topic, MessageBuilder.withPayload(jsonValue)
                        .setHeader(RocketMQHeaders.TAGS, tagId)
                        .build());
    }

    @Override
    public void send(String topic, String jsonValue) {
        this.send(topic, jsonValue);
    }
}

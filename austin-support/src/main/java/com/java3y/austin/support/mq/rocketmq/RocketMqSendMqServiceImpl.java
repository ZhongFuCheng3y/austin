package com.java3y.austin.support.mq.rocketmq;

import com.java3y.austin.support.constans.MessageQueuePipeline;
import com.java3y.austin.support.mq.SendMqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author elpsycongroo
 * create date: 2022/7/15
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
public class RocketMqSendMqServiceImpl implements SendMqService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void send(String topic, String jsonValue, String tagId) {
        if (StringUtils.isNotBlank(tagId)) {
            topic = topic + ":" + tagId;
        }
        send(topic, jsonValue);
    }

    @Override
    public void send(String topic, String jsonValue) {
        rocketMQTemplate.send(topic, MessageBuilder.withPayload(jsonValue).build());
    }
}

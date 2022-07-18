package com.java3y.austin.handler.receiver.rocket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.receiver.service.ConsumeService;
import com.java3y.austin.support.constans.MessageQueuePipeline;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: austin
 * @description:
 * @author: Giorno
 * @create: 2022-07-18
 **/
@Component
@RocketMQMessageListener(topic = "${austin.business.topic.name}",
        consumerGroup = "${austin.rocketmq.consumer.group}",
        selectorType = SelectorType.TAG, selectorExpression = "${austin.business.tagId.value}")
@ConditionalOnProperty(name = "austin-mq-pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
public class RocketMqReceiver implements RocketMQListener<String> {
    @Resource
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StrUtil.isBlank(message)) return;
        consumeService.consume2Send(JSON.parseArray(message, TaskInfo.class));
    }
}

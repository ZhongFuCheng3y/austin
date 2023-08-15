package com.java3y.austin.handler.receiver.rocketmq;

import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.RecallTaskInfo;
import com.java3y.austin.handler.receiver.service.ConsumeService;
import com.java3y.austin.support.constans.MessageQueuePipeline;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author elpsycongroo
 * create date: 2022/7/16
 */
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${austin.business.recall.topic.name}",
        consumerGroup = "${austin.rocketmq.recall.consumer.group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${austin.business.tagId.value}"
)
public class RocketMqRecallReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        RecallTaskInfo recallTaskInfo = JSON.parseObject(message, RecallTaskInfo.class);
        consumeService.consume2recall(recallTaskInfo);
    }
}

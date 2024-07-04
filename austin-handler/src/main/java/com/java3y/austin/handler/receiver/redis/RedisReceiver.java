package com.java3y.austin.handler.receiver.redis;

import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.domain.RecallTaskInfo;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.receiver.MessageReceiver;
import com.java3y.austin.handler.receiver.service.ConsumeService;
import com.java3y.austin.support.constans.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redis 消息队列实现类
 *
 * Guava Eventbus 和 Spring EventBus 只适用于单体服务
 * Redis 适合微服务，且无需单独部署三方消息队列，方便开发与简单应用
 *
 * @author xiaoxiamao
 * @date 2024/7/4
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.REDIS)
public class RedisReceiver implements MessageReceiver {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ConsumeService consumeService;

    @Value("${austin.business.topic.name}")
    private String sendTopic;
    @Value("${austin.business.recall.topic.name}")
    private String recallTopic;

    /**
     * 消费发送消息
     */
    @Scheduled(fixedDelay = 5000)
    public void receiveSendMessage() {
        receiveMessage(sendTopic, message -> {
            List<TaskInfo> taskInfoList = JSON.parseArray(message, TaskInfo.class);
            consumeService.consume2Send(taskInfoList);
        });
    }

    /**
     * 消费撤回消息
     */
    @Scheduled(fixedDelay = 5000)
    public void receiveRecallMessage() {
        receiveMessage(recallTopic, message -> {
            RecallTaskInfo recallTaskInfo = JSON.parseObject(message, RecallTaskInfo.class);
            consumeService.consume2recall(recallTaskInfo);
        });
    }

    /**
     * 消息处理方法
     *
     * @param topic 消息主题
     * @param consumer 消费处理逻辑
     */
    private void receiveMessage(String topic, Consumer<String> consumer) {
        try {
            while (true) {
                // 阻塞操作，减少CPU，IO消耗
                Optional<String> message = Optional.ofNullable(
                        stringRedisTemplate.opsForList().rightPop(topic, 0, TimeUnit.SECONDS));
                if (message.isPresent()) {
                    consumer.accept(message.get());
                }
            }
        } catch (Exception e) {
            log.error("Error receiving messages from Redis topic {}: {}", topic, e);
        }
    }
}

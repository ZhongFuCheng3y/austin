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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redis 消息队列实现类
 *
 * @author xiaoxiamao
 * @date 2024/7/4
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.REDIS)
public class RedisReceiver implements MessageReceiver {

    @Value("${austin.business.topic.name}")
    private String sendTopic;
    @Value("${austin.business.recall.topic.name}")
    private String recallTopic;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ConsumeService consumeService;

    /**
     * 调度线程池
     */
    private ScheduledExecutorService scheduler;

    /**
     * 是否终止线程
     */
    private volatile boolean stop = false;

    /**
     * 初始化调度线程池
     */
    @PostConstruct
    public void init() {
        // 创建调度线程池
        this.scheduler = new ScheduledThreadPoolExecutor(2,
                r -> new Thread(r, "RedisReceiverThread"));
        // 定时调度
        scheduler.scheduleWithFixedDelay(this::receiveSendMessage, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(this::receiveRecallMessage, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 消费发送消息
     */
    public void receiveSendMessage() {
        receiveMessage(sendTopic, message -> {
            log.debug("RedisReceiver#receiveSendMessage message:{}", message);
            List<TaskInfo> taskInfoList = JSON.parseArray(message, TaskInfo.class);
            consumeService.consume2Send(taskInfoList);
        });
    }

    /**
     * 消费撤回消息
     */
    public void receiveRecallMessage() {
        receiveMessage(recallTopic, message -> {
            log.debug("RedisReceiver#receiveRecallMessage recallTaskInfo:{}", message);
            RecallTaskInfo recallTaskInfo = JSON.parseObject(message, RecallTaskInfo.class);
            consumeService.consume2recall(recallTaskInfo);
        });
    }

    /**
     * 消息处理方法
     * <p>
     * 处理责任链有去重处理，此处暂不做
     *
     * @param topic    消息主题
     * @param consumer 消费处理逻辑
     */
    private void receiveMessage(String topic, Consumer<String> consumer) {
        while (!stop) {
            try {
                // 阻塞操作，减少CPU，IO消耗
                Optional<String> message = Optional.ofNullable(
                        stringRedisTemplate.opsForList().rightPop(topic, 20, TimeUnit.SECONDS));
                message.ifPresent(consumer);
            } catch (Exception e) {
                log.error("RedisReceiver#receiveMessage Error receiving messages from Redis topic {}: {}",
                        topic, e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ex) {
                    log.error("RedisReceiver#receiveMessage interrupted: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    /**
     * 销毁调用
     */
    @PreDestroy
    public void onDestroy() {
        stop = true;
        scheduler.shutdown();
    }

}

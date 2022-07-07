package com.java3y.austin.support.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author 3y
 * @date 2022/2/16
 * Kafka工具类
 */
@Component
@Slf4j
public class KafkaUtils {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Value("${austin.business.tagId.key}")
    private String tagIdKey;

    /**
     * 发送kafka消息(不支持tag过滤）
     *
     * @param topicName
     * @param jsonMessage
     */
    public void send(String topicName, String jsonMessage) {
        send(topicName, jsonMessage, null);
    }

    /**
     * 发送kafka消息
     * 支持tag过滤
     *
     * @param topicName
     * @param jsonMessage
     * @param tagId
     */
    public void send(String topicName, String jsonMessage, String tagId) {
        if (StrUtil.isNotBlank(tagId)) {
            List<Header> headers = Arrays.asList(new RecordHeader(tagIdKey, tagId.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(new ProducerRecord(topicName, null, null, null, jsonMessage, headers));
        } else {
            kafkaTemplate.send(topicName, jsonMessage);
        }

    }
}

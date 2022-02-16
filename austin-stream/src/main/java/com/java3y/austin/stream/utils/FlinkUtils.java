package com.java3y.austin.stream.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

/**
 * flink工具类
 *
 * @author 3y
 */
@Slf4j
public class FlinkUtils {
    /**
     * 获取kafkaConsumer
     *
     * @param topicName
     * @param groupId
     * @return
     */
    public KafkaSource<String> getKafkaConsumer(String topicName, String groupId, String broker) {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(broker)
                .setTopics(topicName)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        return source;
    }
}

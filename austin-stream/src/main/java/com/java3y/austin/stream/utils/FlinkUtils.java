package com.java3y.austin.stream.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

/**
 * flink工具类
 *
 * @author 3y
 */
public class FlinkUtils {

    /**
     * 获取kafkaConsumer
     * @param topicName
     * @param groupId
     * @return
     */
    public KafkaSource<String> getKafkaConsumer(String topicName, String groupId) {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("ip:port")
                .setTopics(topicName)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        return source;
    }
}

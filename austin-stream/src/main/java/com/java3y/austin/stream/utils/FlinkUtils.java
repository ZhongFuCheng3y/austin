package com.java3y.austin.stream.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * flink工具类
 *
 * @author 3y
 */
@Component
public class FlinkUtils {

    @Value("${spring.kafka.bootstrap-servers}")
    private String broker;

    /**
     * 获取kafkaConsumer
     * @param topicName
     * @param groupId
     * @return
     */
    public KafkaSource<String> getKafkaConsumer(String topicName, String groupId) {
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

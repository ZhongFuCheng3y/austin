package com.java3y.austin.stream;

import com.java3y.austin.stream.utils.FlinkUtils;
import com.java3y.austin.stream.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * flink启动类
 *
 * @author 3y
 */
@Slf4j
public class AustinBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        String topicName = "austinTopicV2";
        String groupId = "austinTopicV23";

        KafkaSource<String> kafkaConsumer = SpringContextUtils.getBean(FlinkUtils.class)
                .getKafkaConsumer(topicName, groupId);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), "kafkaSource");
        kafkaSource.addSink(new SinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) throws Exception {
                log.error("kafka value:{}", value);
            }
        });
//        DataStream<AnchorInfo> stream = envBatchPendingThread
//                .addSource(new AustinSource())
//                .name("transactions");
//
//        stream.addSink(new AustinSink());

        env.execute("AustinBootStrap");
    }

}

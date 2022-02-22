package com.java3y.austin.stream;

import com.java3y.austin.common.domain.AnchorInfo;
import com.java3y.austin.stream.constants.AustinFlinkConstant;
import com.java3y.austin.stream.function.AustinFlatMapFunction;
import com.java3y.austin.stream.sink.AustinSink;
import com.java3y.austin.stream.utils.MessageQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flink启动类
 *
 * @author 3y
 */
@Slf4j
public class AustinBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 1.获取KafkaConsumer
         */
        KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(AustinFlinkConstant.TOPIC_NAME, AustinFlinkConstant.GROUP_ID, AustinFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), AustinFlinkConstant.SOURCE_NAME);


        /**
         * 2. 数据转换处理
         */
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new AustinFlatMapFunction()).name(AustinFlinkConstant.FUNCTION_NAME);

        /**
         * 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现)
         */
        dataStream.addSink(new AustinSink()).name(AustinFlinkConstant.SINK_NAME);
        env.execute(AustinFlinkConstant.JOB_NAME);

    }

}

package com.java3y.austin.stream;

import com.java3y.austin.stream.constants.AustinFlinkConstant;
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
        SpringContextUtils.loadContext(AustinFlinkConstant.SPRING_CONFIG_PATH);

        /**
         * 1.获取KafkaConsumer
         */
        KafkaSource<String> kafkaConsumer = SpringContextUtils.getBean(FlinkUtils.class).getKafkaConsumer(AustinFlinkConstant.TOPIC_NAME, AustinFlinkConstant.GROUP_ID, AustinFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), AustinFlinkConstant.SOURCE_NAME);


        /**
         * 2. 数据转换处理
         */

        /**
         * 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现)
         */
        kafkaSource.addSink(new SinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) throws Exception {
                log.error("kafka value:{}", value);
            }
        });
        env.execute("AustinBootStrap");
    }

}

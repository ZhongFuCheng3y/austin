package com.java3y.austin.stream.constants;

/**
 * Flink常量信息
 *
 * @author 3y
 */
public class AustinFlinkConstant {
    /**
     * Kafka 配置信息
     * !!! TODO 使用前配置kafka broker ip:port
     * (真实网络ip,这里不能用配置的hosts，看语雀文档得到真实ip)
     * （如果想要自己监听到所有的消息，改掉groupId）
     */
    public static final String GROUP_ID = "austinLogGroup";
    public static final String TOPIC_NAME = "austinTraceLog";
    public static final String BROKER = "austin-kafka:9092";
    /**
     * redis 配置
     * !!!  TODO 使用前配置redis ip:port
     * (真实网络ip,这里不能用配置的hosts，看语雀文档得到真实ip)
     */
    public static final String REDIS_IP = "austin-redis";
    public static final String REDIS_PORT = "6379";
    public static final String REDIS_PASSWORD = "austin";
    /**
     * Flink流程常量
     */
    public static final String SOURCE_NAME = "austin_kafka_source";
    public static final String FUNCTION_NAME = "austin_transfer";
    public static final String SINK_NAME = "austin_sink";
    public static final String JOB_NAME = "AustinBootStrap";
    private AustinFlinkConstant() {
    }


}

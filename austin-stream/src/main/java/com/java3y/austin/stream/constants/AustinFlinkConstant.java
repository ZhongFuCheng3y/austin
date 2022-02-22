package com.java3y.austin.stream.constants;

/**
 * Flink常量信息
 * @author 3y
 */
public class AustinFlinkConstant {

    /**
     * Kafka 配置信息
     * TODO 使用前配置kafka broker ip:port
     */
    public static final String GROUP_ID = "austinLogGroup";
    public static final String TOPIC_NAME = "austinLog";
    public static final String BROKER = "ip:port";

    /**
     * redis 配置
     * TODO 使用前配置redis ip:port
     */
    public static final String REDIS_IP = "ip";
    public static final String REDIS_PORT = "port";
    public static final String REDIS_PASSWORD = "austin";


    /**
     * Flink流程常量
     */
    public static final String SOURCE_NAME = "austin_kafka_source";
    public static final String FUNCTION_NAME = "austin_transfer";
    public static final String SINK_NAME = "austin_sink";
    public static final String JOB_NAME = "AustinBootStrap";


}

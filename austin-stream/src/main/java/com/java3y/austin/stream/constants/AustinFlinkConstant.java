package com.java3y.austin.stream.constants;

public class AustinFlinkConstant {

    /**
     * Kafka 配置信息
     * TODO 使用前需要把broker配置
     */
    public static final String GROUP_ID = "austinLogGroup";
    public static final String TOPIC_NAME = "austinLog";
    public static final String BROKER = "ip:port";


    /**
     * spring配置文件路径
     */
    public static final String SPRING_CONFIG_PATH = "classpath*:austin-spring.xml";


    /**
     * Flink流程常量
     */
    public static final String SOURCE_NAME = "austin_kafka_source";
    public static final String FUNCTION_NAME = "austin_transfer";
    public static final String SINK_NAME = "austin_sink";

}

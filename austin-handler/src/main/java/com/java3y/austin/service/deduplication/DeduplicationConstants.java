package com.java3y.austin.service.deduplication;

/**
 * @author huskey
 * @date 2022/1/18
 */
public class DeduplicationConstants {

    /**
     * 配置样例：{"contentDeduplication":{"num":1,"time":300},"frequencyDeduplication":{"num":5}}
     */
    public static final String DEDUPLICATION_RULE_KEY = "deduplication";
    public static final String CONTENT_DEDUPLICATION = "contentDeduplication";
    public static final String FREQUENCY_DEDUPLICATION = "frequencyDeduplication";
}

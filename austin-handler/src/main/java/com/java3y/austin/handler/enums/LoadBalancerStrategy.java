package com.java3y.austin.handler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author Gavin
 * @Date 2024/9/14
 */
@Getter
@ToString
@AllArgsConstructor
public class LoadBalancerStrategy {

    /**
     * 随机算法
     */
    public static final String SERVICE_LOAD_BALANCER_RANDOM = "random";

    /**
     * 加权随机算法
     */
    public static final String SERVICE_LOAD_BALANCER_RANDOM_WEIGHT_ENHANCED = "random_weight";

    /**
     * 哈希算法
     */
    public static final String SERVICE_LOAD_BALANCER_HASH = "hash";

    /**
     * 轮询算法
     */
    public static final String SERVICE_LOAD_BALANCER_ROBIN = "robin";

    /**
     * 加权轮询算法
     */
    public static final String SERVICE_LOAD_BALANCER_ROBIN_WEIGHT = "robin_weight";

}

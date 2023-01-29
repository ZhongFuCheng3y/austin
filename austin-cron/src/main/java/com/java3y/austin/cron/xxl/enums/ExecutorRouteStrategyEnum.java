package com.java3y.austin.cron.xxl.enums;


/**
 * 路由策略
 *
 * @author 3y
 */
public enum ExecutorRouteStrategyEnum {

    /**
     * FIRST
     */
    FIRST,
    /**
     * LAST
     */
    LAST,
    /**
     * ROUND
     */
    ROUND,
    /**
     * RANDOM
     */
    RANDOM,
    /**
     * CONSISTENT_HASH
     */
    CONSISTENT_HASH,
    /**
     * LEAST_FREQUENTLY_USED
     */
    LEAST_FREQUENTLY_USED,
    /**
     * LEAST_RECENTLY_USED
     */
    LEAST_RECENTLY_USED,
    /**
     * FAILOVER
     */
    FAILOVER,
    /**
     * BUSYOVER
     */
    BUSYOVER,
    /**
     * SHARDING_BROADCAST
     */
    SHARDING_BROADCAST;

    ExecutorRouteStrategyEnum() {
    }
}

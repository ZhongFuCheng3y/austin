package com.java3y.austin.cron.xxl.enums;


/**
 * 路由策略
 * @author 3y
 */
public enum ExecutorRouteStrategyEnum {

    FIRST,
    LAST,
    ROUND,
    RANDOM,
    CONSISTENT_HASH,
    LEAST_FREQUENTLY_USED,
    LEAST_RECENTLY_USED,
    FAILOVER,
    BUSYOVER,
    SHARDING_BROADCAST;

    ExecutorRouteStrategyEnum() {
    }
}

package com.java3y.austin.cron.xxl.enums;

/**
 * 调度类型
 *
 * @author 3y
 */
public enum ScheduleTypeEnum {

    NONE,
    /**
     * schedule by cron
     */
    CRON,

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE;

    ScheduleTypeEnum() {
    }

}

package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 去重类型枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum DeduplicationType implements PowerfulEnum {

    /**
     * 相同内容去重
     */
    CONTENT(10, "N分钟相同内容去重"),

    /**
     * 渠道接受消息 频次 去重
     */
    FREQUENCY(20, "一天内N次相同渠道去重"),
    ;
    private final Integer code;
    private final String description;
}

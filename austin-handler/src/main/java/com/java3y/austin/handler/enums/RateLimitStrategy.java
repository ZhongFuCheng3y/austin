package com.java3y.austin.handler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 限流枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum RateLimitStrategy {


    /**
     * 根据真实请求数限流 (实际意义上的QPS）
     */
    REQUEST_RATE_LIMIT(10, "根据真实请求数限流"),
    /**
     * 根据发送用户数限流（人数限流）
     */
    SEND_USER_NUM_RATE_LIMIT(20, "根据发送用户数限流"),
    ;

    private final Integer code;
    private final String description;


}

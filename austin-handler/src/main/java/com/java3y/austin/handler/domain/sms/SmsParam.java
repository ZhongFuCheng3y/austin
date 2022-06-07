package com.java3y.austin.handler.domain.sms;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author 3y
 * @date 2021/11/4
 * 发送短信参数
 */
@Data
@Builder
public class SmsParam {

    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 需要发送的手机号
     */
    private Set<String> phones;

    /**
     * 发送文案
     */
    private String content;
}

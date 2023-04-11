package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送的消息类型
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageType implements PowerfulEnum {

    /**
     * 通知类消息
     */
    NOTICE(10, "通知类消息", "notice"),
    /**
     * 营销类消息
     */
    MARKETING(20, "营销类消息", "marketing"),
    /**
     * 验证码消息
     */
    AUTH_CODE(30, "验证码消息", "auth_code");

    /**
     * 编码值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;


    /**
     * 英文标识
     */
    private final String codeEn;


}

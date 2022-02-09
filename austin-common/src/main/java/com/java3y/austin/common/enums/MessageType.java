package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送的消息类型
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageType {
    NOTICE(10,"通知类消息","notice"),
    MARKETING(20,"营销类消息","marketing"),
    AUTH_CODE(30,"验证码消息","auth_code")
    ;
    /**
     * 编码值
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;


    /**
     * 英文标识
     */
    private String codeEn;


    /**
     * 通过code获取enum
     * @param code
     * @return
     */
    public static MessageType getEnumByCode(Integer code) {
        MessageType[] values = values();
        for (MessageType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}

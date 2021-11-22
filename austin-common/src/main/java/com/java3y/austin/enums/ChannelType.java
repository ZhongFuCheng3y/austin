package com.java3y.austin.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送渠道类型枚举
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType {
    IM(10, "IM(站内信)"),
    PUSH(20, "push(通知栏)"),
    SMS(30, "sms(短信)"),
    EMAIL(40, "email(邮件)"),
    OFFICIAL_ACCOUNT(50, "OfficialAccounts(服务号)"),
    MINI_PROGRAM(60, "miniProgram(小程序)")
    ;

    private Integer code;
    private String description;


}

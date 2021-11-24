package com.java3y.austin.enums;


import com.java3y.austin.dto.*;
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
    IM(10, "IM(站内信)", ImContentModel.class),
    PUSH(20, "push(通知栏)", PushContentModel.class),
    SMS(30, "sms(短信)", SmsContentModel.class),
    EMAIL(40, "email(邮件)", EmailContentModel.class),
    OFFICIAL_ACCOUNT(50, "OfficialAccounts(服务号)", OfficialAccountsContentModel.class),
    MINI_PROGRAM(60, "miniProgram(小程序)", MiniProgramContentModel.class),
    ;

    private Integer code;
    private String description;
    private Class contentModelClass;


    public static Class getChanelModelClassByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value.getContentModelClass();
            }
        }
        return null;
    }
}

package com.java3y.austin.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 短信渠道商
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsSupplier {


    TENCENT(10,"腾讯渠道商"),
    YUN_PAIN(20,"云片渠道商");
    private final Integer code;
    private final String description;


    /**
     * 根据状态获取描述信息
     * @param code
     * @return
     */
    public static String getDescriptionByStatus(Integer code) {
        for (SmsStatus value : SmsStatus.values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return "";
    }
}

package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 短信状态信息
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsStatus {

    SEND_SUCCESS(10,"调用渠道接口发送成功"),
    RECEIVE_SUCCESS(20,"用户收到短信(收到渠道短信回执，状态成功)"),
    RECEIVE_FAIL(30, "用户收不到短信(收到渠道短信回执，状态失败)"),
    SEND_FAIL(40, "调用渠道接口发送失败");

    private Integer code;
    private String description;


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

package com.java3y.austin.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 打点信息枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum AnchorState {

    RECEIVE(10, "消息接收成功"),
    DISCARD(20, "消费被丢弃"),
    NIGHT_SHIELD(22, "夜间屏蔽"),
    NIGHT_SHIELD_NEXT_SEND(24, "夜间屏蔽(次日早上9点发送)"),
    CONTENT_DEDUPLICATION(30, "消息被内容去重"),
    RULE_DEDUPLICATION(40, "消息被频次去重"),
    WHITE_LIST(50, "白名单过滤"),
    SEND_SUCCESS(60, "消息下发成功"),
    SEND_FAIL(70, "消息下发失败"),

    CLICK(0100, "消息被点击"),
    ;


    private Integer code;
    private String description;

    /**
     * 通过code获取描述
     *
     * @param code
     * @return
     */
    public static String getDescriptionByCode(Integer code) {
        for (AnchorState anchorState : AnchorState.values()) {
            if (anchorState.getCode().equals(code)) {
                return anchorState.getDescription();
            }
        }
        return "未知点位";
    }

}

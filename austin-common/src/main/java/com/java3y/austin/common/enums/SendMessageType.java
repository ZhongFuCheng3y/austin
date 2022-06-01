package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 微信应用消息/钉钉/服务号均有多种的消息类型下发
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum SendMessageType {

    TEXT("10", "文本", "text"),
    VOICE("20", "语音", null),
    VIDEO("30", "视频", null),
    NEWS("40", "图文", "feedCard"),
    TEXT_CARD("50", "文本卡片", null),
    FILE("60", "文件", null),
    MINI_PROGRAM_NOTICE("70", "小程序通知", null),
    MARKDOWN("80", "markdown", "markdown"),
    TEMPLATE_CARD("90", "模板卡片", null),
    IMAGE("100", "图片", null),
    LINK("110", "链接消息", "link"),
    ACTION_CARD("120", "跳转卡片消息", "actionCard"),
    ;

    private String code;
    private String description;
    private String dingDingRobotType;


    /**
     * 通过code获取钉钉的Type值
     *
     * @param code
     * @return
     */
    public static String getDingDingRobotTypeByCode(String code) {
        for (SendMessageType value : SendMessageType.values()) {
            if (value.getCode().equals(code)) {
                return value.getDingDingRobotType();
            }
        }
        return null;
    }


}

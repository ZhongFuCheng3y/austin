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

    TEXT("10", "文本", "text", "text"),
    VOICE("20", "语音", null, "voice"),
    VIDEO("30", "视频", null, null),
    NEWS("40", "图文", "feedCard", null),
    TEXT_CARD("50", "文本卡片", null, null),
    FILE("60", "文件", null, "file"),
    MINI_PROGRAM_NOTICE("70", "小程序通知", null, null),
    MARKDOWN("80", "markdown", "markdown", "markdown"),
    TEMPLATE_CARD("90", "模板卡片", null, null),
    IMAGE("100", "图片", null, "image"),
    LINK("110", "链接消息", "link", "link"),
    ACTION_CARD("120", "跳转卡片消息", "actionCard", "action_card"),
    OA("130", "OA消息", null, "oa"),
    MP_NEWS("140", "图文消息(mpnews)", null, null),

    ;

    private String code;
    private String description;

    /**
     * 钉钉工作消息的类型值
     */
    private String dingDingRobotType;

    /**
     * 钉钉机器人消息的类型值
     */
    private String dingDingWorkType;


    /**
     * 通过code获取钉钉机器人的Type值
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

    /**
     * 通过code获取钉钉工作通知的Type值
     *
     * @param code
     * @return
     */
    public static String getDingDingWorkTypeByCode(String code) {
        for (SendMessageType value : SendMessageType.values()) {
            if (value.getCode().equals(code)) {
                return value.getDingDingWorkType();
            }
        }
        return null;
    }


}

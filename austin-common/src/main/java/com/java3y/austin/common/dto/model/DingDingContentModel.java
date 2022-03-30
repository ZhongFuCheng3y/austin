package com.java3y.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * 钉钉 自定义机器人 + 工作通知
 * <p>
 * https://open.dingtalk.com/document/group/custom-robot-access
 * <p>
 * https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingContentModel extends ContentModel {

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 【文本消息】需要发送的内容
     */
    private String content;

    /**
     * 图片、文件、语音消息 需要发送使用的素材ID字段
     */
    private String mediaId;

    // ...
}

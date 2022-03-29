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
    private String content;
}

package com.java3y.austin.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * 钉钉 自定义机器人
 * https://open.dingtalk.com/document/group/custom-robot-access
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingContentModel extends ContentModel {
    private String content;
}

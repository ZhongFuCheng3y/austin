package com.java3y.austin.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * 发送任务信息
 */
@Data
@Builder
public class TaskInfo {

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 业务Id
     */
    private Long businessId;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 发送的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private Integer sendChannel;

    /**
     * 模板类型
     */
    private Integer templateType;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 发送文案内容
     */
    private String content;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;

    /**
     * 消息去重时间 单位小时
     */
    private Integer deduplicationTime;

    /**
     * 是否夜间屏蔽
     * 0:不屏蔽
     * 1：屏蔽
     */
    private Integer isNightShield;


}

package com.java3y.austin.handler.domain.wechat;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author sunql
 * @date 2022年05月06日 9:56
 *
 * 服务号参数
 */
@Data
@Builder
public class WeChatOfficialParam {
    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 关注服务号得用户
     */
    private Set<String> openIds;

    /**
     * 模板消息的信息载体
     */
    private Map<String, String> data;

    /**
     * 发送账号
     */
    private Integer sendAccount;
}

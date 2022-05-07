package com.java3y.austin.handler.domain.wechat;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author sunql
 * @date 2022年05月06日 15:56
 *
 * 小程序参数
 */
@Data
@Builder
public class WeChatMiniProgramParam {
    /**
     * 业务Id
     */
    private Long messageTemplateId;

    /**
     * 发送账号
     */
    private Integer sendAccount;

    /**
     * 接收者（用户）的 openid
     */
    private Set<String> openIds;

    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     */
    private Map<String, String> data;

}

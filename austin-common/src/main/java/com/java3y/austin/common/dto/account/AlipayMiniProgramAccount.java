package com.java3y.austin.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jwq
 * 支付宝小程序订阅消息账号配置
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlipayMiniProgramAccount {

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 支付宝小程序的AppID
     */
    private String appId;

    /**
     * 订阅模版Id
     */
    private String userTemplateId;

    /**
     * 点击跳转到的小程序页面
     */
    private String page;
}

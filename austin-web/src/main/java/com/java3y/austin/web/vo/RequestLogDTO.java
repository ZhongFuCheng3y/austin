package com.java3y.austin.web.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kl
 * @version 1.0.0
 * @description 请求日志Vo
 * @date 2023/2/23 9:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestLogDTO {

    /**
     * 请求ID（UUID）与 ResponseLogVo id 一致
     */
    private String id;

    /**
     * 接口URI
     */
    @JSONField(ordinal = 1)
    private String uri;

    /**
     * 请求方法
     */
    @JSONField(ordinal = 2)
    private String method;

    /**
     * 参数数组
     */
    @JSONField(ordinal = 3)
    private Object[] args;

    /**
     * 是否需要认证
     */
    @JSONField(ordinal = 4)
    private Boolean auth;

    /**
     * 认证令牌
     */
    @JSONField(ordinal = 5)
    private String token;

    /**
     * 登录账号信息
     */
    @JSONField(ordinal = 6)
    private Object loginAccount;

    /**
     * 产品
     */
    @JSONField(ordinal = 7)
    private String product;

    /**
     * 类名+方法名
     */
    @JSONField(ordinal = 8)
    private String path;

    /**
     * 页面引用
     */
    @JSONField(ordinal = 9)
    private String referer;

    /**
     * 请求地址
     */
    @JSONField(ordinal = 10)
    private String remoteAddr;

    /**
     * 用户代理（浏览器）
     */
    @JSONField(ordinal = 11)
    private String userAgent;
}

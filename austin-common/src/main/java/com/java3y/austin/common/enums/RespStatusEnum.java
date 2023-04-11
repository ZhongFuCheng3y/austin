package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 全局响应状态枚举
 *
 * @author zzb
 * @since 2021.11.17
 **/
@Getter
@ToString
@AllArgsConstructor
public enum RespStatusEnum {

    /**
     * 错误
     */
    ERROR_500("500", "服务器未知错误"),
    ERROR_400("400", "错误请求"),

    /**
     * OK：操作成功
     */
    SUCCESS("0", "操作成功"),
    FAIL("-1", "操作失败"),


    /**
     * 客户端
     */
    CLIENT_BAD_PARAMETERS("A0001", "客户端参数错误"),
    TEMPLATE_NOT_FOUND("A0002", "找不到模板或模板已被删除"),
    TOO_MANY_RECEIVER("A0003", "传入的接收者大于100个"),
    DO_NOT_NEED_LOGIN("A0004", "非测试环境，无须登录"),
    NO_LOGIN("A0005", "还未登录，请先登录"),

    /**
     * 系统
     */
    SERVICE_ERROR("B0001", "服务执行异常"),
    RESOURCE_NOT_FOUND("B0404", "资源不存在"),


    /**
     * pipeline
     */
    CONTEXT_IS_NULL("P0001", "流程上下文为空"),
    BUSINESS_CODE_IS_NULL("P0002", "业务代码为空"),
    PROCESS_TEMPLATE_IS_NULL("P0003", "流程模板配置为空"),
    PROCESS_LIST_IS_NULL("P0004", "业务处理器配置为空"),


    ;

    /**
     * 响应状态
     */
    private final String code;
    /**
     * 响应编码
     */
    private final String msg;
}

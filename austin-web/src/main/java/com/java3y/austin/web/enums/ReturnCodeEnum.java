package com.java3y.austin.web.enums;

public enum ReturnCodeEnum {

    // 200
    SUCCESS(200, "成功"),

    // 400
    PARAM_ERROR(4001, "参数错误"),
    DATA_ALREADY_IN_USE(4002, "数据已经被使用"),
    PASSWORD_ERROR(4003, "密码错误"),

    // 403
    FORBIDDEN(4031, "没有权限"),
    TOKEN_EXPIRED(4032, "token过期"),
    TOKEN_INVALID(4033, "token无效"),
    ACCOUNT_INVALID(4034, "账号已经失效"),
    TOO_MANY_REQUEST(4035, "请求太频繁了，请稍后再试"),
    USER_ACCOUNT_INVALID(4036, "账户无效"),
    BALANCE_NOT_ENOUGH(4037, "余额不足"),

    // 404
    NOT_FOUND(4041, "not found"),
    DATA_NOT_EXIST(4042, "数据不存在"),
    LOGGED_USER_NOT_AVAILABLE(4043, "未获取到登录用户"),
    USER_NOT_EXISTS(4044, "用户不存在"),

    // 500
    SYSTEM_ERROR(5001, "系统异常"),
    BUSINESS_ERROR(5002, "业务异常"),
    DATA_ALREADY_EXISTS(5003, "数据已经存在"),
    LOGIN_FAIL(5004, "登录失败"),
    ;

    ReturnCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;

    private final String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}

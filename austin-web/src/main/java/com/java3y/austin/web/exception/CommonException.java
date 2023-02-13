package com.java3y.austin.web.exception;

import com.java3y.austin.common.enums.RespStatusEnum;

/**
 * @author kl
 * @version 1.0.0
 * @description 通用异常
 * @date 2023/2/9 19:00
 */
public class CommonException extends RuntimeException {
    private String code = RespStatusEnum.ERROR_400.getCode();

    public CommonException(String message) {
        super(message);
    }

    public CommonException(RespStatusEnum respStatusEnum) {
        super(respStatusEnum.getMsg());
        this.code = respStatusEnum.getCode();
    }

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String message, Exception e) {
        super(message, e);
    }

    public CommonException(String code, String message, Exception e) {
        super(message, e);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
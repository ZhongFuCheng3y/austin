package com.java3y.austin.web.exception;

import com.java3y.austin.web.enums.ReturnCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private ReturnCodeEnum returnCodeEnum;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ReturnCodeEnum returnCodeEnum) {
        super(returnCodeEnum.getDesc());
        this.returnCodeEnum = returnCodeEnum;
    }

    public BusinessException(String message, ReturnCodeEnum returnCodeEnum) {
        super(message);
        this.returnCodeEnum = returnCodeEnum;
    }

    public ReturnCodeEnum getReturnCodeEnum() {
        return ReturnCodeEnum.BUSINESS_ERROR;
    }

}

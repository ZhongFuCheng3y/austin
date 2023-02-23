package com.java3y.austin.web.exception;

import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import org.assertj.core.util.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author kl
 * @version 1.0.0
 * @description 拦截异常统一返回
 * @date 2023/2/9 19:03
 */
@ControllerAdvice(basePackages = "com.java3y.austin.web.controller")
@ResponseBody
public class ExceptionHandlerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    public ExceptionHandlerAdvice() {
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public BasicResultVO exceptionResponse(Exception e) {
        BasicResultVO result = BasicResultVO.fail(RespStatusEnum.ERROR_500, "\r\n" + Throwables.getStackTrace(e) + "\r\n");
        log.error(Throwables.getStackTrace(e));
        return result;
    }

    @ExceptionHandler({CommonException.class})
    @ResponseStatus(HttpStatus.OK)
    public BasicResultVO commonResponse(CommonException ce) {
        log.error(Throwables.getStackTrace(ce));
        return new BasicResultVO(ce.getCode(), ce.getMessage(), ce.getRespStatusEnum());
    }
}


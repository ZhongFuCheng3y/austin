package com.java3y.austin.web.exception;

import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author kl
 * @version 1.0.0
 * @description 拦截异常统一返回
 * @date 2023/2/9 19:03
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    public ExceptionHandlerAdvice() {
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public BasicResultVO exceptionResponse(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        BasicResultVO result = BasicResultVO.fail(RespStatusEnum.ERROR_500, "\r\n" + sw + "\r\n");
        log.error(e.getMessage(), e);
        return result;
    }

    @ExceptionHandler({CommonException.class})
    @ResponseStatus(HttpStatus.OK)
    public BasicResultVO commonResponse(CommonException ce) {
        BasicResultVO result = BasicResultVO.fail(RespStatusEnum.ERROR_400, ce.getMessage());
        log.error(ce.getMessage(), ce);
        return result;
    }
}


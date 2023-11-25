package com.java3y.austin.web.aspect;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.java3y.austin.web.vo.RequestLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author kl
 * @version 1.0.0
 * @description 切面类
 * @date 2023/2/23 9:17
 */
@Slf4j
@Aspect
@Component
public class AustinAspect {

    /**
     * 同一个请求的KEY
     */
    private static final String REQUEST_ID_KEY = "request_unique_id";
    @Autowired
    private HttpServletRequest request;

    /**
     * 只切AustinAspect注解
     */
    @Pointcut("@within(com.java3y.austin.web.annotation.AustinAspect) || @annotation(com.java3y.austin.web.annotation.AustinAspect)")
    public void executeService() {
    }

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        this.printRequestLog(methodSignature, joinPoint.getArgs());
    }

    /**
     * 异常通知
     *
     * @param ex
     */
    @AfterThrowing(value = "executeService()", throwing = "ex")
    public void doAfterThrowingAdvice(Throwable ex) {
        printExceptionLog(ex);
    }

    /**
     * 打印请求日志
     *
     * @param methodSignature
     * @param argObs
     */
    public void printRequestLog(MethodSignature methodSignature, Object[] argObs) {
        RequestLogDTO logVo = new RequestLogDTO();
        //设置请求唯一ID
        logVo.setId(IdUtil.fastUUID());
        request.setAttribute(REQUEST_ID_KEY, logVo.getId());
        logVo.setUri(request.getRequestURI());
        logVo.setMethod(request.getMethod());
        List<Object> args = Lists.newArrayList();
        //过滤掉一些不能转为json字符串的参数
        Arrays.stream(argObs).forEach(e -> {
            if (e instanceof MultipartFile || e instanceof HttpServletRequest
                    || e instanceof HttpServletResponse || e instanceof BindingResult) {
                return;
            }
            args.add(e);
        });
        logVo.setArgs(args.toArray());
        logVo.setProduct("austin");
        logVo.setPath(methodSignature.getDeclaringTypeName() + "." + methodSignature.getMethod().getName());
        logVo.setReferer(request.getHeader("referer"));
        logVo.setRemoteAddr(request.getRemoteAddr());
        logVo.setUserAgent(request.getHeader("user-agent"));
        log.info(JSON.toJSONString(logVo));
    }

    /**
     * 打印异常日志
     *
     * @param ex
     */
    public void printExceptionLog(Throwable ex) {
        JSONObject logVo = new JSONObject();
        logVo.put("id", request.getAttribute(REQUEST_ID_KEY));
        log.error(JSON.toJSONString(logVo), ex);
    }
}

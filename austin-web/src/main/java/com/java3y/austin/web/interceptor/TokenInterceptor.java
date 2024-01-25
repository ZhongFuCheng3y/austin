package com.java3y.austin.web.interceptor;


import com.alibaba.fastjson.JSON;
import com.java3y.austin.web.config.JwtConfig;
import com.java3y.austin.web.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {


    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getRequestURI().startsWith("/actuator") || request.getRequestURI().startsWith("/user/login")) {
            return true;
        }

        String jwtToken = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(jwtToken)) {
            throw new RuntimeException("need token");
        }

        Claims claims = JwtUtil.decodeAndVerify(jwtToken, jwtConfig.getKey());
        String subject = claims.getSubject();
        String creator = StringUtils.defaultIfEmpty(JSON.parseObject(subject).getString("creator"), JSON.parseObject(subject).getString("userId"));

        request.setAttribute("creator", creator);
        log.info("request {} with creator {} ", request.getRequestURI(), creator);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}

package com.java3y.austin.handler.script;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 标识 短信渠道
 *
 * @author 3y
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface SmsScriptHandler {

    /**
     * 这里输入脚本名
     *
     * @return
     */
    String value();
}

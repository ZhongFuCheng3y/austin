package com.java3y.austin.web.annotation;

import java.lang.annotation.*;

/**
 * @author kl
 * @version 1.0.0
 * @description 统一返回注解
 * @date 2023/2/9 19:00
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AustinResult {
}

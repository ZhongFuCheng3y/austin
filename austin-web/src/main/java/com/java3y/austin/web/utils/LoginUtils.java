package com.java3y.austin.web.utils;

import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.constant.OfficialAccountParamConstant;
import com.java3y.austin.web.config.WeChatLoginConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 3y
 * @date 2022/12/22
 * 微信服务号登录的Utils
 */
@Component
@Slf4j
public class LoginUtils {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 测试环境 使用
     * 获取 WeChatLoginConfig 对象
     *
     * @return
     */
    public WeChatLoginConfig getLoginConfig() {
        try {
            return applicationContext.getBean(OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG, WeChatLoginConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 测试环境使用
     * 判断是否需要登录
     *
     * @return
     */
    public boolean needLogin() {
        try {
            if (CommonConstant.ENV_TEST.equals(env)) {
                WeChatLoginConfig bean = applicationContext.getBean(OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG, WeChatLoginConfig.class);
                if (Objects.nonNull(bean)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("LoginUtils#needLogin fail:{}", Throwables.getStackTraceAsString(e));
        }
        return false;
    }
}

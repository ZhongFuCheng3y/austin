package com.java3y.austin.support.utils;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.Properties;

/**
 * @program: austin
 * @description:
 * @author: Giorno
 * @create: 2022-07-28
 **/
@Slf4j
@Component
public class NacosUtils {
    private final Properties properties = new Properties();
    @NacosInjected
    private ConfigService configService;
    @Value("${nacos.group}")
    private String nacosGroup;
    @Value("${nacos.data-id}")
    private String nacosDataId;

    public String getProperty(String key, String defaultValue) {
        try {
            String property = this.getContext();
            if (StringUtils.hasText(property)) {
                properties.load(new StringReader(property));
            }
        } catch (Exception e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        String property = properties.getProperty(key);
        return CharSequenceUtil.isBlank(property) ? defaultValue : property;
    }

    private String getContext() {
        String context = null;
        try {
            context = configService.getConfig(nacosDataId, nacosGroup, 5000);
        } catch (NacosException e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        return context;
    }
}

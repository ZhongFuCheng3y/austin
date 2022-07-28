package com.java3y.austin.support.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
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
    @Value("${austin.nacos.server}")
    private String nacosServer;
    @Value("${austin.nacos.group}")
    private String nacosGroup;
    @Value("${austin.nacos.dataId}")
    private String nacosDataId;
    @Value("${austin.nacos.namespace}")
    private String nacosNamespace;
    private final Properties request = new Properties();
    private final Properties properties = new Properties();

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
        return StrUtil.isBlank(property) ? defaultValue : property;
    }

    private String getContext() {
        String context = null;
        try {
            request.put(PropertyKeyConst.SERVER_ADDR, nacosServer);
            request.put(PropertyKeyConst.NAMESPACE, nacosNamespace);
            context = NacosFactory.createConfigService(request)
                    .getConfig(nacosDataId, nacosGroup, 5000);
        } catch (NacosException e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        return context;
    }
}

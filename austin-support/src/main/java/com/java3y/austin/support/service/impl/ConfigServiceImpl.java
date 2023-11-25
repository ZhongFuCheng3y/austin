package com.java3y.austin.support.service.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.setting.dialect.Props;
import com.ctrip.framework.apollo.Config;
import com.java3y.austin.support.service.ConfigService;
import com.java3y.austin.support.utils.NacosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


/**
 * @author 3y
 * 读取配置实现类
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    /**
     * 本地配置
     */
    private static final String PROPERTIES_PATH = "local.properties";
    private Props props = new Props(PROPERTIES_PATH, StandardCharsets.UTF_8);

    /**
     * apollo配置
     */
    @Value("${apollo.bootstrap.enabled}")
    private Boolean enableApollo;
    @Value("${apollo.bootstrap.namespaces}")
    private String namespaces;
    /**
     * nacos配置
     */
    @Value("${austin.nacos.enabled}")
    private Boolean enableNacos;
    @Autowired
    private NacosUtils nacosUtils;


    @Override
    public String getProperty(String key, String defaultValue) {
        if (Boolean.TRUE.equals(enableApollo)) {
            Config config = com.ctrip.framework.apollo.ConfigService.getConfig(namespaces.split(StrPool.COMMA)[0]);
            return config.getProperty(key, defaultValue);
        } else if (Boolean.TRUE.equals(enableNacos)) {
            return nacosUtils.getProperty(key, defaultValue);
        } else {
            return props.getProperty(key, defaultValue);
        }
    }
}

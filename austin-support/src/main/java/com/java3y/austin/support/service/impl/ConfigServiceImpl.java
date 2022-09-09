package com.java3y.austin.support.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.ctrip.framework.apollo.Config;
import com.java3y.austin.support.service.ConfigService;
import com.java3y.austin.support.utils.NacosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.*;


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

    /**
     * Mysql配置
     */
    @Value("${austin.mysql.enable}")
    private Boolean enableMysql;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${austin.database.username}")
    private String user;
    @Value("${austin.database.password}")
    private String pass;
    @Value("${spring.datasource.driver-class-name}")
    private String className;

    public String getPropertyById(Integer sendAccount) {
        Connection conn = null;
        Statement stmt = null;
        String accountConfig = null;
        try {
            Class.forName(className);
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            int Cid = sendAccount - 1;
            String sql = "SELECT id, name, send_channel, account_config FROM channel_account LIMIT "+Cid+",1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                accountConfig = rs.getString("account_config");
//                System.out.println("选用账号的参数为: " + accountConfig);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se1) {
                se1.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
        return accountConfig;
    }

    @Override
    public String getProperty(Integer sendAccount, String key, String defaultValue) {
        if (enableApollo) {
            Config config = com.ctrip.framework.apollo.ConfigService.getConfig(namespaces.split(StrUtil.COMMA)[0]);
            return config.getProperty(key, defaultValue);
        } else if (enableNacos) {
            return nacosUtils.getProperty(key, defaultValue);
        } else if (enableMysql) {
            return getPropertyById(sendAccount);
        } else {
//            System.out.println(props.getProperty(key, defaultValue));
            return props.getProperty(key, defaultValue);
        }
    }
    @Override
    public String getProperty(String key, String defaultValue) {
        if (enableApollo) {
            Config config = com.ctrip.framework.apollo.ConfigService.getConfig(namespaces.split(StrUtil.COMMA)[0]);
            return config.getProperty(key, defaultValue);
        } else if (enableNacos) {
            return nacosUtils.getProperty(key, defaultValue);
        } else {
            return props.getProperty(key, defaultValue);
        }
    }
}

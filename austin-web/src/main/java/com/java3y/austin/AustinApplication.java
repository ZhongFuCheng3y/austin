package com.java3y.austin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 3y
 */
@SpringBootApplication
public class AustinApplication {
    public static void main(String[] args) {

        /**
         *  TODO 【optional】
         * 如果你需要动态配置
         * 1、启动apollo
         * 2、将application.properties配置文件的 apollo.enabled 改为true
         * 3、下方的property替换真实的ip和port
         */
        System.setProperty("apollo.config-service", "http://ip:port");
        SpringApplication.run(AustinApplication.class, args);
    }
}

package com.java3y.austin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 3y
 */
@SpringBootApplication
public class AustinApplication {
    public static void main(String[] args) {

        // TODO 如果你需要使用apollo，将application.properties配置文件的 apollo.enabled 改为true
        System.setProperty("apollo.config-service", "http://austin.apollo.config:5001");
        SpringApplication.run(AustinApplication.class, args);
    }
}

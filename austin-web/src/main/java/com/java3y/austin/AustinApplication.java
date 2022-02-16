package com.java3y.austin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 3y
 */
@SpringBootApplication
public class AustinApplication {
    public static void main(String[] args) {
        // TODO apollo的ip/port【must】
        System.setProperty("apollo.config-service", "http://ip:7000");
        SpringApplication.run(AustinApplication.class, args);
    }
}

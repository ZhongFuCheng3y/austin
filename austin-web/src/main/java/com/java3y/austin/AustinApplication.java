package com.java3y.austin;

import com.java3y.austin.common.constant.AustinConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 3y
 */
@SpringBootApplication
@Slf4j
public class AustinApplication implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    public static void main(String[] args) {
        /**
         * 如果你需要启动Apollo动态配置
         * 1、启动apollo
         * 2、将application.properties配置文件的 austin.apollo.enabled 改为true
         * 3、下方的property替换真实的ip和port
         */
        System.setProperty("apollo.config-service", "http://austin-apollo-config:8080");
        SpringApplication.run(AustinApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        log.info(AnsiOutput.toString(AustinConstant.PROJECT_BANNER, "\n", AnsiColor.GREEN, AustinConstant.PROJECT_NAME, AnsiColor.DEFAULT, AnsiStyle.FAINT));
        log.info("Austin start succeeded, Index >> http://127.0.0.1:{}/", serverPort);
        log.info("Austin start succeeded, Swagger Url >> http://127.0.0.1:{}/swagger-ui/index.html", serverPort);
    }
}

package com.java3y.austin;

import com.java3y.austin.pojo.SmsParam;
import com.java3y.austin.script.TencentSmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;


@SpringBootApplication
@RestController
public class AustinApplication {

    @Autowired
    private TencentSmsScript tencentSmsScript;
    public static void main(String[] args) {
        SpringApplication.run(AustinApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {

        SmsParam smsParam = SmsParam.builder()
                .phones(new HashSet<>(Arrays.asList("//")))
                .content("3333")
                .build();

        return tencentSmsScript.send(smsParam);

    }

}

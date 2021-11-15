package com.java3y.austin;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.dao.MessageTemplateDao;
import com.java3y.austin.domain.MessageTemplate;
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
public class AustinApplication {
    public static void main(String[] args) {
        SpringApplication.run(AustinApplication.class, args);
    }
}

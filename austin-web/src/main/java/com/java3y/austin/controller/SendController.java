package com.java3y.austin.controller;

import com.alibaba.fastjson.JSON;
import com.java3y.austin.handler.SmsHandler;
import com.java3y.austin.pojo.SmsParam;
import com.java3y.austin.pojo.TaskInfo;
import com.java3y.austin.script.TencentSmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;

@RestController
public class SendController {


    @Autowired
    private SmsHandler smsHandler;

    /**
     * 测试发送短信
     * @param phone 手机号
     * @return
     */
    @GetMapping("/sendSms")
    public boolean sendSms(String phone,String content,Long messageTemplateId ) {

        TaskInfo taskInfo = TaskInfo.builder().receiver(new HashSet<>(Arrays.asList(phone)))
                .content(content).messageTemplateId(messageTemplateId).build();

        return smsHandler.doHandler(taskInfo);


    }

}

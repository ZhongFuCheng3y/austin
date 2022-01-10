package com.java3y.austin.controller;

import com.java3y.austin.domain.SendRequest;
import com.java3y.austin.domain.SendResponse;
import com.java3y.austin.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 三歪
 */

@RestController
public class SendController {

    @Autowired
    private SendService sendService;


    /**
     * 发送消息接口
     *
     * @return
     */
    @PostMapping("/send")
    public SendResponse send(@RequestBody SendRequest sendRequest) {
        return sendService.send(sendRequest);
    }
}

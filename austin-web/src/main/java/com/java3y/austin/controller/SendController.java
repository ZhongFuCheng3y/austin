package com.java3y.austin.controller;

import com.java3y.austin.domain.MessageParam;
import com.java3y.austin.domain.SendRequest;
import com.java3y.austin.domain.SendResponse;
import com.java3y.austin.enums.BusinessCode;
import com.java3y.austin.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 三歪
 */

@RestController
public class SendController {

    @Autowired
    private SendService sendService;

    @GetMapping("/sendSmsV2")
    public SendResponse sendSmsV2(String phone) {

        /**
         *
         * messageTemplate Id 为1 的模板内容
         * {"auditStatus":10,"auditor":"yyyyyyz","created":1636978066,"creator":"yyyyc","deduplicationTime":1,"expectPushTime":"0","flowId":"yyyy","id":1,"idType":30,"isDeleted":0,"isNightShield":0,"msgContent":"{\"content\":\"{$contentValue}\"}","msgStatus":10,"msgType":10,"name":"test短信","proposer":"yyyy22","sendAccount":66,"sendChannel":30,"team":"yyyt","templateType":10,"updated":1636978066,"updator":"yyyyu"}
         *
         */

        // 文案参数
        Map<String, String> variables = new HashMap<>(8);
        variables.put("contentValue", "6666");

        MessageParam messageParam = new MessageParam().setReceiver(phone).setVariables(variables);

        // ID为1的消息模板
        SendRequest sendRequest = new SendRequest().setCode(BusinessCode.COMMON_SEND.getCode())
                .setMessageTemplateId(1L)
                .setMessageParam(messageParam);

        SendResponse response = sendService.send(sendRequest);

        return response;
    }

}

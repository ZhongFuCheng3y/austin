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

    /**
     * 发送
     *
     * @param phone
     * @return
     */
    @GetMapping("/sendSmsTest")
    public SendResponse sendSmsTest(String phone, Long templateId) {

        /**
         *
         * messageTemplate Id 为1 的模板内容（普通短信）
         * {"auditStatus":10,"auditor":"yyyyyyz","created":1636978066,"creator":"yyyyc","deduplicationTime":1,"expectPushTime":"0","flowId":"yyyy","id":1,"idType":30,"isDeleted":0,"isNightShield":0,"msgContent":"{\"content\":\"{$contentValue}\"}","msgStatus":10,"msgType":10,"name":"test短信","proposer":"yyyy22","sendAccount":66,"sendChannel":30,"team":"yyyt","templateType":10,"updated":1636978066,"updator":"yyyyu"}
         *
         * messageTemplate Id 为2 的模板内容（营销短信）
         * {"auditStatus":10,"auditor":"yyyyyyz","created":1636978066,"creator":"yyyyc","deduplicationTime":1,"expectPushTime":"0","flowId":"yyyy","id":1,"idType":30,"isDeleted":0,"isNightShield":0,"msgContent":"{\"content\":\"{$contentValue}\"}","msgStatus":10,"msgType":20,"name":"test短信","proposer":"yyyy22","sendAccount":66,"sendChannel":30,"team":"yyyt","templateType":10,"updated":1636978066,"updator":"yyyyu"}
         */

        // 文案参数
        Map<String, String> variables = new HashMap<>(8);
        variables.put("contentValue", "6666");
        MessageParam messageParam = new MessageParam().setReceiver(phone).setVariables(variables);


        SendRequest sendRequest = new SendRequest().setCode(BusinessCode.COMMON_SEND.getCode())
                .setMessageTemplateId(templateId)
                .setMessageParam(messageParam);

        return sendService.send(sendRequest);

    }


}

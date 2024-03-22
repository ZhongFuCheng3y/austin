package com.java3y.austin.web.controller;


import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.service.api.domain.BatchSendRequest;
import com.java3y.austin.service.api.domain.SendRequest;
import com.java3y.austin.service.api.domain.SendResponse;
import com.java3y.austin.service.api.service.RecallService;
import com.java3y.austin.service.api.service.SendService;
import com.java3y.austin.web.annotation.AustinAspect;
import com.java3y.austin.web.exception.CommonException;
import com.java3y.austin.web.interceptor.TokenInterceptor;
import com.java3y.austin.web.service.MessageTemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 三歪
 */

@Api(tags = {"发送消息"})
@RestController
@AustinAspect
public class SendController {

    @Autowired
    private SendService sendService;

    @Autowired
    private RecallService recallService;

    @Autowired
    private MessageTemplateService messageTemplateService;


    /**
     * 单个文案下发相同的人
     *
     * @param sendRequest
     * @return
     */
    @ApiOperation(value = "下发接口", notes = "多渠道多类型下发消息，目前支持邮件和短信，类型支持：验证码、通知类、营销类。")
    @PostMapping("/send")
    public SendResponse send(@RequestBody SendRequest sendRequest) {
        if(!messageTemplateService.hasPermission(Collections.singleton(sendRequest.getMessageTemplateId()), TokenInterceptor.CREAT_THREAD_LOCAL.get())) {
            throw new CommonException(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg());
        }
        return sendService.send(sendRequest);
    }

    /**
     * 不同文案下发到不同的人
     *
     * @param batchSendRequest
     * @return
     */
    @ApiOperation(value = "batch下发接口", notes = "多渠道多类型下发消息，目前支持邮件和短信，类型支持：验证码、通知类、营销类。")
    @PostMapping("/batchSend")
    public SendResponse batchSend(@RequestBody BatchSendRequest batchSendRequest) {
        if(!messageTemplateService.hasPermission(Collections.singleton(batchSendRequest.getMessageTemplateId()), TokenInterceptor.CREAT_THREAD_LOCAL.get())) {
            throw new CommonException(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg());
        }
        return sendService.batchSend(batchSendRequest);
    }

    /**
     * 优先根据messageId撤回消息，如果messageId不存在则根据模板id撤回
     *
     * @param sendRequest
     * @return
     */
    @ApiOperation(value = "撤回消息接口", notes = "优先根据messageId撤回消息，如果messageId不存在则根据模板id撤回")
    @PostMapping("/recall")
    public SendResponse recall(@RequestBody SendRequest sendRequest) {
        if(!messageTemplateService.hasPermission(Collections.singleton(sendRequest.getMessageTemplateId()), TokenInterceptor.CREAT_THREAD_LOCAL.get())) {
            throw new CommonException(RespStatusEnum.CLIENT_BAD_PARAMETERS.getCode(), RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg());
        }
        return recallService.recall(sendRequest);
    }
}

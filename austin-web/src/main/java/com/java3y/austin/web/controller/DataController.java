package com.java3y.austin.web.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.java3y.austin.web.annotation.AustinAspect;
import com.java3y.austin.web.annotation.AustinResult;
import com.java3y.austin.web.service.DataService;
import com.java3y.austin.web.vo.DataParam;
import com.java3y.austin.web.vo.amis.EchartsVo;
import com.java3y.austin.web.vo.amis.SmsTimeLineVo;
import com.java3y.austin.web.vo.amis.UserTimeLineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 获取数据接口（全链路追踪)
 *
 * @author 3y
 */
@Slf4j
@AustinAspect
@AustinResult
@RestController
@RequestMapping("/trace")
@Api("获取数据接口（全链路追踪)")
public class DataController {
    @Autowired
    private DataService dataService;

    @PostMapping("/message")
    @ApiOperation("/获取【72小时】发送消息的全链路数据")
    public UserTimeLineVo getMessageData(@RequestBody DataParam dataParam) {
        if (Objects.isNull(dataParam) || CharSequenceUtil.isBlank(dataParam.getMessageId())) {
            return UserTimeLineVo.builder().items(new ArrayList<>()).build();
        }
        return dataService.getTraceMessageInfo(dataParam.getMessageId());
    }

    @PostMapping("/user")
    @ApiOperation("/获取【当天】用户接收消息的全链路数据")
    public UserTimeLineVo getUserData(@RequestBody DataParam dataParam) {
        if (Objects.isNull(dataParam) || CharSequenceUtil.isBlank(dataParam.getReceiver())) {
            return UserTimeLineVo.builder().items(new ArrayList<>()).build();
        }
        return dataService.getTraceUserInfo(dataParam.getReceiver());
    }

    @PostMapping("/messageTemplate")
    @ApiOperation("/获取消息模板全链路数据")
    public EchartsVo getMessageTemplateData(@RequestBody DataParam dataParam) {
        EchartsVo echartsVo = EchartsVo.builder().build();
        if (CharSequenceUtil.isNotBlank(dataParam.getBusinessId())) {
            echartsVo = dataService.getTraceMessageTemplateInfo(dataParam.getBusinessId());
        }
        return echartsVo;
    }

    @PostMapping("/sms")
    @ApiOperation("/获取短信下发数据")
    public SmsTimeLineVo getSmsData(@RequestBody DataParam dataParam) {
        if (Objects.isNull(dataParam) || Objects.isNull(dataParam.getDateTime()) || CharSequenceUtil.isBlank(dataParam.getReceiver())) {
            return SmsTimeLineVo.builder().items(Lists.newArrayList()).build();
        }
        return dataService.getTraceSmsInfo(dataParam);
    }

}

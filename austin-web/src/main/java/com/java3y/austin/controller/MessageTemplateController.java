package com.java3y.austin.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.dao.MessageTemplateDao;
import com.java3y.austin.domain.MessageTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * MessageTemplateController
 * 插入模板测试类
 * @author 3y
 */
@RestController
@RequestMapping("/Message")
@Api("发送消息")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * test insert
     */
    @GetMapping("/insert")
    @ApiOperation("/插入数据")
    public String insert() {

        MessageTemplate messageTemplate = MessageTemplate.builder()
                .name("test邮件")
                .auditStatus(10)
                .flowId("yyyy")
                .msgStatus(10)
                .idType(50)
                .sendChannel(40)
                .templateType(20)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("{\"content\":\"{$contentValue}\",\"title\":\"{$title}\"}")
                .sendAccount(66)
                .creator("yyyyc")
                .updator("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .deduplicationTime(1)
                .isNightShield(0)
                .build();

        MessageTemplate info = messageTemplateDao.save(messageTemplate);

        return JSON.toJSONString(info);

    }

    /**
     * test query
     */
    @GetMapping("/query")
    @ApiOperation("/查找数据")
    public String query() {
        Iterable<MessageTemplate> all = messageTemplateDao.findAll();
        for (MessageTemplate messageTemplate : all) {
            return JSON.toJSONString(messageTemplate);
        }
        return null;
    }
}

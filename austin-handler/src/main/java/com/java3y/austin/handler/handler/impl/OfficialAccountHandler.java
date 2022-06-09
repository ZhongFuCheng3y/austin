package com.java3y.austin.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.model.OfficialAccountsContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.domain.wechat.WeChatOfficialParam;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.handler.wechat.OfficialAccountService;
import com.java3y.austin.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zyg
 * 微信服务号推送处理
 */
@Component
@Slf4j
public class OfficialAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private OfficialAccountService officialAccountService;

    public OfficialAccountHandler() {
        channelCode = ChannelType.OFFICIAL_ACCOUNT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        // 构建微信模板消息
        OfficialAccountsContentModel contentModel = (OfficialAccountsContentModel) taskInfo.getContentModel();
        WeChatOfficialParam officialParam = WeChatOfficialParam.builder()
                .openIds(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .data(contentModel.getMap())
                .build();

        // 微信模板消息需要记录响应结果
        try {
            List<String> messageIds = officialAccountService.send(officialParam);
            log.info("OfficialAccountHandler#handler successfully messageIds:{}", messageIds);
            return true;
        } catch (Exception e) {
            log.error("OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}


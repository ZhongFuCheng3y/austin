package com.java3y.austin.handler.handler.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.model.MiniProgramContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sunql
 * 微信小程序发送订阅消息
 */
@Component
@Slf4j
public class MiniProgramAccountHandler extends BaseHandler implements Handler {
    @Autowired
    private AccountUtils accountUtils;

    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();
        WxMaService wxMaService = accountUtils.getAccountById(taskInfo.getSendAccount(), WxMaService.class);
        List<WxMaSubscribeMessage> wxMaSubscribeMessages = assembleReq(taskInfo.getReceiver(), contentModel);
        for (WxMaSubscribeMessage message : wxMaSubscribeMessages) {
            try {
                wxMaService.getSubscribeService().sendSubscribeMsg(message);
            } catch (Exception e) {
                log.info("MiniProgramAccountHandler#handler fail! param:{},e:{}", JSON.toJSONString(taskInfo), Throwables.getStackTraceAsString(e));
            }
        }
        return true;
    }

    /**
     * 组装发送模板信息参数
     */
    private List<WxMaSubscribeMessage> assembleReq(Set<String> receiver, MiniProgramContentModel contentModel) {
        List<WxMaSubscribeMessage> messageList = new ArrayList<>(receiver.size());
        for (String openId : receiver) {
            WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .data(getWxMaTemplateData(contentModel.getMiniProgramParam()))
                    .templateId(contentModel.getTemplateId())
                    .page(contentModel.getPage())
                    .build();
            messageList.add(subscribeMessage);
        }
        return messageList;
    }

    /**
     * 构建订阅消息参数
     *
     * @returnp
     */
    private List<WxMaSubscribeMessage.MsgData> getWxMaTemplateData(Map<String, String> data) {
        List<WxMaSubscribeMessage.MsgData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return templateDataList;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {

    }
}


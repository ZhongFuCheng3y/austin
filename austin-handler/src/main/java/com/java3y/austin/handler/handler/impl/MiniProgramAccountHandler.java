package com.java3y.austin.handler.handler.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.domain.AnchorInfo;
import com.java3y.austin.common.domain.RecallTaskInfo;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.model.MiniProgramContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.support.utils.AccountUtils;
import com.java3y.austin.support.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sunql
 * 微信小程序发送订阅消息
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/deleteMessageTemplate.html
 */
@Component
@Slf4j
public class MiniProgramAccountHandler extends BaseHandler{
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private LogUtils logUtils;

    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();
            WxMaService wxMaService = accountUtils.getAccountById(taskInfo.getSendAccount(), WxMaService.class);

            WxMaSubscribeMessage message = assembleReq(taskInfo.getReceiver(), contentModel);
            wxMaService.getSubscribeService().sendSubscribeMsg(message);
            return true;
        } catch (WxErrorException e) {
            logUtils.print(AnchorInfo.builder().bizId(taskInfo.getBizId()).messageId(taskInfo.getMessageId()).businessId(taskInfo.getBusinessId())
                    .ids(taskInfo.getReceiver()).state(e.getError().getErrorCode()).build());
        } catch (Exception e) {
            log.error("MiniProgramAccountHandler#handler fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }

        return false;
    }

    /**
     * 组装发送模板信息参数
     * @param receiver 接收信息者
     * @param contentModel 消息参数信息
     * @return
     */
    private WxMaSubscribeMessage assembleReq(Set<String> receiver, MiniProgramContentModel contentModel) {
        return WxMaSubscribeMessage.builder()
                .toUser(CollUtil.getFirst(receiver.iterator()))
                .data(getWxMaTemplateData(contentModel.getMiniProgramParam()))
                .templateId(contentModel.getTemplateId())
                .page(contentModel.getPage())
                .build();
    }

    /**
     * 构建订阅消息参数
     * @param data 模板参数
     * @returnp
     */
    private List<WxMaSubscribeMessage.MsgData> getWxMaTemplateData(Map<String, String> data) {
        List<WxMaSubscribeMessage.MsgData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return templateDataList;
    }


    /**
     * 微信小程序发送订阅消息 不支持撤回
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/deleteMessageTemplate.html
     * @param recallTaskInfo
     */
    @Override
    public void recall(RecallTaskInfo recallTaskInfo) {

    }
}


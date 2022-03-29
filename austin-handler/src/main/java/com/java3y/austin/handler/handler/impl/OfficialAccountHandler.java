package com.java3y.austin.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.model.OfficialAccountsContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.handler.script.OfficialAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        List<WxMpTemplateMessage> mpTemplateMessages = buildTemplateMsg(taskInfo);
        // 微信模板消息需要记录响应结果
        try {
            List<String> messageIds = officialAccountService.send(mpTemplateMessages);
            log.info("OfficialAccountHandler#handler successfully messageIds:{}", messageIds);
            return true;
        } catch (Exception e) {
            log.error("OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /**
     * 通过taskInfo构建微信模板消息
     *
     * @param taskInfo
     * @return
     */
    private List<WxMpTemplateMessage> buildTemplateMsg(TaskInfo taskInfo) {
        // 需是关注公众号的用户的OpenId
        Set<String> receiver = taskInfo.getReceiver();
        Long messageTemplateId = taskInfo.getMessageTemplateId();
        // 微信模板消息可以关联到系统业务，通过接口查询。
        String templateId = getRealWxMpTemplateId(messageTemplateId);
        List<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receiver.size());
        OfficialAccountsContentModel contentModel = (OfficialAccountsContentModel) taskInfo.getContentModel();
        String url = contentModel.getUrl();
        Map<String, String> param = contentModel.getMap();

        // 构建微信模板消息
        for (String openId : receiver) {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(templateId)
                    .url(url)
                    .build();
            // WxMpTemplateData 对应模板消息 键 -- 值 -- color
            param.forEach((k, v) -> templateMessage.addData(new WxMpTemplateData(k, v)));
            wxMpTemplateMessages.add(templateMessage);
        }
        return wxMpTemplateMessages;
    }

    /**
     * 根据模板id获取真实的模板id
     *
     * @param messageTemplateId 系统业务模板id
     * @return
     */
    private String getRealWxMpTemplateId(Long messageTemplateId) {
        return String.valueOf(messageTemplateId);
    }
}


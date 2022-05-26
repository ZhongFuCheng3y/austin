package com.java3y.austin.handler.wechat.impl;

import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.WeChatOfficialAccount;
import com.java3y.austin.handler.domain.wechat.WeChatOfficialParam;
import com.java3y.austin.handler.wechat.OfficialAccountService;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zyg
 */
@Service
@Slf4j
public class OfficialAccountServiceImpl implements OfficialAccountService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<String> send(WeChatOfficialParam officialParam) throws Exception {
        WeChatOfficialAccount officialAccount = accountUtils.getAccount(officialParam.getSendAccount(), SendAccountConstant.WECHAT_OFFICIAL_ACCOUNT_KEY, SendAccountConstant.WECHAT_OFFICIAL__PREFIX, WeChatOfficialAccount.class);
        WxMpService wxMpService = initService(officialAccount);
        List<WxMpTemplateMessage> messages = assembleReq(officialParam, officialAccount);
        List<String> messageIds = new ArrayList<>(messages.size());
        for (WxMpTemplateMessage wxMpTemplateMessage : messages) {
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            messageIds.add(msgId);
        }
        return messageIds;
    }

    /**
     * 组装发送模板信息参数
     */
    private List<WxMpTemplateMessage> assembleReq(WeChatOfficialParam officialParam, WeChatOfficialAccount officialAccount) {
        Set<String> receiver = officialParam.getOpenIds();
        List<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receiver.size());

        // 构建微信模板消息
        for (String openId : receiver) {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(officialAccount.getTemplateId())
                    .url(officialAccount.getUrl())
                    .data(getWxMpTemplateData(officialParam.getData()))
                    .miniProgram(new WxMpTemplateMessage.MiniProgram(officialAccount.getMiniProgramId(), officialAccount.getPath(), false))
                    .build();
            wxMpTemplateMessages.add(templateMessage);
        }
        return wxMpTemplateMessages;
    }

    /**
     * 构建模板消息参数
     *
     * @return
     */
    private List<WxMpTemplateData> getWxMpTemplateData(Map<String, String> data) {
        List<WxMpTemplateData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMpTemplateData(k, v)));
        return templateDataList;
    }

    /**
     * 初始化微信服务号
     *
     * @return
     */
    public WxMpService initService(WeChatOfficialAccount officialAccount) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(officialAccount.getAppId());
        config.setSecret(officialAccount.getSecret());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}

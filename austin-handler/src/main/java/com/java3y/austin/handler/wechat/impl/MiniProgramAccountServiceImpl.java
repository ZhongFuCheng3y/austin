package com.java3y.austin.handler.wechat.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.WeChatMiniProgramAccount;
import com.java3y.austin.handler.domain.wechat.WeChatMiniProgramParam;
import com.java3y.austin.handler.wechat.MiniProgramAccountService;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sunql
 * @date 2022年05月06日 16:41
 */
@Service
@Slf4j
public class MiniProgramAccountServiceImpl implements MiniProgramAccountService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public void send(WeChatMiniProgramParam miniProgramParam) throws Exception {
        WeChatMiniProgramAccount miniProgramAccount = accountUtils.getAccount(miniProgramParam.getSendAccount(),
                SendAccountConstant.WECHAT_MINI_PROGRAM_ACCOUNT_KEY,
                SendAccountConstant.WECHAT_MINI_PROGRAM_PREFIX,
                WeChatMiniProgramAccount.class);

        WxMaSubscribeService wxMaSubscribeService = initService(miniProgramAccount);
        List<WxMaSubscribeMessage> subscribeMessageList = assembleReq(miniProgramParam, miniProgramAccount);
        for (WxMaSubscribeMessage subscribeMessage : subscribeMessageList) {
            wxMaSubscribeService.sendSubscribeMsg(subscribeMessage);
        }
    }

    /**
     * 组装发送模板信息参数
     */
    private List<WxMaSubscribeMessage> assembleReq(WeChatMiniProgramParam miniProgramParam, WeChatMiniProgramAccount miniProgramAccount) {
        Set<String> receiver = miniProgramParam.getOpenIds();
        List<WxMaSubscribeMessage> messageList = new ArrayList<>(receiver.size());

        // 构建微信小程序订阅消息
        for (String openId : receiver) {
            WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .data(getWxMTemplateData(miniProgramParam.getData()))
                    .miniprogramState(miniProgramAccount.getMiniProgramState())
                    .templateId(miniProgramAccount.getTemplateId())
                    .page(miniProgramAccount.getPage())
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
    private List<WxMaSubscribeMessage.MsgData> getWxMTemplateData(Map<String, String> data) {
        List<WxMaSubscribeMessage.MsgData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return templateDataList;
    }

    /**
     * 初始化微信小程序
     *
     * @return
     */
    private WxMaSubscribeServiceImpl initService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(miniProgramAccount.getAppId());
        wxMaConfig.setSecret(miniProgramAccount.getAppSecret());
        wxMaService.setWxMaConfig(wxMaConfig);
        return new WxMaSubscribeServiceImpl(wxMaService);
    }
}

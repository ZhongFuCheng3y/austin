package com.java3y.austin.handler.script.impl;

import com.java3y.austin.handler.script.EnterpriseWeChatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.WxCpConfigStorage;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 * @date 2022/3/15
 */
@Slf4j
@Service
public class EnterpriseWeChatServiceImpl implements EnterpriseWeChatService {

    @Override
    public WxCpMessageSendResult send(WxCpMessage wxCpMessage) throws WxErrorException {
        WxCpMessageServiceImpl messageService = new WxCpMessageServiceImpl(initService());

        return messageService.send(wxCpMessage);
    }

    private WxCpService initService() {
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(initConfig());
        return wxCpService;
    }

    private WxCpConfigStorage initConfig() {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId("");
        config.setCorpSecret("");
        config.setAgentId(1);
        config.setToken("");
        config.setAesKey("");
        return config;
    }
}

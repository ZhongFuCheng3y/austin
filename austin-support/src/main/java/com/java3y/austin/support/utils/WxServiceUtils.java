package com.java3y.austin.support.utils;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.dto.account.WeChatMiniProgramAccount;
import com.java3y.austin.common.dto.account.WeChatOfficialAccount;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.support.dao.ChannelAccountDao;
import com.java3y.austin.support.domain.ChannelAccount;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信服务号/微信小程序 工具类
 *
 * @author 3y
 */
@Component
@Slf4j
@Data
public class WxServiceUtils {

    private Map<Long, WxMpService> officialAccountServiceMap = new HashMap<>();
    private Map<Long, WxMaSubscribeService> miniProgramServiceMap = new HashMap<>();

    private Map<Long, WeChatOfficialAccount> officialAccountHashMap = new HashMap<>();
    private Map<Long, WeChatMiniProgramAccount> miniProgramHashMap = new HashMap<>();

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @PostConstruct
    public void init() {
        initOfficialAccount();
        initMiniProgram();
    }


    private void initMiniProgram() {
        List<ChannelAccount> miniProgram = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.MINI_PROGRAM.getCode());
        for (ChannelAccount channelAccount : miniProgram) {
            WeChatMiniProgramAccount weChatMiniProgramAccount = JSON.parseObject(channelAccount.getAccountConfig(), WeChatMiniProgramAccount.class);
            miniProgramServiceMap.put(channelAccount.getId(), initMiniProgramService(weChatMiniProgramAccount));
            miniProgramHashMap.put(channelAccount.getId(), weChatMiniProgramAccount);
        }
    }

    private void initOfficialAccount() {
        List<ChannelAccount> officialAccountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.OFFICIAL_ACCOUNT.getCode());
        for (ChannelAccount channelAccount : officialAccountList) {
            WeChatOfficialAccount weChatOfficialAccount = JSON.parseObject(channelAccount.getAccountConfig(), WeChatOfficialAccount.class);
            officialAccountServiceMap.put(channelAccount.getId(), initOfficialAccountService(weChatOfficialAccount));
            officialAccountHashMap.put(channelAccount.getId(), weChatOfficialAccount);
        }
    }

    /**
     * 初始化微信服务号
     *
     * @return
     */
    public WxMpService initOfficialAccountService(WeChatOfficialAccount officialAccount) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(officialAccount.getAppId());
        config.setSecret(officialAccount.getSecret());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    /**
     * 初始化微信小程序
     *
     * @return
     */
    private WxMaSubscribeServiceImpl initMiniProgramService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(miniProgramAccount.getAppId());
        wxMaConfig.setSecret(miniProgramAccount.getAppSecret());
        wxMaService.setWxMaConfig(wxMaConfig);
        return new WxMaSubscribeServiceImpl(wxMaService);
    }
}

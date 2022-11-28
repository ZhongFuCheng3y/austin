package com.java3y.austin.support.utils;


import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.dto.account.WeChatOfficialAccount;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.support.dao.ChannelAccountDao;
import com.java3y.austin.support.domain.ChannelAccount;
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
public class WxServiceUtils {

    public static Map<Long, WxMpService> wxMpServiceMap = new HashMap<>();
    public static Map<Long, WeChatOfficialAccount> accountHashMap = new HashMap<>();

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @PostConstruct
    public void init() {
        List<ChannelAccount> officialAccountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.OFFICIAL_ACCOUNT.getCode());
        for (ChannelAccount channelAccount : officialAccountList) {
            WeChatOfficialAccount weChatOfficialAccount = JSON.parseObject(channelAccount.getAccountConfig(), WeChatOfficialAccount.class);
            wxMpServiceMap.put(channelAccount.getId(), initService(weChatOfficialAccount));
            accountHashMap.put(channelAccount.getId(), weChatOfficialAccount);
        }
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

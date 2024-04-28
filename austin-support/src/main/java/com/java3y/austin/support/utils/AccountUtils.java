package com.java3y.austin.support.utils;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.dto.account.WeChatMiniProgramAccount;
import com.java3y.austin.common.dto.account.WeChatOfficialAccount;
import com.java3y.austin.common.dto.account.sms.SmsAccount;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.support.dao.ChannelAccountDao;
import com.java3y.austin.support.domain.ChannelAccount;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 获取账号信息工具类
 *
 * @author 3y
 */
@Slf4j
@Configuration
public class AccountUtils {

    @Autowired
    private ChannelAccountDao channelAccountDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 消息的小程序/微信服务号账号
     */
    private ConcurrentMap<ChannelAccount, WxMpService> officialAccountServiceMap = new ConcurrentHashMap<>();
    private ConcurrentMap<ChannelAccount, WxMaService> miniProgramServiceMap = new ConcurrentHashMap<>();

    @Bean
    public RedisTemplateWxRedisOps redisTemplateWxRedisOps() {
        return new RedisTemplateWxRedisOps(this.redisTemplate);
    }

    /**
     * 微信小程序：返回 WxMaService
     * 微信服务号：返回 WxMpService
     * 其他渠道：返回XXXAccount账号对象
     *
     * @param sendAccountId
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getAccountById(Integer sendAccountId, Class<T> clazz) {
        try {
            Optional<ChannelAccount> optionalChannelAccount = channelAccountDao.findById(Long.valueOf(sendAccountId));
            if (optionalChannelAccount.isPresent()) {
                ChannelAccount channelAccount = optionalChannelAccount.get();
                if (clazz.equals(WxMaService.class)) {
                    return (T) ConcurrentHashMapUtils.computeIfAbsent(miniProgramServiceMap, channelAccount, account -> initMiniProgramService(JSON.parseObject(account.getAccountConfig(), WeChatMiniProgramAccount.class)));
                } else if (clazz.equals(WxMpService.class)) {
                    return (T) ConcurrentHashMapUtils.computeIfAbsent(officialAccountServiceMap, channelAccount, account -> initOfficialAccountService(JSON.parseObject(account.getAccountConfig(), WeChatOfficialAccount.class)));
                } else {
                    return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
                }
            }
        } catch (Exception e) {
            log.error("AccountUtils#getAccount fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    /**
     * 通过脚本名 匹配到对应的短信账号
     *
     * @param scriptName 脚本名
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getSmsAccountByScriptName(String scriptName, Class<T> clazz) {
        try {
            List<ChannelAccount> channelAccountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(CommonConstant.FALSE, ChannelType.SMS.getCode());
            for (ChannelAccount channelAccount : channelAccountList) {
                try {
                    SmsAccount smsAccount = JSON.parseObject(channelAccount.getAccountConfig(), SmsAccount.class);
                    if (smsAccount.getScriptName().equals(scriptName)) {
                        return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
                    }
                } catch (Exception e) {
                    log.error("AccountUtils#getSmsAccount parse fail! e:{},account:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(channelAccount));
                }
            }
        } catch (Exception e) {
            log.error("AccountUtils#getSmsAccount fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        log.error("AccountUtils#getSmsAccount not found!:{}", scriptName);
        return null;
    }

    /**
     * 初始化微信服务号
     * access_token 用redis存储
     *
     * @return
     */
    public WxMpService initOfficialAccountService(WeChatOfficialAccount officialAccount) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpRedisConfigImpl config = new WxMpRedisConfigImpl(redisTemplateWxRedisOps(), ChannelType.OFFICIAL_ACCOUNT.getAccessTokenPrefix());
        config.setAppId(officialAccount.getAppId());
        config.setSecret(officialAccount.getSecret());
        config.setToken(officialAccount.getToken());
        config.useStableAccessToken(true);
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    /**
     * 初始化微信小程序
     * access_token 用redis存储
     *
     * @return
     */
    private WxMaService initMiniProgramService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaRedisBetterConfigImpl config = new WxMaRedisBetterConfigImpl(redisTemplateWxRedisOps(), ChannelType.MINI_PROGRAM.getAccessTokenPrefix());
        config.setAppid(miniProgramAccount.getAppId());
        config.setSecret(miniProgramAccount.getAppSecret());
        config.useStableAccessToken(true);
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }


}

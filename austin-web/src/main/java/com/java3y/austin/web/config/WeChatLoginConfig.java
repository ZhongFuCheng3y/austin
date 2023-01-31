package com.java3y.austin.web.config;

import com.java3y.austin.common.constant.OfficialAccountParamConstant;
import com.java3y.austin.common.dto.account.WeChatOfficialAccount;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.Data;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.util.Map;


/**
 * 使用微信服务号作为登录的媒介
 * (测试环境 && 开启了配置才使用)
 *
 * @author 3y
 */
@Profile("test")
@Configuration("weChatLoginConfig")
@ConditionalOnProperty(name = "austin.login.official.account.enable", havingValue = "true")
@Data
public class WeChatLoginConfig {

    @Value("${austin.login.official.account.appId}")
    private String appId;
    @Value("${austin.login.official.account.secret}")
    private String secret;
    @Value("${austin.login.official.account.token}")
    private String token;

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 微信服务号 登录 相关对象
     */
    private WxMpService officialAccountLoginService;
    private WxMpDefaultConfigImpl config;
    private WxMpMessageRouter wxMpMessageRouter;

    @Autowired
    private Map<String, WxMpMessageHandler> wxMpMessageHandlers;


    @PostConstruct
    private void init() {
        WeChatOfficialAccount account = WeChatOfficialAccount.builder().appId(appId).secret(secret).token(token).build();
        officialAccountLoginService = accountUtils.initOfficialAccountService(account);
        initConfig();
        initRouter();
    }

    /**
     * 初始化路由器
     * 扫码、关注、取消关注
     */
    private void initRouter() {
        wxMpMessageRouter = new WxMpMessageRouter(officialAccountLoginService);
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(wxMpMessageHandlers.get(OfficialAccountParamConstant.SUBSCRIBE_HANDLER)).end();
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).handler(wxMpMessageHandlers.get(OfficialAccountParamConstant.SCAN_HANDLER)).end();
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(wxMpMessageHandlers.get(OfficialAccountParamConstant.UNSUBSCRIBE_HANDLER)).end();
    }

    /**
     * 初始化配置信息
     */
    private void initConfig() {
        config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setToken(token);
        config.setSecret(secret);
    }

}

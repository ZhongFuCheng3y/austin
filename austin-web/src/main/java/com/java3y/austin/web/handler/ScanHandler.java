package com.java3y.austin.web.handler;

import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 3y
 * 微信服务号扫码处理器
 */
@Component("scanHandler")
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String content = "您在" + (new DateTime().toString("yyyy-MM-dd HH:mm:ss")) + "通过微信扫码登录austin,感谢您的使用。";
        String openId = wxMessage.getFromUser();
        // 将场景值和用户信息存入redis
        //redisTemplate.opsForValue().set(wxMessage.getEventKey(), user, 2, TimeUnit.MINUTES);
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .content(content).build();
    }
}

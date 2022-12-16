package com.java3y.austin.web.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.OfficialAccountParamConstant;
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

@Component("unSubscribeHandler")
@Slf4j
public class UnSubscribeHandler implements WxMpMessageHandler {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String content = DateUtil.now() + StrUtil.COLON + wxMessage.getFromUser() + StrUtil.COLON + OfficialAccountParamConstant.UNSUBSCRIBE_HANDLER;
        try {
//            String eventKey = wxMessage.getEventKey().replaceAll(OfficialAccountParamConstant.QR_CODE_SCENE_PREFIX, CommonConstant.EMPTY_STRING);
//            String userInfo = redisTemplate.opsForValue().get(eventKey);
//            redisTemplate.opsForValue().set(eventKey, JSON.toJSONString(userInfo), 30, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("UnSubscribeHandler#handle fail:{}", Throwables.getStackTraceAsString(e));
        }
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .content(content).build();
    }
}

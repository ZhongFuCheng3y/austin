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

/**
 * @author 3y
 * 微信服务号扫码处理器
 */
@Component("scanHandler")
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 扫码事件
     *
     * @param wxMessage
     * @param context
     * @param wxMpService
     * @param sessionManager
     * @return
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        String content = DateUtil.now() + StrUtil.COLON + wxMessage.getFromUser() + StrUtil.COLON + OfficialAccountParamConstant.SCAN_HANDLER;
        try {
//            WxMpUser user = wxMpService.getUserService().userInfo(wxMessage.getFromUser());
//            redisTemplate.opsForValue().set(wxMessage.getEventKey(), JSON.toJSONString(user), 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("ScanHandler#handle fail:{}", Throwables.getStackTraceAsString(e));
        }
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .content(content).build();
    }

}

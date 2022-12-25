package com.java3y.austin.web.handler;

import com.java3y.austin.common.constant.OfficialAccountParamConstant;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 3y
 * 微信服务号扫码处理器
 */
@Component("scanHandler")
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    /**
     * 扫码事件 (暂不实现逻辑)
     *
     * @param wxMessage
     * @param context
     * @param wxMpService
     * @param sessionManager
     * @return
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .content(OfficialAccountParamConstant.SCAN_TIPS).build();
    }

}

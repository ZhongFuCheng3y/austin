package com.java3y.austin.handler.script;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;

/**
 * 企业微信推送
 *
 * @author 3y
 */
public interface EnterpriseWeChatService {

    /**
     * 发送消息（目前只支持userId/@all)
     *
     * @param wxCpMessage
     * @return
     * @throws WxErrorException
     */
    WxCpMessageSendResult send(WxCpMessage wxCpMessage) throws WxErrorException;
}

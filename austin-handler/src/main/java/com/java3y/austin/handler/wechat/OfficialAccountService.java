package com.java3y.austin.handler.wechat;

import com.java3y.austin.handler.domain.wechat.WeChatOfficialParam;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.List;

/**
 * @author zyg
 */
public interface OfficialAccountService {

    /**
     * 发送模板消息
     *
     * @param weChatOfficialParam 模板消息参数
     * @return
     * @throws Exception
     */
    List<String> send(WeChatOfficialParam weChatOfficialParam) throws Exception;

}

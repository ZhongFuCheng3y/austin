package com.java3y.austin.handler.script;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.List;

/**
 * @author zyg
 */
public interface OfficialAccountService {

    /**
     * 发送模板消息
     *
     * @param wxMpTemplateMessages 模板消息列表
     * @return
     * @throws Exception
     */
    List<String> send(List<WxMpTemplateMessage> wxMpTemplateMessages) throws Exception;

}

package com.java3y.austin.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 *
 * 模板消息参数
 * <p>
 * 参数示例：
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Template_Message_Interface.html
 * * @author zyg
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WechatOfficialAccount {

    /**
     * 服务号关注者的openId
     */
    private String openId;

    /**
     * 需要使用的模板信息Id
     */
    private String templateId;

    /**
     * 模板消息跳转的url
     */
    private String url;

    /**
     * 模板消息跳转小程序的appid
     */
    private String miniProgramId;

    /**
     * 模板消息跳转小程序的页面路径
     */
    private String path;

    /**
     * 模板消息的信息载体
     */
    private Map<String, String> map;
}

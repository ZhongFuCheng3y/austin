package com.java3y.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * 企业微信 应用消息
 * https://developer.work.weixin.qq.com/document/path/90372#%E6%8E%A5%E5%8F%A3%E5%AE%9A%E4%B9%89
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseWeChatContentModel extends ContentModel {

    /**
     * 下发企业微信消息的类型
     */
    private String sendType;

    /**
     * 文案
     */
    private String content;

    /**
     * 图片媒体文件id
     */
    private String mediaId;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 链接
     */
    private String url;

    /**
     * 按钮文案
     */
    private String btnTxt;

    /**
     * 图文消息
     * [{"title":"中秋节礼品领取","description":"今年中秋节公司有豪礼相送","url":"URL","picurl":"http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png","appid":"wx123123123123123","pagepath":"pages/index?userid=zhangsan&orderid=123123123"}]
     */
    private String articles;

    /**
     * 图文消息（mpnews）
     * [{"title":"Title","thumb_media_id":"MEDIA_ID","author":"Author","content_source_url":"URL","content":"Content","digest":"Digest description"}]
     */
    private String mpNewsArticle;


    /**
     * 小程序
     */
    private String appId;
    private String page;
    private Boolean emphasisFirstItem;
    private String contentItems;


    /**
     *  其他消息类型： https://developer.work.weixin.qq.com/document/path/90372#%E6%96%87%E6%9C%AC%E6%B6%88%E6%81%AF
     */

}

package com.java3y.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * 企业微信群 机器人
 * <p>
 * https://developer.work.weixin.qq.com/document/path/91770#%E6%96%87%E6%9C%AC%E7%B1%BB%E5%9E%8B
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseWeChatRobotContentModel extends ContentModel {

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 图片内容（base64编码前）的md5值
     */
    private String md5;

    /**
     * 图片内容的base64编码
     */
    private String base64;

    /**
     * 媒体Id
     */
    private String mediaId;

    /**
     * 图文消息：[{"title":"中秋节礼品领取","description":"今年中秋节公司有豪礼相送","url":"www.qq.com","picurl":"http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"}]
     */
    private String articles;

    /**
     * 图片路径
     */
    private String imagePath;
}

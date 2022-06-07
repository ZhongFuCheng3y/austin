package com.java3y.austin.common.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 * 钉钉 工作通知
 * <p>
 * https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingWorkContentModel extends ContentModel {

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 【文本消息】内容，【markdown消息】内容，【ActionCard消息】内容
     */
    private String content;

    /**
     * 【markdown消息】标题，【ActionCard消息】标题
     */
    private String title;

    /**
     * 【ActionCard消息】按钮布局
     */
    private String btnOrientation;

    /**
     * 【ActionCard消息】按钮的文案和跳转链接的json
     * [{"title":"一个按钮","action_url":"https://www.taobao.com"},{"title":"两个按钮","action_url":"https://www.tmall.com"}]
     */
    private String btns;


    /**
     * 【链接消息】点击消息跳转的URL，
     */
    private String url;


    /**
     * 图片、文件、语音消息 需要发送使用的素材ID字段
     */
    private String mediaId;

    /**
     * 语音时长
     */
    private String duration;

    /**
     * OA消息头
     * {"bgcolor":"FFBBBBBB","text":"头部标题"}
     */
    private String dingDingOaHead;

    /**
     * OA消息内容
     * {"title":"正文标题","form":[{"key":"姓名:","value":"张三"},{"key":"年龄:","value":"20"},{"key":"身高:","value":"1.8米"},{"key":"体重:","value":"130斤"},{"key":"学历:","value":"本科"},{"key":"爱好:","value":"打球、听音乐"}],"rich":{"num":"15.6","unit":"元"},"content":"大段文本大段文本大段文本大段文本大段文本大段文本","image":"@lADOADmaWMzazQKA","file_count":"3","author":"李四 "}
     */
    private String dingDingOaBody;

}

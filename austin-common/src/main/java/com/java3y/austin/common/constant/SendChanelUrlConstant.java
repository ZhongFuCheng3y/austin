package com.java3y.austin.common.constant;


/**
 * 发送渠道的URL常量，统一维护发送地址
 *
 * @author 3y
 */
public class SendChanelUrlConstant {
    /**
     * 个推相关的url
     */
    public static final String GE_TUI_BASE_URL = "https://restapi.getui.com/v2/";
    public static final String GE_TUI_SINGLE_PUSH_PATH = "/push/single/cid";
    public static final String GE_TUI_BATCH_PUSH_CREATE_TASK_PATH = "/push/list/message";
    public static final String GE_TUI_BATCH_PUSH_PATH = "/push/list/cid";
    public static final String GE_TUI_AUTH = "/auth";
    /**
     * 钉钉工作消息相关的url
     */
    public static final String DING_DING_SEND_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    public static final String DING_DING_RECALL_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/recall";
    public static final String DING_DING_PULL_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";
    public static final String DING_DING_UPLOAD_URL = "https://oapi.dingtalk.com/media/upload";
    public static final String DING_DING_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";
    /**
     * 企业微信机器人相关的url
     */
    public static final String ENTERPRISE_WE_CHAT_ROBOT_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?key=<KEY>&type=<TYPE>";
    /**
     * 支付宝小程序相关的url
     */
    public static final String ALI_MINI_PROGRAM_GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    /**
     * 微信小程序相关的url
     */
    public static final String WE_CHAT_MINI_PROGRAM_OPENID_SYNC = "https://api.weixin.qq.com/sns/jscode2session?appid=<APPID>&secret=<SECRET>&js_code=<CODE>&grant_type=authorization_code";

    private SendChanelUrlConstant() {

    }


}

package com.java3y.austin.common.constant;


/**
 * @author 3y
 * 微信服务号的参数常量
 */
public class OfficialAccountParamConstant {
    public static final String SIGNATURE = "signature";
    public static final String ECHO_STR = "echostr";
    public static final String NONCE = "nonce";
    public static final String TIMESTAMP = "timestamp";
    public static final String ENCRYPT_TYPE = "encrypt_type";
    public static final String RAW = "raw";
    public static final String AES = "aes";
    public static final String MSG_SIGNATURE = "msg_signature";
    /**
     * 处理器名
     */
    public static final String SCAN_HANDLER = "scanHandler";
    public static final String SUBSCRIBE_HANDLER = "subscribeHandler";
    public static final String UNSUBSCRIBE_HANDLER = "unSubscribeHandler";
    /**
     * 配置的beanName名
     */
    public static final String WE_CHAT_LOGIN_CONFIG = "weChatLoginConfig";
    /**
     * 二维码场景值的前缀
     */
    public static final String QR_CODE_SCENE_PREFIX = "qrscene_";
    /**
     * 扫码/关注后/取消关注后的服务号文案
     */
    public static final String SUBSCRIBE_TIPS = "项目群还有少量名额，添加我的微信 java3yyy  备注【项目】，我会拉入官方项目交流群";
    public static final String SCAN_TIPS = "咋又扫码啦？重新关注一波吧！";
    public static final String UNSUBSCRIBE_TIPS = "老乡别走！";
    private OfficialAccountParamConstant() {
    }


}

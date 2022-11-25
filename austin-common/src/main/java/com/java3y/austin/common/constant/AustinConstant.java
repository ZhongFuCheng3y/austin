package com.java3y.austin.common.constant;


/**
 * Austin常量信息
 *
 * @author 3y
 */
public class AustinConstant {

    /**
     * 跨域地址端口
     */
    public static final String ORIGIN_VALUE = "http://localhost:3000";

    /**
     * businessId默认的长度
     * 生成的逻辑：com.java3y.austin.support.utils.TaskInfoUtils#generateBusinessId(java.lang.Long, java.lang.Integer)
     */
    public final static Integer BUSINESS_ID_LENGTH = 16;

    /**
     * 接口限制 最多的人数
     */
    public static final Integer BATCH_RECEIVER_SIZE = 100;

    /**
     * 消息发送给全部人的标识
     * (企业微信 应用消息)
     * (钉钉自定义机器人)
     * (钉钉工作消息)
     */
    public static final String SEND_ALL = "@all";



}

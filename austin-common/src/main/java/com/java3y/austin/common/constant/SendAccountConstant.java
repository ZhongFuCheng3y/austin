package com.java3y.austin.common.constant;


/**
 * 发送账号的常量信息汇总
 * <p>
 * (读取apollo的key和前缀)
 * <p>
 * 约定：所有的账号都从10开始，步长为10
 *
 * @author 3y
 */
public class SendAccountConstant {

    /**
     * 账号约定：所有的账号都从10开始，步长为10
     */
    public static final Integer START = 10;
    public static final Integer STEP = 10;


    /**
     * 钉钉 工作应用消息 账号
     */
    public static final String DING_DING_WORK_NOTICE_ACCOUNT_KEY = "dingDingWorkNoticeAccount";
    public static final String DING_DING_WORK_NOTICE_PREFIX = "ding_ding_work_notice_";
    public static final String DING_DING_ACCESS_TOKEN_PREFIX = "ding_ding_access_token_";

    /**
     * 个推PUSH 消息账号
     */
    public static final String GE_TUI_ACCOUNT_KEY = "geTuiAccount";
    public static final String GE_TUI_ACCOUNT_PREFIX = "ge_tui_account_";
    public static final String GE_TUI_ACCESS_TOKEN_PREFIX = "ge_tui_access_token_";



    /**
     * 邮件 账号
     */
    public static final String EMAIL_ACCOUNT_KEY = "emailAccount";
    public static final String EMAIL_ACCOUNT_PREFIX = "email_";


    /**
     * 钉钉群自定义机器人 账号
     */
    public static final String DING_DING_ROBOT_ACCOUNT_KEY = "dingDingRobotAccount";
    public static final String DING_DING_ROBOT_PREFIX = "ding_ding_robot_";

    /**
     * 企业微信群机器人 账号
     */
    public static final String ENTERPRISE_WECHAT_ROBOT_ACCOUNT_KEY = "enterpriseWechatAccountRobot";
    public static final String ENTERPRISE_WECHAT_ROBOT_PREFIX = "enterprise_wechat_robot_";

    /**
     * 飞书群机器人 账号
     */
    public static final String FEI_SHU_ROBOT_ACCOUNT_KEY = "feiShuAccountRobot";
    public static final String FEI_SHU_ROBOT_PREFIX = "fei_shu_robot_";

    /**
     * 企业微信 应用消息 账号
     */
    public static final String ENTERPRISE_WECHAT_ACCOUNT_KEY = "enterpriseWechatAccount";
    public static final String ENTERPRISE_WECHAT_PREFIX = "enterprise_wechat_";

    /**
     * 微信服务号 应用消息 账号
     */
    public static final String WECHAT_OFFICIAL_ACCOUNT_KEY = "officialAccount";
    public static final String WECHAT_OFFICIAL__PREFIX = "official_";

    /**
     * 微信小程序 应用消息 账号
     */
    public static final String WECHAT_MINI_PROGRAM_ACCOUNT_KEY = "miniProgramAccount";
    public static final String WECHAT_MINI_PROGRAM_PREFIX = "mini_program_";

    /**
     * 短信 账号
     */
    public static final String SMS_ACCOUNT_KEY = "smsAccount";
    public static final String SMS_PREFIX = "sms_";


    /**
     * 短信账号code
     */
    public static final Integer TENCENT_SMS_CODE = 10;
    public static final Integer YUN_PIAN_SMS_CODE = 20;


}

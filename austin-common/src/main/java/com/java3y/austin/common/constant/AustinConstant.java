package com.java3y.austin.common.constant;


/**
 * Austin常量信息
 *
 * @author 3y
 */
public class AustinConstant {
    /**
     * businessId默认的长度
     * 生成的逻辑：com.java3y.austin.support.utils.TaskInfoUtils#generateBusinessId(java.lang.Long, java.lang.Integer)
     */
    public static final Integer BUSINESS_ID_LENGTH = 16;
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
    /**
     * 链路追踪缓存的key标识
     */
    public static final String CACHE_KEY_PREFIX = "Austin";
    public static final String MESSAGE_ID = "MessageId";
    /**
     * 消息模板常量；
     * 如果新建模板/账号时，没传入则用该常量
     */
    public static final String DEFAULT_CREATOR = "Java3y";
    public static final String DEFAULT_UPDATOR = "Java3y";
    public static final String DEFAULT_TEAM = "Java3y公众号";
    public static final String DEFAULT_AUDITOR = "Java3y";
    /**
     * 项目打印常量
     */
    public static final String PROJECT_NAME = " :: Austin :: ";
    public static final String PROJECT_BANNER = "\n" +
            " .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .-----------------.\n" +
            "| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n" +
            "| |      __      | || | _____  _____ | || |    _______   | || |  _________   | || |     _____    | || | ____  _____  | |\n" +
            "| |     /  \\     | || ||_   _||_   _|| || |   /  ___  |  | || | |  _   _  |  | || |    |_   _|   | || ||_   \\|_   _| | |\n" +
            "| |    / /\\ \\    | || |  | |    | |  | || |  |  (__ \\_|  | || | |_/ | | \\_|  | || |      | |     | || |  |   \\ | |   | |\n" +
            "| |   / ____ \\   | || |  | '    ' |  | || |   '.___`-.   | || |     | |      | || |      | |     | || |  | |\\ \\| |   | |\n" +
            "| | _/ /    \\ \\_ | || |   \\ `--' /   | || |  |`\\____) |  | || |    _| |_     | || |     _| |_    | || | _| |_\\   |_  | |\n" +
            "| ||____|  |____|| || |    `.__.'    | || |  |_______.'  | || |   |_____|    | || |    |_____|   | || ||_____|\\____| | |\n" +
            "| |              | || |              | || |              | || |              | || |              | || |              | |\n" +
            "| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |\n" +
            " '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------' \n";
    private AustinConstant() {

    }


}

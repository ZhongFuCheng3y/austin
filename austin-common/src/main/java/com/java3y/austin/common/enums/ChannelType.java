package com.java3y.austin.common.enums;


import com.java3y.austin.common.dto.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;

/**
 * 发送渠道类型枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType implements PowerfulEnum {


    /**
     * IM(站内信)  -- 未实现该渠道
     */
    IM(10, "IM(站内信)", ImContentModel.class, "im", null, null),
    /**
     * push(通知栏) --安卓 已接入 个推
     */
    PUSH(20, "push(通知栏)", PushContentModel.class, "push", "ge_tui_access_token_", 3600 * 24L),
    /**
     * sms(短信)  -- 腾讯云、云片
     */
    SMS(30, "sms(短信)", SmsContentModel.class, "sms", null, null),
    /**
     * email(邮件) -- QQ、163邮箱
     */
    EMAIL(40, "email(邮件)", EmailContentModel.class, "email", null, null),
    /**
     * officialAccounts(微信服务号) --
     * accessToken 交由 weixin-java-mp 组件管理，所以不设置expireTime
     */
    OFFICIAL_ACCOUNT(50, "officialAccounts(服务号)", OfficialAccountsContentModel.class, "official_accounts", "official_account_", null),
    /**
     * miniProgram(微信小程序)
     * accessToken 交由 weixin-java-miniapp 组件管理，所以不设置expireTime
     */
    MINI_PROGRAM(60, "miniProgram(小程序)", MiniProgramContentModel.class, "mini_program", "mini_program_", null),

    /**
     * enterpriseWeChat(企业微信)
     */
    ENTERPRISE_WE_CHAT(70, "enterpriseWeChat(企业微信)", EnterpriseWeChatContentModel.class, "enterprise_we_chat", null, null),
    /**
     * dingDingRobot(钉钉机器人)
     */
    DING_DING_ROBOT(80, "dingDingRobot(钉钉机器人)", DingDingRobotContentModel.class, "ding_ding_robot", null, null),
    /**
     * dingDingWorkNotice(钉钉工作通知)
     */
    DING_DING_WORK_NOTICE(90, "dingDingWorkNotice(钉钉工作通知)", DingDingWorkContentModel.class, "ding_ding_work_notice", "ding_ding_access_token_", 3600 * 2L),
    /**
     * enterpriseWeChat(企业微信机器人)
     */
    ENTERPRISE_WE_CHAT_ROBOT(100, "enterpriseWeChat(企业微信机器人)", EnterpriseWeChatRobotContentModel.class, "enterprise_we_chat_robot", null, null),
    /**
     * feiShuRoot(飞书机器人)
     */
    FEI_SHU_ROBOT(110, "feiShuRoot(飞书机器人)", FeiShuRobotContentModel.class, "fei_shu_robot", null, null),
    /**
     * alipayMiniProgram(支付宝小程序)
     */
    ALIPAY_MINI_PROGRAM(120, "alipayMiniProgram(支付宝小程序)", AlipayMiniProgramContentModel.class, "alipay_mini_program", null, null),
    ;

    /**
     * 编码值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;

    /**
     * 内容模型Class
     */
    private final Class<? extends ContentModel> contentModelClass;

    /**
     * 英文标识
     */
    private final String codeEn;

    /**
     * accessToken prefix
     */
    private final String accessTokenPrefix;

    /**
     * accessToken expire
     * 单位秒
     */
    private final Long accessTokenExpire;

    /**
     * 通过code获取class
     *
     * @param code
     * @return
     */
    public static Class<? extends ContentModel> getChanelModelClassByCode(Integer code) {
        return Arrays.stream(values()).filter(channelType -> Objects.equals(code, channelType.getCode()))
                .map(ChannelType::getContentModelClass)
                .findFirst().orElse(null);
    }
}

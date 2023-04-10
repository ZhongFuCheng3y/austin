package com.java3y.austin.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送ID类型枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum IdType implements PowerfulEnum {
    /**
     * 站内userId
     */
    USER_ID(10, "userId"),
    /**
     * 手机设备号
     */
    DID(20, "did"),
    /**
     * 手机号
     */
    PHONE(30, "phone"),
    /**
     * 微信体系的openId
     */
    OPEN_ID(40, "openId"),
    /**
     * 邮件
     */
    EMAIL(50, "email"),
    /**
     * 企业微信userId
     */
    ENTERPRISE_USER_ID(60, "enterprise_user_id"),
    /**
     * 钉钉userId
     */
    DING_DING_USER_ID(70, "ding_ding_user_id"),
    /**
     * 个推cid
     */
    CID(80, "cid"),
    /**
     * 飞书userId
     */
    FEI_SHU_USER_ID(90, "fei_shu_user_id"),
    ;

    private final Integer code;
    private final String description;


}

package com.java3y.austin.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 3y
 * @date 2022/1/22
 */
@Getter
@ToString
@AllArgsConstructor
public enum AuditStatus implements PowerfulEnum {

    /**
     * 10.待审核
     */
    WAIT_AUDIT(10, "待审核"),
    /**
     * 20.审核成功
     */
    AUDIT_SUCCESS(20, "审核成功"),
    /**
     * 30.被拒绝'
     */
    AUDIT_REJECT(30, "被拒绝");

    private final Integer code;
    private final String description;


}

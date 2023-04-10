package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 模板枚举信息
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum TemplateType implements PowerfulEnum {

    /**
     * 定时类的模板(后台定时调用)
     */
    CLOCKING(10, "定时类的模板(后台定时调用)"),
    /**
     * 实时类的模板(接口实时调用)
     */
    REALTIME(20, "实时类的模板(接口实时调用)"),
    ;

    private final Integer code;
    private final String description;

}

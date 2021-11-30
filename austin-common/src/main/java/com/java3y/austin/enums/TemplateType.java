package com.java3y.austin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 模板枚举信息
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum TemplateType {

    OPERATION(10, "运营类的模板"),
    TECHNOLOGY(20, "技术类的模板"),
    ;

    private Integer code;
    private String description;

}

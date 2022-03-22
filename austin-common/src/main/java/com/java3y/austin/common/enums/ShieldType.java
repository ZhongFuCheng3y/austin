package com.java3y.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 屏蔽类型
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum ShieldType {


    NIGHT_NO_SHIELD(10, "夜间不屏蔽"),
    NIGHT_SHIELD(20, "夜间屏蔽"),
    NIGHT_SHIELD_BUT_NEXT_DAY_SEND(30, "夜间屏蔽(次日早上9点发送)");

    private Integer code;
    private String description;




}

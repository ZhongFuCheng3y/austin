package com.java3y.austin.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 消息参数
 * single
 * @author 3y
 */
@Data
@Accessors(chain = true)
public class MessageParam {

    /**
     * @Description: 接收者
     * 多个用,逗号号分隔开
     * 必传
     */
    private String receiver;

    /**
     * @Description: 消息内容中的可变部分
     * 可选
     */
    private Map<String, String> variables;

    /**
     * @Description: 扩展参数
     * 可选
     */
    private Map<String,String> extra;
}

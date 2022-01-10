package com.java3y.austin.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 发送接口的参数
 * @author 3y
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SendRequest {

    /**
     * 执行业务类型
     */
    private String code;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;


    /**
     * 消息相关的参数
     */
    private MessageParam messageParam;



}

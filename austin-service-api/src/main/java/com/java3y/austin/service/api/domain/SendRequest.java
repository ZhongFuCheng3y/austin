package com.java3y.austin.service.api.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class SendRequest {

    /**
     * 执行业务类型(默认填写 "send")
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

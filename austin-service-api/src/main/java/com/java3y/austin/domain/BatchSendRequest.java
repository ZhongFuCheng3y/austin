package com.java3y.austin.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 发送接口的参数
 * batch
 *
 * @author 3y
 */
@Data
@Accessors(chain = true)
public class BatchSendRequest {


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
    private List<MessageParam> messageParamList;


}

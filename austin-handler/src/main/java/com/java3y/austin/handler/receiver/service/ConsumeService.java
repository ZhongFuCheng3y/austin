package com.java3y.austin.handler.receiver.service;


import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.support.domain.MessageTemplate;

import java.util.List;

/**
 * 消费消息服务
 *
 * @author 3y
 */
public interface ConsumeService {

    /**
     * 从MQ拉到消息进行消费，发送消息
     *
     * @param taskInfoLists
     */
    void consume2Send(List<TaskInfo> taskInfoLists);


    /**
     * 从MQ拉到消息进行消费，撤回消息
     *
     * @param messageTemplate
     */
    void consume2recall(MessageTemplate messageTemplate);


}

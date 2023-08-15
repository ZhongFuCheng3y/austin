package com.java3y.austin.handler.receiver.service;


import com.java3y.austin.common.domain.RecallTaskInfo;
import com.java3y.austin.common.domain.TaskInfo;

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
     * 如果有 recallMessageId ，则优先撤回 recallMessageId
     * 如果没有 recallMessageId ，则撤回整个模板的消息
     *
     * @param recallTaskInfo
     */
    void consume2recall(RecallTaskInfo recallTaskInfo);


}

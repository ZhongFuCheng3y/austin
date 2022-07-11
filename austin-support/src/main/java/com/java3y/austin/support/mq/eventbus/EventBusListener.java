package com.java3y.austin.support.mq.eventbus;


import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.support.domain.MessageTemplate;

import java.util.List;

/**
 * @author 3y
 * 监听器
 */
public interface EventBusListener {


    /**
     * 消费消息
     * @param lists
     */
    void consume(List<TaskInfo> lists);

    /**
     * 撤回消息
     * @param messageTemplate
     */
    void recall(MessageTemplate messageTemplate);
}

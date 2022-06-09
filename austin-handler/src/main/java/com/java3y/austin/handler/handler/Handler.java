package com.java3y.austin.handler.handler;

import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.support.domain.MessageTemplate;

/**
 * @author 3y
 * 消息处理器
 */
public interface Handler {

    /**
     * 处理器
     *
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     *
     * @param messageTemplate
     * @return
     */
    void recall(MessageTemplate messageTemplate);


}

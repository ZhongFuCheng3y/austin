package com.java3y.austin.handler.handler;

import com.java3y.austin.common.domain.TaskInfo;

/**
 * @author 3y
 * 消息处理器
 */
public interface Handler {

    /**
     * 处理器
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

}

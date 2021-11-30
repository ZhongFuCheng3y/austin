package com.java3y.austin.handler;

import com.java3y.austin.domain.TaskInfo;

/**
 * @author 3y
 * 发送各个渠道的handler
 */
public interface Handler {

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    boolean doHandler(TaskInfo taskInfo);


}

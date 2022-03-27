package com.java3y.austin.handler.shield;

import com.java3y.austin.common.domain.TaskInfo;

/**
 * 屏蔽服务
 *
 * @author 3y
 */
public interface ShieldService {


    /**
     * 屏蔽消息
     * @param taskInfo
     */
    void shield(TaskInfo taskInfo);
}

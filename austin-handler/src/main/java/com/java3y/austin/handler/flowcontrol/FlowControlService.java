package com.java3y.austin.handler.flowcontrol;

import com.java3y.austin.common.domain.TaskInfo;

/**
 * @author 3y
 * 流量控制服务
 */
public interface FlowControlService {


    /**
     * 根据渠道进行流量控制
     *
     * @param taskInfo
     * @param flowControlParam
     */
    void flowControl(TaskInfo taskInfo, FlowControlParam flowControlParam);

}

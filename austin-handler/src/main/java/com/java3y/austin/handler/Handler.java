package com.java3y.austin.handler;

import com.java3y.austin.domain.AnchorInfo;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author 3y
 * 发送各个渠道的handler
 */
public abstract class Handler {

    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;


    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    public void doHandler(TaskInfo taskInfo) {
        if (!handler(taskInfo)) {
            LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
        }
        LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);

}

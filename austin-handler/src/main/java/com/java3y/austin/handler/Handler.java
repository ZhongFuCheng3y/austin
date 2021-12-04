package com.java3y.austin.handler;

import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.enums.ChannelType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author 3y
 * 发送各个渠道的handler
 */
public abstract class Handler {

    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 初始化渠道与Handler的映射关系
     */
    @PostConstruct
    private void init() {
        for (ChannelType channelType : ChannelType.values()) {
            handlerHolder.putHandler(channelType.getCode(), this);
        }
    }

    public void doHandler(TaskInfo taskInfo) {
        handler(taskInfo);
    }

    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract void handler(TaskInfo taskInfo);

}

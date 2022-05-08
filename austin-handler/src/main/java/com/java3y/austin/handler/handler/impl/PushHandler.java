package com.java3y.austin.handler.handler.impl;

import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 通知栏消息发送处理
 * <p>
 * (目前具体的实现是个推服务商，安卓端已验证)
 *
 * @author 3y
 */
@Component
@Slf4j
public class PushHandler extends BaseHandler implements Handler {

    public PushHandler() {
        channelCode = ChannelType.PUSH.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        return true;
    }

}

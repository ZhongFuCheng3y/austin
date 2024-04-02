package com.java3y.austin.handler.action;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Sets;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.pipeline.BusinessProcess;
import com.java3y.austin.common.pipeline.ProcessContext;
import com.java3y.austin.handler.handler.HandlerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送消息，路由到对应的渠道下发消息
 *
 * @author 3y
 */
@Service
public class SendMessageAction implements BusinessProcess<TaskInfo> {
    @Autowired
    private HandlerHolder handlerHolder;

    @Override
    public void process(ProcessContext<TaskInfo> context) {
        TaskInfo taskInfo = context.getProcessModel();

        // 微信小程序&服务号只支持单人推送，为了后续逻辑统一处理，于是在这做了单发处理
        if (ChannelType.MINI_PROGRAM.getCode().equals(taskInfo.getSendChannel())
                || ChannelType.OFFICIAL_ACCOUNT.getCode().equals(taskInfo.getSendChannel())
                || ChannelType.ALIPAY_MINI_PROGRAM.getCode().equals(taskInfo.getSendChannel())) {
            TaskInfo taskClone = ObjectUtil.cloneByStream(taskInfo);
            for (String receiver : taskInfo.getReceiver()) {
                taskClone.setReceiver(Sets.newHashSet(receiver));
                handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskClone);
            }
            return;
        }
        handlerHolder.route(taskInfo.getSendChannel()).doHandler(taskInfo);
    }
}

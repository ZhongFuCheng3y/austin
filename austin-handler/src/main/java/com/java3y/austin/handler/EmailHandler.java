package com.java3y.austin.handler;

import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.enums.ChannelType;
import org.springframework.stereotype.Component;

/**
 * 邮件发送处理
 *
 * @author 3y
 */
@Component
public class EmailHandler extends Handler {

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public void handler(TaskInfo taskInfoList) {
    }
}

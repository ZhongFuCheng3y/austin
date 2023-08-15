package com.java3y.austin.handler.receiver.springeventbus;

import com.java3y.austin.common.domain.RecallTaskInfo;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.receiver.service.ConsumeService;
import com.java3y.austin.support.constans.MessageQueuePipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：
 *
 * @author tony
 * @date 2023/2/6 11:18
 */
@Component
@ConditionalOnProperty(name = "austin.mq.pipeline", havingValue = MessageQueuePipeline.SPRING_EVENT_BUS)
public class SpringEventBusReceiver {

    @Autowired
    private ConsumeService consumeService;

    public void consume(List<TaskInfo> lists) {
        consumeService.consume2Send(lists);
    }

    public void recall(RecallTaskInfo recallTaskInfo) {
        consumeService.consume2recall(recallTaskInfo);
    }
}

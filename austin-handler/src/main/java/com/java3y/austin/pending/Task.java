package com.java3y.austin.pending;


import cn.hutool.core.collection.CollUtil;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.handler.HandlerHolder;
import com.java3y.austin.service.deduplication.DeduplicationRuleService;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Task 执行器
 * 0.丢弃消息
 * 1.通用去重功能
 * 2.发送消息
 *
 * @author 3y
 */
@Data
@Accessors(chain = true)
@Slf4j
public class Task implements Runnable {

    @Autowired
    private HandlerHolder handlerHolder;

    @Autowired
    private DeduplicationRuleService deduplicationRuleService;

    private TaskInfo taskInfo;

    @Override
    public void run() {
        // 0. TODO 丢弃消息

        // 1.平台通用去重
        deduplicationRuleService.duplication(taskInfo);


        // 2. 真正发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel())
                    .doHandler(taskInfo);
        }

    }
}

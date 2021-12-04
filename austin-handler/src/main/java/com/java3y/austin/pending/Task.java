package com.java3y.austin.pending;


import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.handler.HandlerHolder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Task 执行器
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

    private TaskInfo taskInfo;

    @Override
    public void run() {

        // 1. TODO 通用去重

        // 2. 真正发送消息
        handlerHolder.route(taskInfo.getSendChannel())
                .doHandler(taskInfo);
    }
}

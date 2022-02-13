package com.java3y.austin.cron.handler;

import com.java3y.austin.cron.service.TaskHandler;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 定时任务处理类
 * @author 3y
 */
@Service
@Slf4j
public class CronTaskHandler {

    @Autowired
    private TaskHandler taskHandler;

    /**
     * 处理所有的 austin 定时任务消息
     */
    @XxlJob("austinJob")
    public void execute() {
        log.info("CronTaskHandler#execute messageTemplateId:{} cron exec!", XxlJobHelper.getJobParam());
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());
        taskHandler.handle(messageTemplateId);

    }

}

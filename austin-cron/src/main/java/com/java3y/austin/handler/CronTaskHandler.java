package com.java3y.austin.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CronTaskHandler {

    /**
     * 简单任务
     */
    @XxlJob("austinJobHandler")
    public void execute() {
        log.info("XXL-JOB, Hello World.");
    }

}

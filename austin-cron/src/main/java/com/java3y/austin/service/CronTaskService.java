package com.java3y.austin.service;

import com.java3y.austin.entity.XxlJobInfo;
import com.java3y.austin.vo.BasicResultVO;

/**
 * 定时任务服务
 */
public interface CronTaskService {


    /**
     * 新增/修改 定时任务
     *
     * @return 新增时返回任务Id，修改时无返回
     */
    BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo);

    /**
     * 删除定时任务
     *
     * @param taskId
     */
    BasicResultVO deleteCronTask(Integer taskId);

    /**
     * 启动定时任务
     *
     * @param taskId
     */
    BasicResultVO startCronTask(Integer taskId);


    /**
     * 暂停定时任务
     *
     * @param taskId
     */
    BasicResultVO stopCronTask(Integer taskId);


}

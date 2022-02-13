package com.java3y.austin.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.java3y.austin.cron.constants.PendingConstant;
import com.java3y.austin.cron.domain.CrowdInfoVo;
import com.java3y.austin.cron.pending.CrowdBatchTaskPending;
import com.java3y.austin.cron.service.TaskHandler;
import com.java3y.austin.cron.utils.ReadFileUtils;
import com.java3y.austin.support.dao.MessageTemplateDao;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.support.pending.PendingParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 3y
 * @date 2022/2/9
 */
@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {
    @Autowired
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private CrowdBatchTaskPending crowdBatchTaskPending;


    @Override
    @Async
    public void handle(Long messageTemplateId) {
        log.info("start:{}", Thread.currentThread().getName());

        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);
            return;
        }

        // 初始化pending的信息
        PendingParam<CrowdInfoVo> pendingParam = new PendingParam<>();
        pendingParam.setNumThreshold(PendingConstant.NUM_THRESHOLD)
                .setQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setTimeThreshold(PendingConstant.TIME_THRESHOLD)
                .setThreadNum(PendingConstant.THREAD_NUM)
                .setPending(crowdBatchTaskPending);
        crowdBatchTaskPending.initAndStart(pendingParam);

        // 读取文件得到每一行记录给到队列做batch处理
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }
            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).build();
            crowdBatchTaskPending.pending(crowdInfoVo);
        });

    }

}

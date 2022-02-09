package com.java3y.austin.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.cron.domain.CrowdInfoVo;
import com.java3y.austin.cron.service.TaskHandler;
import com.java3y.austin.cron.utils.ReadFileUtils;
import com.java3y.austin.support.dao.MessageTemplateDao;
import com.java3y.austin.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 3y
 * @date 2022/2/9
 */
@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {
    @Autowired
    private MessageTemplateDao messageTemplateDao;


    @Override
    @Async
    public void handle(Long messageTemplateId) {
        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty!");
            return;
        }
        List<CrowdInfoVo> csvRowList = ReadFileUtils.getCsvRowList(messageTemplate.getCronCrowdPath());

        if (CollUtil.isNotEmpty(csvRowList)) {

        }

        log.info("csv info:", JSON.toJSONString(csvRowList));
    }

}

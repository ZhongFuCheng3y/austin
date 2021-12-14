package com.java3y.austin.service.deduplication;

import cn.hutool.core.date.DateUtil;
import com.java3y.austin.domain.DeduplicationParam;
import com.java3y.austin.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 3y
 * @date 2021/12/12
 * 去重服务
 */
@Service
public class DeduplicationRuleService {

    @Autowired
    private ContentDeduplicationService contentDeduplicationService;

    @Autowired
    private FrequencyDeduplicationService frequencyDeduplicationService;


    public void duplication(TaskInfo taskInfo) {

        // 文案去重
        DeduplicationParam contentParams = DeduplicationParam.builder()
                .deduplicationTime(300L).countNum(1).taskInfo(taskInfo)
                .build();
        contentDeduplicationService.deduplication(contentParams);

        // 运营总规则去重(一天内用户收到最多同一个渠道的消息次数)
        Long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam businessParams = DeduplicationParam.builder()
                .deduplicationTime(seconds).countNum(5).taskInfo(taskInfo)
                .build();
        frequencyDeduplicationService.deduplication(businessParams);
    }

}

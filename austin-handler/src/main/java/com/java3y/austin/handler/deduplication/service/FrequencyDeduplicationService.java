package com.java3y.austin.handler.deduplication.service;

import cn.hutool.core.text.StrPool;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.enums.DeduplicationType;
import com.java3y.austin.handler.deduplication.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * @author 3y
 * @date 2021/12/12
 * 频次去重服务
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {


    private static final String PREFIX = "FRE";

    @Autowired
    public FrequencyDeduplicationService(@Qualifier("SimpleLimitService") LimitService limitService) {

        this.limitService = limitService;
        deduplicationType = DeduplicationType.FREQUENCY.getCode();

    }

    /**
     * 业务规则去重 构建key
     * <p>
     * key ： receiver + sendChannel
     * <p>
     * 一天内一个用户只能收到某个渠道的消息 N 次
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrPool.C_UNDERLINE
                + receiver + StrPool.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}

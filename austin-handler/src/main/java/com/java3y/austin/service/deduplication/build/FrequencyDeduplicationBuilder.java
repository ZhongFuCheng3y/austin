package com.java3y.austin.service.deduplication.build;

import cn.hutool.core.date.DateUtil;
import com.java3y.austin.domain.DeduplicationParam;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.enums.DeduplicationType;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huskey
 * @date 2022/1/18
 */

@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {
    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}

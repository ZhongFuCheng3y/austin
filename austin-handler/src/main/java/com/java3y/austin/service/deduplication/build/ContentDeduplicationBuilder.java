package com.java3y.austin.service.deduplication.build;

import com.java3y.austin.domain.DeduplicationParam;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.enums.DeduplicationType;
import org.springframework.stereotype.Service;


/**
 * @author huskey
 * @date 2022/1/18
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplication == null) {
           return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}

package com.java3y.austin.service.deduplication.build;

import com.alibaba.fastjson.JSONObject;
import com.java3y.austin.domain.DeduplicationParam;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.service.deduplication.build.Builder;
import org.springframework.stereotype.Service;


/**
 * @author huskey
 * @date 2022/1/18
 */

@Service
public class ContentDeduplicationBuilder implements Builder {

    public DeduplicationParam build(String deduplication, String key) {
        JSONObject object = JSONObject.parseObject(deduplication);
        if (object == null) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;

    }
}

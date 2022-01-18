package com.java3y.austin.service.deduplication.build;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.java3y.austin.domain.DeduplicationParam;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.service.deduplication.build.Builder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author huskey
 * @date 2022/1/18
 */

@Service
public class FrequencyDeduplicationBuilder implements Builder {

    @Override
    public DeduplicationParam build(String deduplication, String key) {
        JSONObject object = JSONObject.parseObject(deduplication);
        if (object == null) {
            return null;
        }
        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.RULE_DEDUPLICATION);
        return deduplicationParam;
    }
}

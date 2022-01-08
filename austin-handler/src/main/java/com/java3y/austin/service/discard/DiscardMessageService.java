package com.java3y.austin.service.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.java3y.austin.constant.AustinConstant;
import com.java3y.austin.domain.AnchorInfo;
import com.java3y.austin.domain.TaskInfo;
import com.java3y.austin.enums.AnchorState;
import com.java3y.austin.utils.LogUtils;
import org.springframework.stereotype.Service;

/**
 * 丢弃模板消息
 * @author 3y
 */
@Service
public class DiscardMessageService {
    private static final String DISCARD_MESSAGE_KEY = "discard";

    @ApolloConfig("boss.austin")
    private Config config;

    /**
     * 丢弃消息，配置在apollo
     * @param taskInfo
     * @return
     */
    public boolean isDiscard(TaskInfo taskInfo) {
        JSONArray array = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                AustinConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));
        if (array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            LogUtils.print(AnchorInfo.builder().businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).state(AnchorState.DISCARD.getCode()).build());
            return true;
        }
        return false;
    }

}

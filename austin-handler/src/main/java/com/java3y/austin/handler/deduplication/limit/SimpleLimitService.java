package com.java3y.austin.handler.deduplication.limit;

import cn.hutool.core.collection.CollUtil;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.handler.deduplication.DeduplicationParam;
import com.java3y.austin.handler.deduplication.service.AbstractDeduplicationService;
import com.java3y.austin.support.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 采用普通的计数去重方法，限制的是每天发送的条数。
 * 业务逻辑： 一天内相同的用户如果已经收到某渠道内容5次，则应该被过滤掉
 * 技术方案：由pipeline set & mget实现
 * @author cao
 * @date 2022-04-20 13:41
 */
@Service(value = "SimpleLimitService")
public class SimpleLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SP_";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {
        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        // 获取redis记录
        Map<String, String> readyPutRedisReceiver = new HashMap<>(taskInfo.getReceiver().size());
        //redis数据隔离
        List<String> keys = deduplicationAllKey(service, taskInfo).stream().map(key -> LIMIT_TAG + key).collect(Collectors.toList());
        Map<String, String> inRedisValue = redisUtils.mGet(keys);

        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            String value = inRedisValue.get(key);

            // 符合条件的用户
            if (Objects.nonNull(value) && Integer.parseInt(value) >= param.getCountNum()) {
                filterReceiver.add(receiver);
            } else {
                readyPutRedisReceiver.put(receiver, key);
            }
        }

        // 不符合条件的用户：需要更新Redis(无记录添加，有记录则累加次数)
        putInRedis(readyPutRedisReceiver, inRedisValue, param.getDeduplicationTime());

        return filterReceiver;
    }


    /**
     * 存入redis 实现去重
     *
     * @param readyPutRedisReceiver
     */
    private void putInRedis(Map<String, String> readyPutRedisReceiver,
                            Map<String, String> inRedisValue, Long deduplicationTime) {
        Map<String, String> keyValues = new HashMap<>(readyPutRedisReceiver.size());
        for (Map.Entry<String, String> entry : readyPutRedisReceiver.entrySet()) {
            String key = entry.getValue();
            if (Objects.nonNull(inRedisValue.get(key))) {
                keyValues.put(key, String.valueOf(Integer.parseInt(inRedisValue.get(key)) + 1));
            } else {
                keyValues.put(key, String.valueOf(CommonConstant.TRUE));
            }
        }
        if (CollUtil.isNotEmpty(keyValues)) {
            redisUtils.pipelineSetEx(keyValues, deduplicationTime);
        }
    }

}

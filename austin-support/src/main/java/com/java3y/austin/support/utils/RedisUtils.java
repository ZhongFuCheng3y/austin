package com.java3y.austin.support.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 3y
 * @date 2021/12/10
 * 对Redis的某些操作二次封装
 */
@Component
@Slf4j
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * mGet将结果封装为Map
     *
     * @param keys
     */
    public Map<String, String> mGet(List<String> keys) {
        HashMap<String, String> result = new HashMap<>(keys.size());
        try {
            List<String> value = redisTemplate.opsForValue().multiGet(keys);
            if (CollUtil.isNotEmpty(value)) {
                for (int i = 0; i < keys.size(); i++) {
                    result.put(keys.get(i), value.get(i));
                }
            }
        } catch (Exception e) {
            log.error("redis mGet fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    /**
     * pipeline 设置 key-value 并设置过期时间
     */
    public void pipelineSetEx(Map<String, String> keyValues, Long seconds) {
        try {
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    connection.setEx(entry.getKey().getBytes(), seconds,
                            entry.getValue().getBytes());
                }
                return null;
            });
        } catch (Exception e) {
            log.error("redis pipelineSetEX fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * pipeline 设置 key-value 并设置过期时间
     *
     * @param seconds 过期时间
     * @param delta   自增的步长
     */
    public void pipelineHashIncrByEX(Map<String, String> keyValues, Long seconds, Long delta) {
        try {
            redisTemplate.executePipelined((RedisCallback<String>) connection -> {
                for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                    connection.hIncrBy(entry.getKey().getBytes(), entry.getValue().getBytes(), delta);
                    connection.expire(entry.getKey().getBytes(), seconds);
                }
                return null;
            });
        } catch (Exception e) {
            log.error("redis pipelineSetEX fail! e:{}", Throwables.getStackTraceAsString(e));
        }
    }
}

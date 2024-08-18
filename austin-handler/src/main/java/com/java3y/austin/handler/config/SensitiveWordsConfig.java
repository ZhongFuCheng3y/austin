package com.java3y.austin.handler.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 敏感词配置
 *
 * @author xiaoxiamao
 * @date 2024/08/17
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "austin.senswords")
public class SensitiveWordsConfig {

    /**
     * 敏感词字典redis key
     */
    public static final String SENS_WORDS_DICT = "SENS_WORDS_DICT";
    /**
     * 更新时间
     */
    private static final long UPDATE_TIME = 10 * 60 * 1000;
    /**
     * 敏感词字典
     */
    private Set<String> sensitiveWords = Collections.emptySet();

    /**
     * 是否开启敏感词过滤
     */
    private boolean filterEnabled;
    /**
     * 字典路径
     */
    private String dictPath;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TaskExecutor taskExecutor;

    /**
     * 初始化敏感词字典
     */
    @PostConstruct
    public void loadSensitiveWords() {
        // 不开启过滤，直接返回
        if (!filterEnabled) {
            log.info("SensitiveWordConfig#loadSensitiveWords filterEnabled is false, return.");
            return;
        }
        loadSensWords();
        storeSensWords();
        // 开启定时任务，每10分钟更新一次
        taskExecutor.execute(this::startScheduledUpdate);
    }

    /**
     * 加载敏感词字典
     */
    private void loadSensWords() {
        if (ObjectUtils.isEmpty(dictPath)) {
            log.error("SensitiveWordConfig#loadSensWords dictPath is null or empty, skipping load.");
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(dictPath))) {
            sensitiveWords = reader.lines().map(String::trim).collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("SensitiveWordConfig#loadSensitiveWords Failed to load sensitive words from {}: {}",
                    dictPath, e.getMessage());
            sensitiveWords = Collections.emptySet();
        }
    }

    /**
     * 存储敏感词字典
     */
    private void storeSensWords() {
        redisTemplate.opsForSet().add(SENS_WORDS_DICT, sensitiveWords.toArray(new String[0]));
        log.info("SensitiveWordConfig#storeSensWords {} sensitive words stored in Redis under key '{}'.",
                sensitiveWords.size(), SENS_WORDS_DICT);
    }

    /**
     * 实现热更新，修改词典后自动加载
     */
    private void startScheduledUpdate() {
        while (true) {
            try {
                // 每10分钟更新一次
                TimeUnit.SECONDS.sleep(UPDATE_TIME);
                log.info("SensitiveWordConfig#startScheduledUpdate start update...");
                loadSensitiveWords();
                storeSensWords();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("SensitiveWordConfig#startScheduledUpdate interrupted: {}", e.getMessage());
                break;
            }
        }
    }

}

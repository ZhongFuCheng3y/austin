package com.java3y.austin.handler.config;

import com.java3y.austin.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
public class SensitiveWordsConfig {

    /**
     * 敏感词字典redis key
     */
    public static final String SENS_WORDS_DICT = "SENS_WORDS_DICT";

    /**
     * 文件前缀
     */
    private static final String FILE_PREFIX = "file:";
    /**
     * 更新时间
     */
    private static final long UPDATE_TIME_SECONDS = 10 * 60;
    /**
     * 敏感词字典
     */
    private Set<String> sensitiveWords = Collections.emptySet();

    /**
     * 是否开启敏感词过滤
     */
    @Value("${austin.senswords.filter.enabled}")
    private boolean filterEnabled;
    /**
     * 字典路径
     */
    @Value("${austin.senswords.dict.path}")
    private String dictPath;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 是否终止线程
     */
    private volatile boolean stop = false;

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
        // 加载并存储
        loadSensWords();
        storeSensWords();
        // 定时更新
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
        // 为直接路径，添加前缀
        Resource resource = resourceLoader.getResource(dictPath.startsWith(CommonConstant.SLASH) ? FILE_PREFIX + dictPath : dictPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
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
        redisTemplate.delete(SENS_WORDS_DICT);
        if (ObjectUtils.isEmpty(sensitiveWords)) {
            return;
        }
        redisTemplate.opsForSet().add(SENS_WORDS_DICT, sensitiveWords.toArray(new String[0]));
        log.debug("SensitiveWordConfig#storeSensWords sensitive words stored in Redis under key [{}], count [{}].",
                SENS_WORDS_DICT, sensitiveWords.size());
    }

    /**
     * 实现热更新，修改词典后自动加载
     */
    private void startScheduledUpdate() {
        while (!stop) {
            try {
                TimeUnit.SECONDS.sleep(UPDATE_TIME_SECONDS);
                log.debug("SensitiveWordConfig#startScheduledUpdate start update...");
                loadSensWords();
                storeSensWords();
            } catch (InterruptedException e) {
                log.error("SensitiveWordConfig#startScheduledUpdate interrupted: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * onDestroy
     */
    @PreDestroy
    public void onDestroy() {
        stop = true;
        if (taskExecutor instanceof ThreadPoolTaskExecutor) {
            ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) taskExecutor;
            threadPoolTaskExecutor.shutdown();
        }
    }

}

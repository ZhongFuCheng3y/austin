package com.java3y.austin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 3y
 * @date 2021/12/9
 */
@RestController
@Slf4j
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @RequestMapping("/redis")
    public void testRedis() {
        log.info(redisTemplate.opsForValue().get("1"));
    }
}

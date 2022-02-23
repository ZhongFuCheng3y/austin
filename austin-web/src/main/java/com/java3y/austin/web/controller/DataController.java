package com.java3y.austin.web.controller;

import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.utils.RedisUtils;
import com.java3y.austin.web.vo.DataParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 获取数据接口（全链路追踪)
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/trace")
@Api("获取数据接口（全链路追踪)")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class DataController {

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/data")
    @ApiOperation("/获取数据")
    public BasicResultVO getData(@RequestBody DataParam dataParam) {


        Long businessId = dataParam.getBusinessId();
        Map<Object, Object> objectObjectMap = redisUtils.hGetAll(String.valueOf(businessId));
        log.info("data:{}", JSON.toJSONString(objectObjectMap));
        return BasicResultVO.success();
    }

}

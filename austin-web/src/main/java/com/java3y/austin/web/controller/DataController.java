package com.java3y.austin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.utils.RedisUtils;
import com.java3y.austin.web.service.DataService;
import com.java3y.austin.web.vo.amis.EchartsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    private DataService dataService;

    @GetMapping("/user")
    @ApiOperation("/获取【当天】用户接收消息的全链路数据")
    public BasicResultVO getUserData(String receiver) {
        List<String> list = redisUtils.lRange(receiver, 0, -1);
        // log.info("data:{}", JSON.toJSONString(objectObjectMap));
        return BasicResultVO.success();
    }

    @GetMapping("/messageTemplate")
    @ApiOperation("/获取消息模板全链路数据")
    public BasicResultVO getMessageTemplateData(String businessId) {
        EchartsVo echartsVo = EchartsVo.builder().build();
        if (StrUtil.isNotBlank(businessId)) {
            echartsVo = dataService.getTraceMessageTemplateInfo(businessId);
        }
        return BasicResultVO.success(echartsVo);
    }

    public static void main(String[] args) {
        EchartsVo.TitleVO titleVO = EchartsVo.TitleVO.builder().text("销售情况").build();
        EchartsVo echartsVo = EchartsVo.builder().title(titleVO).build();

        System.out.println(JSON.toJSONString(echartsVo));
    }
}

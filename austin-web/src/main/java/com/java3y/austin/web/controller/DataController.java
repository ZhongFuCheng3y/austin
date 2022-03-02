package com.java3y.austin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.web.service.DataService;
import com.java3y.austin.web.vo.DataParam;
import com.java3y.austin.web.vo.amis.EchartsVo;
import com.java3y.austin.web.vo.amis.UserTimeLineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private DataService dataService;

    @PostMapping("/user")
    @ApiOperation("/获取【当天】用户接收消息的全链路数据")
    public BasicResultVO getUserData(@RequestBody DataParam dataParam) {
        UserTimeLineVo traceUserInfo = dataService.getTraceUserInfo(dataParam.getReceiver());

        return BasicResultVO.success(traceUserInfo);
    }

    @PostMapping("/messageTemplate")
    @ApiOperation("/获取消息模板全链路数据")
    public BasicResultVO getMessageTemplateData(@RequestBody DataParam dataParam) {
        EchartsVo echartsVo = EchartsVo.builder().build();
        if (StrUtil.isNotBlank(dataParam.getBusinessId())) {
            echartsVo = dataService.getTraceMessageTemplateInfo(dataParam.getBusinessId());
        }
        return new BasicResultVO<>(RespStatusEnum.SUCCESS, echartsVo);
    }

    public static void main(String[] args) {
        EchartsVo.TitleVO titleVO = EchartsVo.TitleVO.builder().text("销售情况").build();
        EchartsVo echartsVo = EchartsVo.builder().title(titleVO).build();

        System.out.println(JSON.toJSONString(echartsVo));
    }
}

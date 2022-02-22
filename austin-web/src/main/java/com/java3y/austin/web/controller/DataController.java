package com.java3y.austin.web.controller;

import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.domain.MessageTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 获取数据接口（全链路追踪)
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/messageTemplate")
@Api("获取数据接口（全链路追踪)")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class DataController {

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/data")
    @ApiOperation("/获取数据")
    public BasicResultVO getData(@RequestBody MessageTemplate messageTemplate) {


        return BasicResultVO.success();
    }

}

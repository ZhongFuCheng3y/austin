package com.java3y.austin.web.controller;


import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.dao.ChannelAccountDao;
import com.java3y.austin.support.domain.ChannelAccount;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.web.service.ChannelAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 渠道账号管理接口
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/account")
@Api("素材管理接口")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class ChannelAccountController {

    @Autowired
    private ChannelAccountService channelAccountService;


    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public BasicResultVO saveOrUpdate(@RequestBody ChannelAccount channelAccount) {
        return BasicResultVO.success(channelAccountService.save(channelAccount));
    }

    /**
     * 根据渠道标识查询渠道账号相关的信息
     */
    @GetMapping("/query")
    @ApiOperation("/保存数据")
    public BasicResultVO query(Integer channelType) {
        return BasicResultVO.success(channelAccountService.queryByChannelType(channelType));
    }

}

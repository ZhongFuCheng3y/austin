package com.java3y.austin.web.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.service.api.domain.MessageParam;
import com.java3y.austin.service.api.domain.SendRequest;
import com.java3y.austin.service.api.domain.SendResponse;
import com.java3y.austin.service.api.enums.BusinessCode;
import com.java3y.austin.service.api.service.RecallService;
import com.java3y.austin.service.api.service.SendService;
import com.java3y.austin.support.domain.MessageTemplate;
import com.java3y.austin.web.service.MessageTemplateService;
import com.java3y.austin.web.utils.ConvertMap;
import com.java3y.austin.web.vo.MessageTemplateParam;
import com.java3y.austin.web.vo.MessageTemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 消息模板管理Controller
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/messageTemplate")
@Api("发送消息")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

    @Autowired
    private RecallService recallService;

    @Value("${austin.business.upload.crowd.path}")
    private String dataPath;

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateService.saveOrUpdate(messageTemplate);
        return BasicResultVO.success(info);
    }

    /**
     * 列表数据
     */
    @GetMapping("/list")
    @ApiOperation("/列表页")
    public BasicResultVO queryList(MessageTemplateParam messageTemplateParam) {
        List<Map<String, Object>> result = ConvertMap.flatList(messageTemplateService.queryList(messageTemplateParam));

        long count = messageTemplateService.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(result).build();
        return BasicResultVO.success(messageTemplateVo);
    }

    /**
     * 根据Id查找
     */
    @GetMapping("query/{id}")
    @ApiOperation("/根据Id查找")
    public BasicResultVO queryById(@PathVariable("id") Long id) {
        Map<String, Object> result = ConvertMap.flatSingle(messageTemplateService.queryById(id));
        return BasicResultVO.success(result);
    }

    /**
     * 根据Id复制
     */
    @PostMapping("copy/{id}")
    @ApiOperation("/根据Id复制")
    public BasicResultVO copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);
        return BasicResultVO.success();
    }


    /**
     * 根据Id删除
     * id多个用逗号分隔开
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public BasicResultVO deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
            messageTemplateService.deleteByIds(idList);
            return BasicResultVO.success();
        }
        return BasicResultVO.fail();
    }


    /**
     * 测试发送接口
     */
    @PostMapping("test")
    @ApiOperation("/测试发送接口")
    public BasicResultVO test(@RequestBody MessageTemplateParam messageTemplateParam) {

        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder().receiver(messageTemplateParam.getReceiver()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode()).messageTemplateId(messageTemplateParam.getId()).messageParam(messageParam).build();
        SendResponse response = sendService.send(sendRequest);
        if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
            return BasicResultVO.fail(response.getMsg());
        }
        return BasicResultVO.success(response);
    }

    /**
     * 撤回接口
     */
    @PostMapping("recall/{id}")
    @ApiOperation("/撤回消息接口")
    public BasicResultVO recall(@PathVariable("id") String id) {

        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.RECALL.getCode()).
                messageTemplateId(Long.valueOf(id)).build();
        SendResponse response = recallService.recall(sendRequest);
        if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
            return BasicResultVO.fail(response.getMsg());
        }
        return BasicResultVO.success(response);
    }


    /**
     * 启动模板的定时任务
     */
    @PostMapping("start/{id}")
    @ApiOperation("/启动模板的定时任务")
    public BasicResultVO start(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.startCronTask(id);
    }

    /**
     * 暂停模板的定时任务
     */
    @PostMapping("stop/{id}")
    @ApiOperation("/暂停模板的定时任务")
    public BasicResultVO stop(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.stopCronTask(id);
    }

    /**
     * 上传人群文件
     */
    @PostMapping("upload")
    @ApiOperation("/上传人群文件")
    public BasicResultVO upload(@RequestParam("file") MultipartFile file) {
        String filePath = new StringBuilder(dataPath)
                .append(IdUtil.fastSimpleUUID())
                .append(file.getOriginalFilename())
                .toString();
        try {
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            file.transferTo(localFile);


        } catch (Exception e) {
            log.error("MessageTemplateController#upload fail! e:{},params{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(file));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
        return BasicResultVO.success(MapUtil.of(new String[][]{{"value", filePath}}));
    }

}


package com.java3y.austin.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.domain.MessageParam;
import com.java3y.austin.domain.MessageTemplate;
import com.java3y.austin.domain.SendRequest;
import com.java3y.austin.domain.SendResponse;
import com.java3y.austin.enums.BusinessCode;
import com.java3y.austin.enums.RespStatusEnum;
import com.java3y.austin.service.MessageTemplateService;
import com.java3y.austin.service.SendService;
import com.java3y.austin.utils.ConvertMap;
import com.java3y.austin.vo.BasicResultVO;
import com.java3y.austin.vo.MessageTemplateParam;
import com.java3y.austin.vo.MessageTemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 消息模板管理Controller
 *
 * @author 3y
 */
@RestController
@RequestMapping("/messageTemplate")
@Api("发送消息")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class MessageTemplateController {
    private static final List<String> flatFieldName = Arrays.asList("msgContent");

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/插入数据")
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
        List<Map<String, Object>> result = ConvertMap.flatList(messageTemplateService.queryList(messageTemplateParam), flatFieldName);

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
        Map<String, Object> result = ConvertMap.flatSingle(messageTemplateService.queryById(id), flatFieldName);
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

}

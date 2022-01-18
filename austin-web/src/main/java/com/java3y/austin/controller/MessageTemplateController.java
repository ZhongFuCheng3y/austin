package com.java3y.austin.controller;

import com.java3y.austin.dao.MessageTemplateDao;
import com.java3y.austin.domain.MessageTemplate;
import com.java3y.austin.vo.BasicResultVO;
import com.java3y.austin.vo.MessageTemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 消息模板管理Controller
 * @author 3y
 */
@RestController
@RequestMapping("/messageTemplate")
@Api("发送消息")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/插入数据")
    public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateDao.save(messageTemplate);
        return BasicResultVO.success(info);
    }

    /**
     * 列表数据
     */
    @GetMapping("/query")
    @ApiOperation("/查找数据")
    public BasicResultVO queryList() {
        Iterable<MessageTemplate> all = messageTemplateDao.findAll();
        long count = messageTemplateDao.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(all).build();
        return BasicResultVO.success(messageTemplateVo);
    }
}

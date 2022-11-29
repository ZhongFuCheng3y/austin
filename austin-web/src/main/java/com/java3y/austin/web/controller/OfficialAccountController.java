package com.java3y.austin.web.controller;


import cn.hutool.core.util.StrUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.utils.WxServiceUtils;
import com.java3y.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信服务号
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/officialAccount")
@Api("微信服务号")
@CrossOrigin(origins = {AustinConstant.ORIGIN_VALUE, "https://aisuda.bce.baidu.com", "http://localhost:8080"}
        , allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.PUT, RequestMethod.POST, RequestMethod.GET})
public class OfficialAccountController {


    /**
     * @param id 账号Id
     * @return
     */
    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    public BasicResultVO queryList(Long id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();

            WxMpService wxMpService = WxServiceUtils.wxMpServiceMap.get(id);
            List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
            for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
                CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(wxMpTemplate.getTitle()).value(wxMpTemplate.getTemplateId()).build();
                result.add(commonAmisVo);
            }
            return BasicResultVO.success(result);
        } catch (Exception e) {
            log.error("OfficialAccountController#queryList fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
    }


    /**
     * 根据账号Id和模板ID获取模板列表
     *
     * @return
     * @
     */
    @PostMapping("/detailTemplate")
    @ApiOperation("/根据账号Id和模板ID获取模板列表")
    public BasicResultVO queryDetailList(Long id, String wxTemplateId) {
        if (id == null || wxTemplateId == null) {
            return BasicResultVO.success(RespStatusEnum.CLIENT_BAD_PARAMETERS);
        }
        try {
            WxMpService wxMpService = WxServiceUtils.wxMpServiceMap.get(id);
            List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
            CommonAmisVo wxMpTemplateParam = getWxMpTemplateParam(wxTemplateId, allPrivateTemplate);
            return BasicResultVO.success(wxMpTemplateParam);
        } catch (Exception e) {
            log.error("OfficialAccountController#queryList fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
    }


    /**
     * 这个方法不用看，纯粹为了适配amis前端
     *
     * @param wxTemplateId
     * @param allPrivateTemplate
     * @return
     */
    private CommonAmisVo getWxMpTemplateParam(String wxTemplateId, List<WxMpTemplate> allPrivateTemplate) {
        CommonAmisVo officialAccountParam = null;
        for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
            if (wxTemplateId.equals(wxMpTemplate.getTemplateId())) {
                String[] data = wxMpTemplate.getContent().split(StrUtil.LF);
                officialAccountParam = CommonAmisVo.builder()
                        .type("input-table")
                        .name("officialAccountParam")
                        .label("新增一行，输入模板对应的文案")
                        .addable(true)
                        .editable(true)
                        .needConfirm(false)
                        .build();
                List<CommonAmisVo.ColumnsDTO> columnsDTOS = new ArrayList<>();
                for (String datum : data) {
                    String name = datum.substring(datum.indexOf("{{") + 2, datum.indexOf("."));
                    CommonAmisVo.ColumnsDTO.ColumnsDTOBuilder dtoBuilder = CommonAmisVo.ColumnsDTO.builder().name(name).type("input-text").required(true).quickEdit(true);
                    if (datum.contains("first")) {
                        dtoBuilder.label("名字");
                    } else if (datum.contains("remark")) {
                        dtoBuilder.label("备注");
                    } else {
                        dtoBuilder.label(datum.split("：")[0]);
                    }
                    columnsDTOS.add(dtoBuilder.build());
                }
                officialAccountParam.setColumns(columnsDTOS);

            }
        }
        return officialAccountParam;
    }
}

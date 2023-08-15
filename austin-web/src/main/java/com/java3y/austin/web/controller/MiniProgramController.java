package com.java3y.austin.web.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.http.HttpUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.SendChanelUrlConstant;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.support.utils.AccountUtils;
import com.java3y.austin.web.annotation.AustinAspect;
import com.java3y.austin.web.annotation.AustinResult;
import com.java3y.austin.web.exception.CommonException;
import com.java3y.austin.web.utils.Convert4Amis;
import com.java3y.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 微信服务号
 *
 * @author 3y
 */
@Slf4j
@AustinAspect
@RestController
@RequestMapping("/miniProgram")
@Api("微信服务号")
public class MiniProgramController {

    @Autowired
    private AccountUtils accountUtils;

    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    @AustinResult
    public List<CommonAmisVo> queryList(Integer id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();
            WxMaService wxMaService = accountUtils.getAccountById(id, WxMaService.class);
            List<TemplateInfo> templateList = wxMaService.getSubscribeService().getTemplateList();
            for (TemplateInfo templateInfo : templateList) {
                CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(templateInfo.getTitle()).value(templateInfo.getPriTmplId()).build();
                result.add(commonAmisVo);
            }
            return result;
        } catch (Exception e) {
            log.error("MiniProgramController#queryList fail:{}", Throwables.getStackTraceAsString(e));
            throw new CommonException(RespStatusEnum.SERVICE_ERROR);
        }

    }

    /**
     * 根据账号Id和模板ID获取模板列表
     *
     * @return
     */
    @PostMapping("/detailTemplate")
    @ApiOperation("/根据账号Id和模板ID获取模板列表")
    @AustinResult
    public CommonAmisVo queryDetailList(Integer id, String wxTemplateId) {
        if (Objects.isNull(id) || Objects.isNull(wxTemplateId)) {
            log.info("id || wxTemplateId null! id:{},wxTemplateId:{}", id, wxTemplateId);
            return CommonAmisVo.builder().build();
        }

        try {
            WxMaService wxMaService = accountUtils.getAccountById(id, WxMaService.class);
            List<TemplateInfo> templateList = wxMaService.getSubscribeService().getTemplateList();
            return Convert4Amis.getWxMaTemplateParam(wxTemplateId, templateList);
        } catch (Exception e) {
            log.error("MiniProgramController#queryDetailList fail:{}", Throwables.getStackTraceAsString(e));
            throw new CommonException(RespStatusEnum.SERVICE_ERROR);
        }
    }

    /**
     * 登录凭证校验
     * <p>
     * 临时给小程序登录使用，正常消息推送平台不会有此接口
     *
     * @return
     */
    @GetMapping("/sync/openid")
    @ApiOperation("登录凭证校验")
    public String syncOpenId(String code, String appId, String secret) {
        String url = SendChanelUrlConstant.WE_CHAT_MINI_PROGRAM_OPENID_SYNC
                .replace("<APPID>", appId).replace("<CODE>", code).replace("<SECRET>", secret);
        return HttpUtil.get(url);
    }

}

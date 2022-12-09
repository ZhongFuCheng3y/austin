package com.java3y.austin.web.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Throwables;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.utils.WxServiceUtils;
import com.java3y.austin.web.utils.Convert4Amis;
import com.java3y.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微信服务号
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/officialAccount")
@Api("微信服务号")
public class OfficialAccountController {

    @Autowired
    private WxServiceUtils wxServiceUtils;

    /**
     * @param id 账号Id
     * @return
     */
    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    public BasicResultVO queryList(Long id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();
            WxMpService wxMpService = wxServiceUtils.getOfficialAccountServiceMap().get(id);

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
     */
    @PostMapping("/detailTemplate")
    @ApiOperation("/根据账号Id和模板ID获取模板列表")
    public BasicResultVO queryDetailList(Long id, String wxTemplateId) {
        if (id == null || wxTemplateId == null) {
            return BasicResultVO.success(RespStatusEnum.CLIENT_BAD_PARAMETERS);
        }
        try {
            WxMpService wxMpService = wxServiceUtils.getOfficialAccountServiceMap().get(id);
            List<WxMpTemplate> allPrivateTemplate = wxMpService.getTemplateMsgService().getAllPrivateTemplate();
            CommonAmisVo wxMpTemplateParam = Convert4Amis.getWxMpTemplateParam(wxTemplateId, allPrivateTemplate);
            return BasicResultVO.success(wxMpTemplateParam);
        } catch (Exception e) {
            log.error("OfficialAccountController#queryDetailList fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
    }


    /**
     * 接收微信的事件消息
     * https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
     * 临时给微信服务号登录使用，正常消息推送平台不会有此接口
     *
     * @return
     */
    @GetMapping("/receipt")
    @ApiOperation("/接收微信的事件消息")
    public String receiptMessage(String signature, String timestamp, String nonce, String echostr) {
        log.info("get weixin signature:{},:{},nonce:{},echostr:{}", signature, timestamp, nonce, echostr);
        if (StrUtil.isNotBlank(echostr)) {
            return echostr;
        }
        return null;
    }

    /**
     * 临时给微信服务号登录使用（生成二维码），正常消息推送平台不会有此接口
     *
     * @param accountId 消息推送平台体系内的Id
     * @return
     */
    @GetMapping("/qrCode")
    @ApiOperation("/生成 服务号 二维码")
    public BasicResultVO getQrCode(Long accountId) {
        if (accountId == null) {
            return BasicResultVO.fail(RespStatusEnum.CLIENT_BAD_PARAMETERS);
        }
        try {
            // 生成随机值，获取服务号二维码 且 用于校验登录
            String id = IdUtil.getSnowflake().nextIdStr();
            WxMpService wxMpService = wxServiceUtils.getOfficialAccountServiceMap().get(accountId);
            WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(id, 2592000);


            String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
            Map<Object, Object> result = MapUtil.of(new String[][]{{"url", url}, {"id", id}});
            return BasicResultVO.success(result);
        } catch (Exception e) {
            log.error("OfficialAccountController#getQrCode fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
    }


}

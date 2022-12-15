package com.java3y.austin.web.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.CommonConstant;
import com.java3y.austin.common.constant.OfficialAccountParamConstant;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.common.vo.BasicResultVO;
import com.java3y.austin.support.utils.WxServiceUtils;
import com.java3y.austin.web.config.WeChatLoginConfig;
import com.java3y.austin.web.utils.Convert4Amis;
import com.java3y.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信服务号
 *
 * @author 3y
 */
@Slf4j
@RequestMapping("/officialAccount")
@RestController
@Api("微信服务号")
public class OfficialAccountController {

    @Autowired
    private WxServiceUtils wxServiceUtils;

    //@Autowired
    private WeChatLoginConfig configService;

    @Autowired
    private StringRedisTemplate redisTemplate;

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
    @RequestMapping(value = "/receipt", produces = {CommonConstant.CONTENT_TYPE_XML})
    @ApiOperation("/接收微信的事件消息")
    public String receiptMessage(HttpServletRequest request, HttpServletResponse response) {
        try {
            WxMpService wxMpService = configService.getOfficialAccountLoginService();

            String echoStr = request.getParameter(OfficialAccountParamConstant.ECHO_STR);
            String signature = request.getParameter(OfficialAccountParamConstant.SIGNATURE);
            String nonce = request.getParameter(OfficialAccountParamConstant.NONCE);
            String timestamp = request.getParameter(OfficialAccountParamConstant.TIMESTAMP);

            // echoStr!=null，说明只是微信调试的请求
            if (StrUtil.isNotBlank(echoStr)) {
                return echoStr;
            }

            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                return RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg();
            }

            String encryptType = StrUtil.isBlank(request.getParameter(OfficialAccountParamConstant.ENCRYPT_TYPE)) ? OfficialAccountParamConstant.RAW : request.getParameter(OfficialAccountParamConstant.ENCRYPT_TYPE);

            if (OfficialAccountParamConstant.RAW.equals(encryptType)) {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
                log.info("raw inMessage:{}", JSON.toJSONString(inMessage));
                WxMpXmlOutMessage outMessage = configService.getWxMpMessageRouter().route(inMessage);
                response.getWriter().write(outMessage.toXml());
            } else if (OfficialAccountParamConstant.AES.equals(encryptType)) {
                String msgSignature = request.getParameter(OfficialAccountParamConstant.MSG_SIGNATURE);
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), configService.getConfig(), timestamp, nonce, msgSignature);
                log.info("aes inMessage:{}", JSON.toJSONString(inMessage));
                WxMpXmlOutMessage outMessage = configService.getWxMpMessageRouter().route(inMessage);
                response.getWriter().write(outMessage.toEncryptedXml(configService.getConfig()));
            }
            return RespStatusEnum.SUCCESS.getMsg();
        } catch (Exception e) {
            log.error("OfficialAccountController#receiptMessage fail:{}", Throwables.getStackTraceAsString(e));
            return RespStatusEnum.SERVICE_ERROR.getMsg();
        }

    }

    /**
     * 临时给微信服务号登录使用（生成二维码），正常消息推送平台不会有此接口
     *
     * @return
     */
    @PostMapping("/qrCode")
    @ApiOperation("/生成 服务号 二维码")
    public BasicResultVO getQrCode() {
        try {
            String id = IdUtil.getSnowflake().nextIdStr();
            WxMpService wxMpService = configService.getOfficialAccountLoginService();
            WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(id, 2592000);
            String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
            return BasicResultVO.success(Convert4Amis.getWxMpQrCode(url));
        } catch (Exception e) {
            log.error("OfficialAccountController#getQrCode fail:{}", Throwables.getStackTraceAsString(e));
            return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
        }
    }

}

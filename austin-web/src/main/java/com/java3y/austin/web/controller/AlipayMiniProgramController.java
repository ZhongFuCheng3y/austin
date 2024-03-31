package com.java3y.austin.web.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.http.HttpUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.MerchantMsgTemplateVO;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.SendChanelUrlConstant;
import com.java3y.austin.common.dto.account.AlipayMiniProgramAccount;
import com.java3y.austin.common.enums.RespStatusEnum;
import com.java3y.austin.handler.config.AlipayClientSingleton;
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
import com.alipay.api.request.AlipayOpenMiniMessageTemplateBatchqueryRequest;
import com.alipay.api.response.AlipayOpenMiniMessageTemplateBatchqueryResponse;
import com.alipay.api.domain.AlipayOpenMiniMessageTemplateBatchqueryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 支付宝小程序
 *
 * @author 3y
 */
@Slf4j
@AustinAspect
@RestController
@RequestMapping("/alipayMiniProgram")
@Api("支付宝小程序")
public class AlipayMiniProgramController {

    @Autowired
    private AccountUtils accountUtils;

    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    @AustinResult
    public List<CommonAmisVo> queryList(Integer id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();
            AlipayMiniProgramAccount miniProgramAccount = accountUtils.getAccountById(id, AlipayMiniProgramAccount.class);
            AlipayClient client = AlipayClientSingleton.getSingleton(miniProgramAccount);

            // 构造请求参数以调用接口
            AlipayOpenMiniMessageTemplateBatchqueryRequest request = new AlipayOpenMiniMessageTemplateBatchqueryRequest();
            AlipayOpenMiniMessageTemplateBatchqueryModel model = new AlipayOpenMiniMessageTemplateBatchqueryModel();

            // 设置子板状态列表
            List<String> statusList = new ArrayList<String>();
            statusList.add("STARTED");
            model.setStatusList(statusList);

            // 设置消息业务类型
            model.setBizType("sub_msg");

            // 设置分页页号
            model.setPageNum(1L);

            // 设置分页大小
            model.setPageSize("1");

            request.setBizModel(model);
            AlipayOpenMiniMessageTemplateBatchqueryResponse response = client.execute(request);
            List<MerchantMsgTemplateVO> templateList = response.getTemplateList();
            for (MerchantMsgTemplateVO templateInfo : templateList) {
                CommonAmisVo commonAmisVo = CommonAmisVo.builder().label(templateInfo.getName()).value(templateInfo.getTemplateId()).build();
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
    public CommonAmisVo queryDetailList(Integer id, String alipayTemplateId) {
        if (Objects.isNull(id) || Objects.isNull(alipayTemplateId)) {
            log.info("id || wxTemplateId null! id:{},wxTemplateId:{}", id, alipayTemplateId);
            return CommonAmisVo.builder().build();
        }

        try {
            AlipayMiniProgramAccount miniProgramAccount = accountUtils.getAccountById(id, AlipayMiniProgramAccount.class);
            AlipayClient client = AlipayClientSingleton.getSingleton(miniProgramAccount);

            // 构造请求参数以调用接口
            AlipayOpenMiniMessageTemplateBatchqueryRequest request = new AlipayOpenMiniMessageTemplateBatchqueryRequest();
            AlipayOpenMiniMessageTemplateBatchqueryModel model = new AlipayOpenMiniMessageTemplateBatchqueryModel();

            // 设置子板状态列表
            List<String> statusList = new ArrayList<String>();
            statusList.add("STARTED");
            model.setStatusList(statusList);

            // 设置消息业务类型
            model.setBizType("sub_msg");

            // 设置分页页号
            model.setPageNum(1L);

            // 设置分页大小
            model.setPageSize("1");

            request.setBizModel(model);
            AlipayOpenMiniMessageTemplateBatchqueryResponse response = client.execute(request);
            List<MerchantMsgTemplateVO> templateList = response.getTemplateList();

            return Convert4Amis.getAlipayTemplateParam(alipayTemplateId, templateList);
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

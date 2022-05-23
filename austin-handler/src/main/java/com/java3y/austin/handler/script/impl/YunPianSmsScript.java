package com.java3y.austin.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.TencentSmsAccount;
import com.java3y.austin.common.dto.account.YunPianSmsAccount;
import com.java3y.austin.common.enums.SmsStatus;
import com.java3y.austin.handler.domain.sms.SmsParam;
import com.java3y.austin.handler.domain.sms.YunPianSendResult;
import com.java3y.austin.handler.script.SmsScript;
import com.java3y.austin.support.domain.SmsRecord;
import com.java3y.austin.support.utils.AccountUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 3y
 * @date 2022年5月23日
 * 发送短信接入文档：https://www.yunpian.com/official/document/sms/zh_CN/domestic_list
 */
//@Service
@Slf4j
public class YunPianSmsScript implements SmsScript {
    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecord> send(SmsParam smsParam) throws Exception {
        YunPianSmsAccount account = accountUtils.getAccount(smsParam.getSendAccount(), SendAccountConstant.SMS_ACCOUNT_KEY, SendAccountConstant.SMS_PREFIX, YunPianSmsAccount.class);

        Map<String, String> params = assembleParam(smsParam, account);

        String result = HttpRequest.post(account.getUrl())
                .header(Header.CONTENT_TYPE.getValue(), ContentType.FORM_URLENCODED.getValue())
                .header(Header.ACCEPT.getValue(), ContentType.JSON.getValue())
                .body(JSON.toJSONString(params))
                .timeout(2000)
                .execute().body();

        YunPianSendResult yunPianSendResult = JSON.parseObject(result, YunPianSendResult.class);

        return assembleSmsRecord(smsParam, yunPianSendResult, account);
    }

    /**
     * 组装参数
     * @param smsParam
     * @param account
     * @return
     */
    private Map<String, String> assembleParam(SmsParam smsParam, YunPianSmsAccount account) {
        Map<String, String> params = new HashMap<>();
        params.put("apikey", account.getApikey());
        params.put("mobile", StringUtils.join(smsParam.getPhones(), StrUtil.C_COMMA));
        params.put("tpl_id", account.getTplId());
        params.put("tpl_value", "");
        return params;
    }


    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, YunPianSendResult response, YunPianSmsAccount account) {
        if (response == null || ArrayUtil.isEmpty(response.getData())) {
            return null;
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();

        for (YunPianSendResult.DataDTO datum : response.getData()) {
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getMobile()))
                    .supplierId(account.getSupplierId())
                    .supplierName(account.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(String.valueOf(datum.getSid()))
                    .chargingNum(Math.toIntExact(datum.getCount()))
                    .status("0".equals(datum.getCode())?SmsStatus.SEND_SUCCESS.getCode():SmsStatus.SEND_FAIL.getCode())
                    .reportContent(datum.getMsg())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }

        return smsRecordList;
    }



}


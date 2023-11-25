package com.java3y.austin.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.dto.account.sms.TencentSmsAccount;
import com.java3y.austin.common.enums.SmsStatus;
import com.java3y.austin.handler.domain.sms.SmsParam;
import com.java3y.austin.handler.script.SmsScript;
import com.java3y.austin.support.domain.SmsRecord;
import com.java3y.austin.support.utils.AccountUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 3y
 * @date 2021/11/6
 * 1. 发送短信接入文档：https://cloud.tencent.com/document/api/382/55981
 * 2. 推荐直接使用SDK调用
 * 3. 推荐使用API Explorer 生成代码
 */

@Slf4j
@Component("TencentSmsScript")
public class TencentSmsScript implements SmsScript {

    private static final Integer PHONE_NUM = 11;

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecord> send(SmsParam smsParam) {
        try {
            TencentSmsAccount tencentSmsAccount = Objects.nonNull(smsParam.getSendAccountId()) ? accountUtils.getAccountById(smsParam.getSendAccountId(), TencentSmsAccount.class)
                    : accountUtils.getSmsAccountByScriptName(smsParam.getScriptName(), TencentSmsAccount.class);
            SmsClient client = init(tencentSmsAccount);
            SendSmsRequest request = assembleSendReq(smsParam, tencentSmsAccount);
            SendSmsResponse response = client.SendSms(request);
            return assembleSendSmsRecord(smsParam, response, tencentSmsAccount);
        } catch (Exception e) {
            log.error("TencentSmsScript#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return new ArrayList<>();
        }
    }

    @Override
    public List<SmsRecord> pull(Integer accountId) {
        try {
            TencentSmsAccount account = accountUtils.getAccountById(accountId, TencentSmsAccount.class);
            SmsClient client = init(account);
            PullSmsSendStatusRequest req = assemblePullReq(account);
            PullSmsSendStatusResponse resp = client.PullSmsSendStatus(req);
            return assemblePullSmsRecord(account, resp);
        } catch (Exception e) {
            log.error("TencentSmsReceipt#pull fail!{}", Throwables.getStackTraceAsString(e));
            return new ArrayList<>();
        }
    }

    /**
     * 组装 发送消息的 返回值
     *
     * @param smsParam
     * @param response
     * @param tencentSmsAccount
     * @return
     */
    private List<SmsRecord> assembleSendSmsRecord(SmsParam smsParam, SendSmsResponse response, TencentSmsAccount tencentSmsAccount) {

        List<SmsRecord> smsRecordList = new ArrayList<>();
        if (Objects.isNull(response) || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return smsRecordList;
        }

        for (SendStatus sendStatus : response.getSendStatusSet()) {

            // 腾讯返回的电话号有前缀，这里取巧直接翻转获取手机号
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUM)).reverse().toString();

            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(tencentSmsAccount.getSupplierId())
                    .supplierName(tencentSmsAccount.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .reportContent(sendStatus.getCode())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }
        return smsRecordList;
    }

    /**
     * 组装发送短信参数
     */
    private SendSmsRequest assembleSendReq(SmsParam smsParam, TencentSmsAccount account) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(account.getSmsSdkAppId());
        req.setSignName(account.getSignName());
        req.setTemplateId(account.getTemplateId());
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 初始化 client
     *
     * @param account
     */
    private SmsClient init(TencentSmsAccount account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getRegion(), clientProfile);
    }

    /**
     * 组装 拉取回执信息
     *
     * @param account
     * @param resp
     * @return
     */
    private List<SmsRecord> assemblePullSmsRecord(TencentSmsAccount account, PullSmsSendStatusResponse resp) {
        List<SmsRecord> smsRecordList = new ArrayList<>();
        if (Objects.nonNull(resp) && Objects.nonNull(resp.getPullSmsSendStatusSet()) && resp.getPullSmsSendStatusSet().length > 0) {
            for (PullSmsSendStatus pullSmsSendStatus : resp.getPullSmsSendStatusSet()) {
                SmsRecord smsRecord = SmsRecord.builder()
                        .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                        .messageTemplateId(0L)
                        .phone(Long.valueOf(pullSmsSendStatus.getSubscriberNumber()))
                        .supplierId(account.getSupplierId())
                        .supplierName(account.getSupplierName())
                        .msgContent("")
                        .seriesId(pullSmsSendStatus.getSerialNo())
                        .chargingNum(0)
                        .status("SUCCESS".equals(pullSmsSendStatus.getReportStatus()) ? SmsStatus.RECEIVE_SUCCESS.getCode() : SmsStatus.RECEIVE_FAIL.getCode())
                        .reportContent(pullSmsSendStatus.getDescription())
                        .updated(Math.toIntExact(pullSmsSendStatus.getUserReceiveTime()))
                        .created(Math.toIntExact(DateUtil.currentSeconds()))
                        .build();
                smsRecordList.add(smsRecord);
            }
        }
        return smsRecordList;
    }

    /**
     * 组装 拉取回执 入参
     *
     * @param account
     * @return
     */
    private PullSmsSendStatusRequest assemblePullReq(TencentSmsAccount account) {
        PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
        req.setLimit(10L);
        req.setSmsSdkAppId(account.getSmsSdkAppId());
        return req;
    }


}


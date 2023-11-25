package com.java3y.austin.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.dto.account.sms.LinTongSmsAccount;
import com.java3y.austin.common.enums.SmsStatus;
import com.java3y.austin.handler.domain.sms.LinTongSendMessage;
import com.java3y.austin.handler.domain.sms.LinTongSendResult;
import com.java3y.austin.handler.domain.sms.SmsParam;
import com.java3y.austin.handler.script.SmsScript;
import com.java3y.austin.support.domain.SmsRecord;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <span>Form File</span>
 * <p>Description</p>
 * <p>Company:QQ 752340543</p>
 *
 * @author topsuder
 * @version v1.0.0
 * @DATE 2022/11/24-14:29
 * @Description
 * @see com.java3y.austin.handler.script.impl austin
 */
@Slf4j
@Component("LinTongSmsScript")
public class LinTongSmsScript implements SmsScript {

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 发送短信
     *
     * @param smsParam
     * @return 渠道商接口返回值
     */
    @Override
    public List<SmsRecord> send(SmsParam smsParam) {

        try {
            LinTongSmsAccount linTongSmsAccount = accountUtils.getSmsAccountByScriptName(smsParam.getScriptName(), LinTongSmsAccount.class);
            String request = assembleReq(smsParam, linTongSmsAccount);
            String response = HttpRequest.post(linTongSmsAccount.getUrl()).body(request)
                    .header(Header.ACCEPT.getValue(), "application/json")
                    .header(Header.CONTENT_TYPE.getValue(), "application/json;charset=utf-8")
                    .timeout(2000)
                    .execute().body();
            LinTongSendResult linTongSendResult = JSON.parseObject(response, LinTongSendResult.class);
            return assembleSmsRecord(smsParam, linTongSendResult, linTongSmsAccount);
        } catch (Exception e) {
            log.error("LinTongSmsAccount#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return new ArrayList<>();
        }

    }

    /**
     * 拉取回执
     *
     * @param id 渠道账号的ID
     * @return 渠道商回执接口返回值
     */
    @Override
    public List<SmsRecord> pull(Integer id) {
        return new ArrayList<>();
    }


    /**
     * 组装发送短信参数
     */
    private String assembleReq(SmsParam smsParam, LinTongSmsAccount account) {
        Map<String, Object> map = new HashMap<>(5);
        final long time = DateUtil.date().getTime();
        String sign = SecureUtil.md5(account.getUserName() + time + SecureUtil.md5(account.getPassword()));
        map.put("userName", account.getUserName());
        //获取当前时间戳
        map.put("timestamp", time);
        List<LinTongSendMessage> linTongSendMessages = new ArrayList<>(smsParam.getPhones().size());
        for (String phone : smsParam.getPhones()) {
            linTongSendMessages.add(LinTongSendMessage.builder().phone(phone).content(smsParam.getContent()).build());
        }
        map.put("messageList", linTongSendMessages);
        map.put("sign", sign);
        return JSONUtil.toJsonStr(map);
    }

    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, LinTongSendResult response, LinTongSmsAccount account) {
        if (response == null || ArrayUtil.isEmpty(response.getDtoList())) {
            return new ArrayList<>();
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();

        for (LinTongSendResult.DataDTO datum : response.getDtoList()) {
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getPhone()))
                    .supplierId(account.getSupplierId())
                    .supplierName(account.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(datum.getMsgId().toString())
                    .chargingNum(1)
                    .status(datum.getCode() == 0 ? SmsStatus.SEND_SUCCESS.getCode() : SmsStatus.SEND_FAIL.getCode())
                    .reportContent(datum.getMessage())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }

        return smsRecordList;
    }

}

package com.java3y.austin.handler.receipt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.AustinConstant;
import com.java3y.austin.common.dto.account.TencentSmsAccount;
import com.java3y.austin.common.dto.account.YunPianSmsAccount;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.common.enums.SmsStatus;
import com.java3y.austin.common.enums.SmsSupplier;
import com.java3y.austin.support.dao.ChannelAccountDao;
import com.java3y.austin.support.dao.SmsRecordDao;
import com.java3y.austin.support.domain.ChannelAccount;
import com.java3y.austin.support.domain.SmsRecord;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatus;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 拉取短信回执信息
 *
 * @author 3y
 */
@Component
@Slf4j
public class SmsPullReceipt {

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @Autowired
    private SmsRecordDao smsRecordDao;

    /**
     * 拉取消息并入库
     * <p>
     * eg accountList：[{"sms_10":{"url":"sms.tencentcloudapi.com","region":"ap-guangzhou","secretId":"234234","secretKey":"234324324","smsSdkAppId":"2343242","templateId":"234234","signName":"Java3y公众号","supplierId":10,"supplierName":"腾讯云"}},{"sms_20":{"url":"https://sms.yunpian.com/v2/sms/tpl_batch_send.json","apikey":"23423432","tpl_id":"23423432","supplierId":20,"supplierName":"云片"}}]
     */
    public void pull() {
        List<ChannelAccount> channelAccountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(AustinConstant.FALSE, ChannelType.SMS.getCode());
        for (ChannelAccount channelAccount : channelAccountList) {
            Integer supplierId = JSON.parseObject(channelAccount.getAccountConfig()).getInteger("supplierId");
            if (SmsSupplier.TENCENT.getCode().equals(supplierId)) {
                TencentSmsAccount tencentSmsAccount = JSON.parseObject(channelAccount.getAccountConfig(), TencentSmsAccount.class);
                pullTencent(tencentSmsAccount);
            } else if (SmsSupplier.YUN_PAIN.getCode().equals(supplierId)) {
                YunPianSmsAccount yunPianSmsAccount = JSON.parseObject(channelAccount.getAccountConfig(), YunPianSmsAccount.class);
                pullYunPain(yunPianSmsAccount);
            }
        }
    }

    /**
     * 拉取腾讯的回执
     *
     * @param account
     */
    private void pullTencent(TencentSmsAccount account) {
        try {
            /**
             * 初始化客户端
             */
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(account.getUrl());
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(new Credential(account.getSecretId(), account.getSecretKey()), account.getRegion(), clientProfile);

            /**
             * 每次拉取10条
             */
            PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
            req.setLimit(10L);
            req.setSmsSdkAppId(account.getSmsSdkAppId());

            /**
             * 拉取回执后入库
             */
            PullSmsSendStatusResponse resp = client.PullSmsSendStatus(req);
            List<SmsRecord> smsRecordList = new ArrayList<>();
            if (resp != null && resp.getPullSmsSendStatusSet() != null && resp.getPullSmsSendStatusSet().length > 0) {
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
            if (!CollUtil.isEmpty(smsRecordList)) {
                smsRecordDao.saveAll(smsRecordList);
            }
        } catch (Exception e) {
            log.error("TencentSmsReceipt#init fail!{}", Throwables.getStackTraceAsString(e));
        }
    }

    private void pullYunPain(YunPianSmsAccount yunPianSmsAccount) {

    }
}

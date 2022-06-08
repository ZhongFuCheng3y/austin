package com.java3y.austin.handler.receipt;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.TencentSmsAccount;
import com.java3y.austin.common.enums.SmsStatus;
import com.java3y.austin.support.config.SupportThreadPoolConfig;
import com.java3y.austin.support.domain.SmsRecord;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatus;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusRequest;
import com.tencentcloudapi.sms.v20210111.models.PullSmsSendStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
public class MessageReceipt {

    @Autowired
    private TencentSmsReceipt tencentSmsReceipt;

    @Autowired
    private YunPianSmsReceipt yunPianSmsReceipt;

    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {

                // TODO 回执这里自行打开(免得报错)
//                tencentSmsReceipt.pull();
//                yunPianSmsReceipt.pull();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
        });
    }
}

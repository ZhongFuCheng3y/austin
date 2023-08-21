package com.java3y.austin.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.google.common.base.Throwables;
import com.java3y.austin.common.domain.RecallTaskInfo;
import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.account.AlipayMiniProgramAccount;
import com.java3y.austin.common.dto.model.AlipayMiniProgramContentModel;
import com.java3y.austin.common.enums.ChannelType;
import com.java3y.austin.handler.config.AlipayClientSingleton;
import com.java3y.austin.handler.domain.alipay.AlipayMiniProgramParam;
import com.java3y.austin.handler.handler.BaseHandler;
import com.java3y.austin.handler.handler.Handler;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jwq
 * 支付宝小程序发送订阅消息
 */
@Component
@Slf4j
public class AlipayMiniProgramAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public AlipayMiniProgramAccountHandler() {
        channelCode = ChannelType.ALIPAY_MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        AlipayMiniProgramParam miniProgramParam = buildMiniProgramParam(taskInfo);
        try {
            AlipayMiniProgramAccount miniProgramAccount = accountUtils.getAccountById(miniProgramParam.getSendAccount(), AlipayMiniProgramAccount.class);
            AlipayClient client = AlipayClientSingleton.getSingleton(miniProgramAccount);
            List<AlipayOpenAppMiniTemplatemessageSendRequest> request = assembleReq(miniProgramParam, miniProgramAccount);
            for (AlipayOpenAppMiniTemplatemessageSendRequest req : request) {
                client.execute(req);
            }
        } catch (Exception e) {
            log.error("AlipayMiniProgramAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(taskInfo));
            return false;
        }
        return true;
    }

    /**
     * 通过taskInfo构建小程序订阅消息
     *
     * @param taskInfo 任务信息
     * @return AlipayMiniProgramParam
     */
    private AlipayMiniProgramParam buildMiniProgramParam(TaskInfo taskInfo) {
        AlipayMiniProgramParam param = AlipayMiniProgramParam.builder()
                .toUserId(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();

        AlipayMiniProgramContentModel contentModel = (AlipayMiniProgramContentModel) taskInfo.getContentModel();
        param.setData(contentModel.getMap());
        return param;
    }

    /**
     * 组装模板消息的参数
     */
    private List<AlipayOpenAppMiniTemplatemessageSendRequest> assembleReq(AlipayMiniProgramParam alipayMiniProgramParam, AlipayMiniProgramAccount alipayMiniProgramAccount) {
        Set<String> receiver = alipayMiniProgramParam.getToUserId();
        List<AlipayOpenAppMiniTemplatemessageSendRequest> requestList = new ArrayList<>(receiver.size());

        for (String toUserId : receiver) {
            AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
            AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
            model.setToUserId(toUserId);
            model.setUserTemplateId(alipayMiniProgramAccount.getUserTemplateId());
            model.setPage(alipayMiniProgramAccount.getPage());
            model.setData(alipayMiniProgramParam.getData().toString());
            request.setBizModel(model);
            requestList.add(request);
        }
        return requestList;
    }

    @Override
    public void recall(RecallTaskInfo recallTaskInfo) {

    }
}

package com.java3y.austin.handler.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.java3y.austin.common.constant.SendAccountConstant;
import com.java3y.austin.common.dto.account.AlipayMiniProgramAccount;
import com.java3y.austin.handler.alipay.AlipayMiniProgramAccountService;
import com.java3y.austin.handler.domain.alipay.AlipayMiniProgramParam;
import com.java3y.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jwq
 * 支付宝小程序发送订阅消息实现
 */
@Service
@Slf4j
public class AlipayMiniProgramAccountServiceImpl implements AlipayMiniProgramAccountService {

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 发送订阅消息
     *
     * @param miniProgramParam 订阅消息参数
     * @throws AlipayApiException alipay异常
     */
    @Override
    public void send(AlipayMiniProgramParam miniProgramParam) throws AlipayApiException {
        AlipayMiniProgramAccount miniProgramAccount = accountUtils.getAccount(miniProgramParam.getSendAccount(),
                SendAccountConstant.ALIPAY_MINI_PROGRAM_ACCOUNT_KEY,
                SendAccountConstant.ALIPAY_MINI_PROGRAM_PREFIX,
                AlipayMiniProgramAccount.class);

        AlipayClient client = initService(miniProgramAccount);
        List<AlipayOpenAppMiniTemplatemessageSendRequest> request = assembleReq(miniProgramParam, miniProgramAccount);
        for(AlipayOpenAppMiniTemplatemessageSendRequest req : request){
            client.execute(req);
        }
    }

    /**
     * 组装模板消息的参数
     */
    private List<AlipayOpenAppMiniTemplatemessageSendRequest> assembleReq(AlipayMiniProgramParam alipayMiniProgramParam, AlipayMiniProgramAccount alipayMiniProgramAccount){
        Set<String> receiver = alipayMiniProgramParam.getToUserId();
        List<AlipayOpenAppMiniTemplatemessageSendRequest> requestList = new ArrayList<>(receiver.size());

        for(String toUserId : receiver){
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

    /**
     * 初始化支付宝小程序
     */
    private AlipayClient initService(AlipayMiniProgramAccount alipayMiniProgramAccount) throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi.alipaydev.com/gateway.do");
        alipayConfig.setAppId(alipayMiniProgramAccount.getAppId());
        alipayConfig.setPrivateKey(alipayMiniProgramAccount.getPrivateKey());
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(alipayMiniProgramAccount.getAlipayPublicKey());
        alipayConfig.setCharset("utf-8");
        alipayConfig.setSignType("RSA2");
        return new DefaultAlipayClient(alipayConfig);
    }


}

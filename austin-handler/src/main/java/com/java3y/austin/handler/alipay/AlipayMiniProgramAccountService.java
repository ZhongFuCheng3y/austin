package com.java3y.austin.handler.alipay;

import com.alipay.api.AlipayApiException;
import com.java3y.austin.handler.domain.alipay.AlipayMiniProgramParam;

/**
 * @author jwq
 * 支付宝小程序发送订阅消息接口
 */
public interface AlipayMiniProgramAccountService {
    /**
     * 发送订阅消息
     *
     * @param miniProgramParam 订阅消息参数
     * @throws AlipayApiException alipay异常
     */
    void send(AlipayMiniProgramParam miniProgramParam) throws AlipayApiException;
}

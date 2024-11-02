package com.java3y.austin.handler.loadbalance.base;

import com.java3y.austin.handler.domain.sms.MessageTypeSmsConfig;
import com.java3y.austin.handler.loadbalance.ServiceLoadBalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @Author Gavin
 * @Date 2024/9/14
 */
public abstract class BaseEnhancedServiceLoadBalancer implements ServiceLoadBalancer<MessageTypeSmsConfig> {


    /**
     * 根据权重重新生成服务元数据列表，权重越高的元数据，会在最终的列表中出现的次数越多
     * 例如，权重为1，最终出现1次，权重为2，最终出现2次，权重为3，最终出现3次，依此类推...
     */
    protected List<MessageTypeSmsConfig> getWeightMessageTypeSmsConfigList(List<MessageTypeSmsConfig> servers) {
        List<MessageTypeSmsConfig> list = new ArrayList<>();
        servers.forEach((server) -> {
            IntStream.range(0, server.getWeights()).forEach((i) -> {
                list.add(server);
            });
        });
        return list;
    }
}

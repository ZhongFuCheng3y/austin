package com.java3y.austin.handler.loadbalance.impl;

import com.java3y.austin.handler.domain.sms.MessageTypeSmsConfig;
import com.java3y.austin.handler.enums.LoadBalancerStrategy;
import com.java3y.austin.handler.loadbalance.annotations.LoadBalancer;
import com.java3y.austin.handler.loadbalance.base.BaseEnhancedServiceLoadBalancer;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Gavin
 * @Date 2024/9/14
 */
@Slf4j
@LoadBalancer(loadbalancer = LoadBalancerStrategy.SERVICE_LOAD_BALANCER_RANDOM_WEIGHT_ENHANCED)
public class RandomWeightEnhancedLoadBalancerImpl extends BaseEnhancedServiceLoadBalancer {

    /**
     * 安全随机数，重用性能与随机数质量更高
     */
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public MessageTypeSmsConfig selectOne(List<MessageTypeSmsConfig> servers) {
        if (servers == null || servers.isEmpty()){
            return null;
        }
        servers = this.getWeightMessageTypeSmsConfigList(servers);
        int index = secureRandom.nextInt(servers.size());
        return servers.get(index);
    }

    @Override
    public List<MessageTypeSmsConfig> select(List<MessageTypeSmsConfig> servers) {
        if (servers == null || servers.isEmpty()){
            return null;
        }
        int total = 0;
        for (MessageTypeSmsConfig channelConfig : servers) {
            total += channelConfig.getWeights();
        }
        // 生成一个随机数[1,total]，看落到哪个区间
        int index = secureRandom.nextInt(total) + 1;

        List<MessageTypeSmsConfig> selectedServers = new ArrayList<>();
        MessageTypeSmsConfig supplier = null;
        MessageTypeSmsConfig supplierBack = null;
        for (int i = 0; i < servers.size(); ++i) {
            if (index <= servers.get(i).getWeights()) {
                supplier = servers.get(i);

                // 取下一个供应商
                int j = (i + 1) % servers.size();
                if (i == j) {
                    return Collections.singletonList(supplier);
                }
                supplierBack = servers.get(j);
                return Arrays.asList(supplier, supplierBack);
            }
            index -= servers.get(i).getWeights();
        }
        return selectedServers;
    }
}

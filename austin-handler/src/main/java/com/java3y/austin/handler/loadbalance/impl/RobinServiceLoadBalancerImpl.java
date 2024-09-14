package com.java3y.austin.handler.loadbalance.impl;

import com.java3y.austin.handler.domain.sms.MessageTypeSmsConfig;
import com.java3y.austin.handler.enums.LoadBalancerStrategy;
import com.java3y.austin.handler.loadbalance.ServiceLoadBalancer;
import com.java3y.austin.handler.loadbalance.annotations.LoadBalancer;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Gavin
 * @Date 2024/9/14
 */
@Slf4j
@LoadBalancer(loadbalancer = LoadBalancerStrategy.SERVICE_LOAD_BALANCER_ROBIN)
public class RobinServiceLoadBalancerImpl implements ServiceLoadBalancer<MessageTypeSmsConfig> {
    private volatile AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public MessageTypeSmsConfig selectOne(List<MessageTypeSmsConfig> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        int count = servers.size();
        int index = atomicInteger.incrementAndGet();
        if (index >= (Integer.MAX_VALUE - 10000)) {
            // 当AtomicInteger递增后的值大于或者等于Integer的最大值减去10000时，会将AtomicInteger的值重置为0。
            // 这里之所以是大于或者等于Integer的最大值减去10000，是为了避免在高并发环境下由于竞态条件问题导致AtomicInteger自增后的值超过Integer的最大值，从而发生范围越界的问题。
            atomicInteger.set(0);
        }
        return servers.get(index % count);
    }

    @Override
    public List<MessageTypeSmsConfig> select(List<MessageTypeSmsConfig> servers) {
        if (servers == null || servers.isEmpty()) {
            return null;
        }
        if (servers.size() == 1) {
            return servers;
        }
        int count = servers.size();
        int index = atomicInteger.incrementAndGet();
        if (index >= (Integer.MAX_VALUE - 10000)) {
            atomicInteger.set(0);
        }
        int currentIndex = index % count;
        int nextIndex = (index + 1) % count;
        return Arrays.asList(servers.get(currentIndex), servers.get(nextIndex));
    }
}

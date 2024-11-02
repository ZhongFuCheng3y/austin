package com.java3y.austin.handler.loadbalance;

import com.java3y.austin.handler.loadbalance.annotations.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Gavin
 * @Date 2024/9/14
 */
@Service
@Slf4j
public class ServiceLoadBalancerFactory<T>  implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Map<String, ServiceLoadBalancer<T>> serviceLoadBalancerMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<T> selectService(List<T> servers, String loadbalancerStrategy) {
        ServiceLoadBalancer<T> serviceLoadBalancer = serviceLoadBalancerMap.get(loadbalancerStrategy);
        if (Objects.isNull(serviceLoadBalancer)) {
            log.error("没有找到对应的负载均衡策略");
            return servers;
        }
        return serviceLoadBalancer.select(servers);
    }

    @PostConstruct
    private void init() {
        Map<String, Object> serviceMap = this.applicationContext.getBeansWithAnnotation(LoadBalancer.class);
        serviceMap.forEach((name, service) -> {
            if (service instanceof ServiceLoadBalancer) {
                LoadBalancer LoadBalancer = AopUtils.getTargetClass(service).getAnnotation(LoadBalancer.class);
                String loadbalancerStrategy = LoadBalancer.loadbalancer();
                //通常情况下 实现的负载均衡service与loadBalanceStrategy一一对应
                serviceLoadBalancerMap.put(loadbalancerStrategy, (ServiceLoadBalancer) service);
            }
        });
    }
}

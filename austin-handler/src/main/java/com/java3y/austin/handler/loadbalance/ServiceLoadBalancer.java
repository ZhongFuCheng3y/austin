package com.java3y.austin.handler.loadbalance;

import java.util.List;

/**
 * @Author Gavin
 * @Date 2024/9/14
 */
public interface ServiceLoadBalancer<T> {

    /**
     * 以负载均衡的方式选取一个服务节点
     * @param servers 服务列表
     * @return 可用的服务节点
     */
    T selectOne(List<T> servers);

    /**
     * 以负载均衡的方式选取一个服务节点和一个备选服务节点
     * @param servers 服务列表
     * @return 可用的服务节点
     */
    List<T> select(List<T> servers);
}


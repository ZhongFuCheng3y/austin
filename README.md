## 01、项目介绍

austin项目**核心功能**：发送消息

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvztdk1w2wj30ky0p8759.jpg)

**项目出现意义**：只要公司内有发送消息的需求，都应该要有类似`austin`的项目，对各类消息进行统一发送处理。这有利于对功能的

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzz1vifljj31vc0u07dr.jpg)

## 02、项目流程图

austin项目**核心流程**：`austin-api`接收到发送消息请求，直接将请求进`MQ`。`austin-handler`消费`MQ`消息后由各类消息的Handler进行发送处理

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzwltzsdfj31ku0u0q5r.jpg)

**Question 1** ：为什么发个消息需要MQ？

**Answer 1**：发送消息实际上是调用各个服务提供的API，假设某消息的服务超时，`austin-api`如果是直接调用服务，那存在**超时**风险，拖垮整个接口性能。MQ在这是为了做异步和解耦，并且在一定程度上抗住业务流量。

**Question 2**：`austin-stream`和`austin-datahourse`的作用？

**Answer 2**：`austin-handler`在发送消息的过程中会做些**通用业务处理**以及**发送消息**，这个过程会产生大量的日志数据。日志数据会被收集至MQ，由`austin-stream`流式处理模块进行消费并最后将数据写入至`austin-datahourse`

**Question 3**：`austin-admin`和`austin-cron`的作用？

**Answer 3**：`autsin-admin`是`austin`项目的**管理后台**，负责管理消息以及查看消息下发的情况。业务方可根据通过`austin-admin`管理后台直接**定时**发送消息，而`austin-cron`就是承载着定时任务的工作了。

## 03、项目技术架构图

2021-11~2021-12实现功能：

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzx4f1iwoj31le0rs0uy.jpg)

实现功能所需引入的技术栈：

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzxrppbb0j30ym0iaq8v.jpg)

未完待续






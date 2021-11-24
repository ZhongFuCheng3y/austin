![](https://tva1.sinaimg.cn/large/008i3skNgy1gwn3mgftzaj30p00an0t1.jpg)

<p align="center">
  <a href="#公众号"><img src="https://img.shields.io/badge/Author-3y-orange.svg" alt="作者"></a>
  <a href="#公众号"><img src="https://gitee.com/zhongfucheng/austin/badge/star.svg?theme=dark" alt="作者"></a>
  <a href="https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzI4Njg5MDA5NA==&action=getalbum&album_id=2140193116740517888&scene=126#wechat_redirect"><img src="https://img.shields.io/badge/austin-系列文章-critical.svg" alt="Java3y"></a>
  <a href="#公众号"><img src="https://img.shields.io/badge/如何准备-面试-yellow.svg" alt="对线面试官"></a>
  
</p>


## 项目介绍

austin项目**核心功能**：发送消息

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvztdk1w2wj30ky0p8759.jpg)

**项目出现意义**：只要公司内有发送消息的需求，都应该要有类似`austin`的项目，对各类消息进行统一发送处理。这有利于对功能的收拢，以及提高业务需求开发的效率

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzz1vifljj31vc0u07dr.jpg)

## 系统项目架构

austin项目**核心流程**：`austin-api`接收到发送消息请求，直接将请求进`MQ`。`austin-handler`消费`MQ`消息后由各类消息的Handler进行发送处理

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzwltzsdfj31ku0u0q5r.jpg)

**Question 1** ：为什么发个消息需要MQ？

**Answer 1**：发送消息实际上是调用各个服务提供的API，假设某消息的服务超时，`austin-api`如果是直接调用服务，那存在**超时**风险，拖垮整个接口性能。MQ在这是为了做异步和解耦，并且在一定程度上抗住业务流量。

**Question 2**：`austin-stream`和`austin-datahourse`的作用？

**Answer 2**：`austin-handler`在发送消息的过程中会做些**通用业务处理**以及**发送消息**，这个过程会产生大量的日志数据。日志数据会被收集至MQ，由`austin-stream`流式处理模块进行消费并最后将数据写入至`austin-datahourse`

**Question 3**：`austin-admin`和`austin-cron`的作用？

**Answer 3**：`autsin-admin`是`austin`项目的**管理后台**，负责管理消息以及查看消息下发的情况。业务方可根据通过`austin-admin`管理后台直接**定时**发送消息，而`austin-cron`就是承载着定时任务的工作了。

## 将要实现的项目架构模块

2021-11~2021-12实现功能：

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzx4f1iwoj31le0rs0uy.jpg)

实现功能所需引入的技术栈：

![](https://tva1.sinaimg.cn/large/008i3skNgy1gvzxrppbb0j30ym0iaq8v.jpg)

## 已完成内容

截止11-21号：

- Maven+SpringBoot项目搭建
- logback日志记录项目运行时信息
- 引入common/guava/Hutool/Lombok/fastjson/OkHttp工具包
- 接入腾讯云渠道发送一条短信
- 使用SpringData JPA将短信发送记录存储至MySQL
- 完成对Kafka接入



**Java3y**公众号在持续更新austin系列文章，**保姆级**讲解搭建项目的过程（包括技术选型以及一些业务的探讨）以及相关环境的搭建。**扫下面的码直接关注，带你了解整个项目**



如果你需要用这个项目写在简历上，**强烈建议关注公众号看实现细节的思路**。如果⽂档中有任何的不懂的问题，都可以直接来找我询问，我乐意帮助你们！公众号下有我的联系方式

<img src='https://tva1.sinaimg.cn/large/006tNbRwly1gb0nzpn8z7g30go0gokbp.gif' width=200px height=200px />

## 如何准备面试？

**对线面试官**公众号持续更新**面试系列**文章（对线面试官系列），深受各大开发的好评，已有不少的同学通过对线面试官系列得到BATTMD等一线大厂的的offer。一个**讲人话的面试系列**，八股文不再是背诵。

<img align="center" src='https://tva1.sinaimg.cn/large/008i3skNgy1gtlvty8zo5j60u00u0q5602.jpg' width=300px height=300px />


如果你觉得有帮助，想要打赏作者，那么可以通过这个收款码打赏我，**金额不重要，心意最重要**。主要是我可以通过这个打赏情况来看我的心血有没有价值。

<img src='https://tva1.sinaimg.cn/large/00831rSTly1gcuu0j4wwqj30u014qako.jpg' width=200px height=200px /><img src='https://tva1.sinaimg.cn/large/00831rSTly1gcuuckh3s4j30rc0yo7jr.jpg' width=200px height=200px />




![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2ccd794db76e482680f72d60959cf368~tplv-k3u1fbpfcp-zoom-1.image)

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/Author-3y-orange.svg" alt="作者"></a>
  <a href="https://gitee.com/zhongfucheng/austin"><img src="https://gitee.com/zhongfucheng/austin/badge/star.svg?theme=dark" alt="gitee Starts"></a>
  <a href="https://gitee.com/zhongfucheng/austin"><img src="https://gitee.com/zhongfucheng/austin/badge/fork.svg?theme=dark" alt="gitee Starts"></a>
  <a href="https://github.com/ZhongFuCheng3y/austin"><img src="https://img.shields.io/github/forks/ZhongFuCheng3y/austin.svg?style=flat&label=GithubFork"></a> 
  <a href="https://github.com/ZhongFuCheng3y/austin"><img src="https://img.shields.io/github/stars/ZhongFuCheng3y/austin.svg?style=flat&label=GithubStars"></a>
  <a href="https://github.com/ZhongFuCheng3y/austin-admin"><img src="https://img.shields.io/badge/austin前端-GitHub-green.svg" alt="作者"></a>
  <a href="#项目交流"><img src="https://img.shields.io/badge/项目-交流-red.svg" alt="项目交流"></a>
  <a href="#如何准备面试"><img src="https://img.shields.io/badge/如何准备-面试-yellow.svg" alt="对线面试官"></a>
</p>



## 项目介绍

austin项目**核心功能**：统一的接口发送各种类型消息，对消息生命周期全链路追踪

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5436b2e3d6cd471db9aafbd436198ca7~tplv-k3u1fbpfcp-zoom-1.image)

**项目出现意义**：只要公司内有发送消息的需求，都应该要有类似`austin`的项目，对各类消息进行统一发送处理。这有利于对功能的收拢，以及提高业务需求开发的效率

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c267ebb2ff234243b8665312dbb46310~tplv-k3u1fbpfcp-zoom-1.image)

## 系统项目架构

austin项目**核心流程**：`austin-api`接收到发送消息请求，直接将请求进`MQ`。`austin-handler`消费`MQ`消息后由各类消息的Handler进行发送处理


![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b5d4dfde0f164805a6e85a86498b0cd7~tplv-k3u1fbpfcp-watermark.image?)

**Question** ：为什么发个消息需要MQ？

**Answer**：发送消息实际上是调用各个服务提供的API，假设某消息的服务超时，`austin-api`如果是直接调用服务，那存在**超时**风险，拖垮整个接口性能。MQ在这是为了做异步和解耦，并且在一定程度上抗住业务流量。

**Question**：能简单说下接入层做了什么事吗？

**Answer**：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c94059a008784a69bd10b98caa46d683~tplv-k3u1fbpfcp-zoom-1.image)

**Question**：`austin-stream`和`austin-datahouse`的作用？

**Answer**：`austin-handler`在发送消息的过程中会做些**通用业务处理**以及**发送消息**，这个过程会产生大量的日志数据。日志数据会被收集至MQ，由`austin-stream`流式处理模块进行消费并最后将数据写入至`austin-datahouse`

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e4bd420001c549ebad922637f7b2e38a~tplv-k3u1fbpfcp-zoom-1.image)

**Question**：`austin-admin`和`austin-web`和`austin-cron`的作用？

**Answer**：`autsin-admin`是`austin`项目的前端项目，可通过它实现对管理消息以及查看消息下发的情况，而`austin-web`则是提供相关的接口给到`austin-admin`进行调用（austin项目是前后端分离的）

业务方可操作`austin-admin`管理后台调用`austin-web`创建**定时**发送消息，`austin-cron`就承载着定时任务处理的工作

## 使用姿势

目前引用的中间件教程的安装姿势均基于`Centos 7.6`(**完全部署所有的服务，大概8G内存**)，austin项目**强依赖**`MySQL`/`Redis`/`Kafka`/`apollo`，**弱依赖**`prometheus`/`graylog`/`flink`/`xxl-job`。如果缺少相关的组件可戳：[安装相关组件教程](INSTALL.md)。


**1**、austin使用的MySQL版本**5.7x**。如果目前使用的MySQL版本8.0，注意改变`pom.xml`所依赖的版本

**2**、填写`application.properties`中`austin-database`对应的`ip/port/username/password`信息

**3**、执行`sql`文件夹下的`austin.sql`创建对应的表以及插入测试数据

**4**、填写`application.properties`中`austin-kafka`对应的`ip`/`port`信息

**5**、填写`application.properties`中`austin-redis`对应的`ip`/`port`信息

**6**、检查`apollo`的`app.id`/`apollo.bootstrap.namespaces`，检查创建的消息队列topic：`austin.business.topic.name`

**7**、以上配置信息都在`application.properties`文件中修改。

**8**、由于使用了Apollo且我是在云服务器上安装的，我这边会直接跳过`metaserver`服务发现，在`AustinApplication`需要配置对应的apollo地址(注意端口!)

**9**、发送渠道**账号的信息**都配置在**apollo**，配置的示例参照`com.java3y.austin.support.utils#getAccount`中的注释

**10**、调用http接口`com.java3y.austin.web.controller#send`给自己发一条短信或者邮件感受

```shell
curl -XPOST "127.0.0.1:8080/send"  -H 'Content-Type: application/json'  -d '{"code":"send","messageParam":{"extra":null,"receiver":"13719333899"},"messageTemplateId":1}'
```

**11**、austin前端管理系统部署（一分钟即能打开），戳[GitHub](https://github.com/ZhongFuCheng3y/austin-admin)或[Gitee](https://gitee.com/zhongfucheng/austin-admin)查看 

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a023d9082fa644bda9b50144e02985cb~tplv-k3u1fbpfcp-zoom-1.image) 

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7125184e9fbf4de8b522aecbd4e791df~tplv-k3u1fbpfcp-zoom-1.image)

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4adde725eeee443baf96f286f5429f05~tplv-k3u1fbpfcp-zoom-1.image)

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/01d028359e6e4229825a7fd8cc22c6c7~tplv-k3u1fbpfcp-zoom-1.image)

**12**、正常使用**数据管理**(查看实时数据链路下发)需要将`austin-stream`的`jar`包上传至`Flink`，根据[部署文档](INSTALL.md)启动Flink。在打`jar`包前需要填写`com.java3y.austin.stream.constants.AustinFlinkConstant`中的`redis`和`kafka`的`ip/port`（注：日志的topic在`application.properties`中的`austin.business.log.topic.name`。如果没有该topic，需要提前创建)

**13**、正常使用**定时任务**需要部署`xxl-job`，根据[部署文档](INSTALL.md)启动xxl的调度中心，并在`application.properteis`中填写  `austin-xxl-job-ip`和`austin-xxl-job-port`

**14**、正常使用**分布式日志采集**需要部署`graylog`，根据[部署文档](INSTALL.md)启动`graylog`，并在`application.properteis`中填写  `austin-grayLog-ip`

**14**、正常使用**系统监控**需要部署`promethus`和`grafana`，根据[部署文档](INSTALL.md)配置`grafana`图表

## 里程碑

- [x] Maven+SpringBoot项目搭建
- [x] logback日志记录项目运行时信息，引入common/guava/Hutool/Lombok/fastjson/OkHttp工具包
- [x] 接入腾讯云渠道发送一条短信
- [x] 使用SpringData JPA将短信发送记录存储至MySQL
- [x] 使用SpringBoot接入Kafka
- [x] 利用责任链完成对接入层的请求进行封装（责任链模式）
- [x] 消费层实现数据隔离（线程池：生产者与消费者模式）
- [x] 通用去重消息功能（SpringBoot接入Redis并使用pipeline减少网络请求）
- [x] 配置服务器和docker容器以及SpringBoot应用的监控（prometheus+Grafana+auctuator）
- [x] 接入分布式配置中心完成 丢失消息、白名单以及账号配置（Apollo分布式配置中心）
- [x] 邮件渠道接入
- [x] 日志链路数据追踪 + 注解式打印日志（优雅打印日志以及数据埋点）
- [x] 接入GrayLog分布式日志收集框架
- [x] 引入前端低代码平台AMIS搭建后台管理页面
- [x] 接入分布式定时任务框架定时发送任务（xxl-job定时任务框架），编写上传文件接口并使用LazyPending批处理人群文件数据
- [x] 接入实时流计算平台（Flink），实时日志数据根据用户维度和消息模板维度清洗至Redis
- [x] 通过AMIS低代码平台接入echarts图表展示实时聚合后的数据
- [x] 优雅停机、动态线程池参数配置
- [x] 企业微信渠道接入
- [x] 夜间屏蔽次日早晨推送（xxl-job定时任务框架，另类的延时队列）
- [x] 钉钉渠道接入
- [ ] 编写单测
- [ ] 持续提高消息推送系统的影响力，让更多的业务方了解其功能，进而挖掘更多拉新和唤醒用户的玩法，提高站内的次留率和转化率
- [ ] 优化代码
- [ ] 接入微信服务号渠道
- [ ] 接入微信小程序渠道
- [ ] 接入PUSH渠道
- [ ] 接入工作流引擎实现对消息工单审核


**近期更新时间**：2022年3月30日

**近期更新功能**：钉钉群自定义机器人与工作消息渠道接入完成

## 项目交流

欢迎扫描下方二维码添加我的**个人微信**备注：【**项目**】，我会空闲的时候拉进项目交流群（群里会解答相关的问题）


<img align="center" src='https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5eae548196934599a7cb3637aedf381d~tplv-k3u1fbpfcp-zoom-1.image' width=300px height=300px />

**Java3y**公众号在持续更新austin系列文章，**保姆级**讲解搭建项目的过程（包括技术选型以及一些业务的探讨）以及相关环境的搭建。**扫下面的码直接关注，带你了解整个项目**


如果你需要用这个项目写在简历上，**强烈建议关注公众号看实现细节的思路**。如果⽂档中有任何的不懂的问题，都可以直接来找我询问，我乐意帮助你们！公众号下有我的联系方式

<img align="center" src='https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4e109cdb8d064c1e87541d7b6c17957d~tplv-k3u1fbpfcp-zoom-1.image' width=300px height=300px />

## 如何准备面试？

**对线面试官**公众号持续更新**面试系列**文章（对线面试官系列），深受各大开发的好评，已有不少的同学通过对线面试官系列得到BATTMD等一线大厂的的offer。一个**讲人话的面试系列**，八股文不再是背诵。


![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f48cee2bbd44476f93dfcdd8aaf8a4eb~tplv-k3u1fbpfcp-watermark.image?)

想要获取这份电子书，**点击关注**下方公众号，回复「**对线**」得到我的联系方式即可进群获取电子书

<img align="center" src='https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f87f574e93964921a4d02146bf3ccdac~tplv-k3u1fbpfcp-zoom-1.image' width=300px height=300px />
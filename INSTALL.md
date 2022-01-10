

## 01、安装MySQL

**一**、下载并安装mysql：

```
wget -i -c http://dev.mysql.com/get/mysql57-community-release-el7-10.noarch.rpm
yum -y install mysql57-community-release-el7-10.noarch.rpm
yum -y install mysql-community-server
```

**二**、启动并查看状态MySQL：

```
systemctl start  mysqld.service
systemctl status mysqld.service
```

**三**、查看MySQL的默认密码：

```
grep "password" /var/log/mysqld.log
```

[![img](https://tva1.sinaimg.cn/large/008i3skNgy1gwg6eiwyqfj313402mgm8.jpg)](https://tva1.sinaimg.cn/large/008i3skNgy1gwg6eiwyqfj313402mgm8.jpg)

**四**、登录进MySQL

```
mysql -uroot -p
```

**五**、修改默认密码（设置密码需要有大小写符号组合—安全性)，把下面的`my passrod `替换成自己的密码

```
ALTER USER 'root'@'localhost' IDENTIFIED BY 'my password';
```

**六**、开启远程访问 (把下面的`my passrod `替换成自己的密码)

```
grant all privileges on *.* to 'root'@'%' identified by 'my password' with grant option;

flush privileges;

exit
```

**七**、在云服务上增加MySQL的端口

## 02、安装Docker和Docker-compose

首先我们需要安装GCC相关的环境：

```
yum -y install gcc

yum -y install gcc-c++
```

安装Docker需要的依赖软件包：

```
yum install -y yum-utils device-mapper-persistent-data lvm2
```

设置国内的镜像（提高速度）

```
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

更新yum软件包索引：

```
yum makecache fast
```

安装DOCKER CE(注意：Docker分为CE版和EE版，一般我们用CE版就够用了)

```
yum -y install docker-ce
```

启动Docker：

```
systemctl start docker
```

下载回来的Docker版本：:

```
docker version
```

运行以下命令以下载 Docker Compose 的当前稳定版本：

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

将可执行权限应用于二进制文件：

```
sudo chmod +x /usr/local/bin/docker-compose
```

创建软链：

```
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

测试是否安装成功：

```
docker-compose --version
```

## 03、安装Kafka

新建搭建kafka环境的`docker-compose.yml`文件，内容如下：

```yml
version: '3'
services:
  zookepper:
    image: wurstmeister/zookeeper                    # 原镜像`wurstmeister/zookeeper`
    container_name: zookeeper                        # 容器名为'zookeeper'
    volumes:                                         # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "/etc/localtime:/etc/localtime"
    ports:                                           # 映射端口
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka                                # 原镜像`wurstmeister/kafka`
    container_name: kafka                                    # 容器名为'kafka'
    volumes:                                                 # 数据卷挂载路径设置,将本机目录映射到容器目录
      - "/etc/localtime:/etc/localtime"
    environment:                                                       # 设置环境变量,相当于docker run命令中的-e
      KAFKA_BROKER_ID: 0                                               # 在kafka集群中，每个kafka都有一个BROKER_ID来区分自己
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://ip:9092 # TODO 将kafka的地址端口注册给zookeeper
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092                        # 配置kafka的监听端口
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181                
      KAFKA_CREATE_TOPICS: "hello_world"
    ports:                              # 映射端口
      - "9092:9092"
    depends_on:                         # 解决容器依赖启动先后问题
      - zookepper

  kafka-manager:
    image: sheepkiller/kafka-manager                         # 原镜像`sheepkiller/kafka-manager`
    container_name: kafka-manager                            # 容器名为'kafka-manager'
    environment:                        # 设置环境变量,相当于docker run命令中的-e
      ZK_HOSTS: zookeeper:2181 
      APPLICATION_SECRET: xxxxx
      KAFKA_MANAGER_AUTH_ENABLED: "true"  # 开启kafka-manager权限校验
      KAFKA_MANAGER_USERNAME: admin       # 登陆账户
      KAFKA_MANAGER_PASSWORD: 123456      # 登陆密码
    ports:                              # 映射端口
      - "9000:9000"
    depends_on:                         # 解决容器依赖启动先后问题
      - kafka
```

文件内**// TODO 中的ip**需要改成自己的，并且如果你用的是云服务器，那需要把端口给打开。

在存放`docker-compose.yml`的目录下执行启动命令：

```
docker-compose up -d
```

可以查看下docker镜像运行的情况：

```
docker ps 
```

进入kafka 的容器：

```
docker exec -it kafka sh
```

创建一个topic(这里我的**topicName**就叫austinTopic，你们可以改成自己的)

```
$KAFKA_HOME/bin/kafka-topics.sh --create --topic austinTopic --partitions 4 --zookeeper zookeeper:2181 --replication-factor 1 
```

查看刚创建的topic信息：

```
$KAFKA_HOME/bin/kafka-topics.sh --zookeeper zookeeper:2181 --describe --topic austinTopic
```

## 04、安装Redis

首先，我们新建一个文件夹`redis`，然后在该目录下创建出`data`文件夹、`redis.conf`文件和`docker-compose.yaml`文件

`redis.conf`文件的内容如下(后面的配置可在这更改，比如requirepass 我指定的密码为`austin`)

```Java
protected-mode no
port 6379
timeout 0
save 900 1 
save 300 10
save 60 10000
rdbcompression yes
dbfilename dump.rdb
dir /data
appendonly yes
appendfsync everysec
requirepass austin
```

`docker-compose.yaml`的文件内容如下：

```yaml
version: '3'
services:
  redis:
    image: redis:latest
    container_name: redis
    restart: always
    ports:
      - 6379:6379
    volumes:
      - ./redis.conf:/usr/local/etc/redis/redis.conf:rw
      - ./data:/data:rw
    command:
      /bin/bash -c "redis-server /usr/local/etc/redis/redis.conf "
```

配置的工作就完了，如果是云服务器，记得开redis端口**6379**

启动Redis跟之前安装Kafka的时候就差不多啦

```shell
docker-compose up -d

docker ps

docker exec -it redis redis-cli
```

## 05、安装Apollo

部署Apollo跟之前一样直接用`docker-compose`就完事了，在GitHub已经给出了对应的教程和`docker-compose.yml`以及相关的文件，直接复制粘贴就完事咯。

**https://www.apolloconfig.com/#/zh/deployment/quick-start-docker**

**https://github.com/apolloconfig/apollo/tree/master/scripts/docker-quick-start**

部门的创建其实也是一份"配置"，输入`organizations`就能把现有的部门给改掉，我新增了`boss`股东部门，大家都是我的股东。

![](https://tva1.sinaimg.cn/large/008i3skNgy1gy8usif8ipj31n80iimyq.jpg)
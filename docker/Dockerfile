# 使用openjdk8的镜像
FROM openjdk:8-jre

ENV PARAMS=""

# 设置工作目录
WORKDIR /build
# 将jar包复制到容器中
ADD ./austin-web-0.0.1-SNAPSHOT.jar ./austin.jar
# 暴露8080端口
EXPOSE 8080

# 运行jar包
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS austin.jar $PARAMS"]

# docker build -t austin:0.1 .
# docker run -e PARAMS="--austin.database.ip= --austin.database.port=3306 --austin.redis.ip= --austin.mq.pipeline=eventbus  " -p 8080:8080 --name austin:0.1

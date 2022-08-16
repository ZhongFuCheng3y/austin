# 使用openjdk8的镜像
FROM openjdk:8-jre

ENV PARAMS=""

# 设置工作目录
WORKDIR /build
# 将jar包复制到容器中
ADD ./austin-web/target/austin-web-0.0.1-SNAPSHOT.jar ./austin.jar
# 暴露8080端口
EXPOSE 8080

# 运行jar包
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS austin.jar $PARAMS"]

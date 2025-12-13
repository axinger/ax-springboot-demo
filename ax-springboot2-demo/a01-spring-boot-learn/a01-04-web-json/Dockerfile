# 基础镜像使用java
#FROM openjdk:8-jdk-alpine
FROM eclipse-temurin:17-jre-alpine
#FROM eclipse-temurin:17-jre-jammy # bin/bash 有 mkdir命令
#作者
MAINTAINER 阿星

# volume 指定临时文件目录为/tmp, 在主机/var/lib/docker 目录下创建一个临时文件夹并链接到容器的/tmp
VOLUME /tmp

#将本地文件添加到容器中
COPY /target/*.jar /target/server.jar
#配置容器，使其可执行化
# 自动识别config/application.yml
ENTRYPOINT ["java","-jar","/target/server.jar"]
#配置时区，不然会发现打包到docker中启动的容器日志里的时间是差8个小时的
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo 'Asia/Shanghai' >/etc/timezone

# 暴露端口,需要和服务的端口一致
EXPOSE 8080

# 命令运行
# restart=always; privileged=true; network=demo-network
# idea配置项
# --restart=always --privileged=true --network=demo-network

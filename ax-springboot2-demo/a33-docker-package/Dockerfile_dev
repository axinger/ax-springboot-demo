# 基础镜像使用java
#对于 JDK 镜像的选择，可以根据实际需求和特征进行权衡。
#eclipse-temurin:17-jre-jammy 是基于 JAMMA 标准的 JDK 版本，它提供了更加丰富、完善的 Java 模块化支持，以及一些新的语言特性。此外，它还包括了 Eclipse OpenJ9 虚拟机，并采用优化过的算法来提高应用程序的性能和响应速度。
#相反，eclipse-temurin:17-jre-alpine 则是一个基于 Alpine Linux 发行版的 JDK 镜像，它使用较轻量的 base image，并且在资源占用方面表现得更好，因此适合于资源受限的环境中。
#通常而言，如果您需要最新的 Java 技术和性能优化，请选择 eclipse-temurin:17-jre-jammy 这样的 JDK 镜像。但在某些情况下，如需求有限、资源受限或考虑 Docker 映像的大小等因素，则可以选择 eclipse-temurin:17-jre-alpine。


FROM eclipse-temurin:17-jdk
#FROM eclipse-temurin:17-jdk-jammy
#FROM eclipse-temurin:17-jdk-centos7
#FROM eclipse-temurin:17-jdk-focal
#FROM eclipse-temurin:17-jdk-alpine


#FROM eclipse-temurin:17-jre
#FROM eclipse-temurin:8-jre
# bin/bash 有 mkdir命令
#FROM eclipse-temurin:17-jre-jammy
#FROM eclipse-temurin:17-jre-focal
#FROM eclipse-temurin:17-jre-centos7
#FROM eclipse-temurin:17-jre-alpine

#作者
MAINTAINER 阿星

# volume 指定临时文件目录为/tmp, 在主机/var/lib/docker 目录下创建一个临时文件夹并链接到容器的/tmp
VOLUME /logs
VOLUME /data
#ENV APP_NAME=server.jar

#设置工作目录，设置了工作目录，它将会作用于该指令后面的 RUN, CMD, ENTRYPOINT, COPY , ADD 指令
#WORKDIR /app

# 安装lsof
#RUN apt-get update && apt-get install -y lsof


RUN bash -c 'mkdir -p {config,target}'
#RUN sh -c 'mkdir config target'

#将配置文件添加到容器中
COPY /docker/application.yml /config/application.yml

#将本地文件添加到容器中
COPY /target/*.jar /target/server.jar

#配置容器，使其可执行化
# 自动识别config/application.yml

# 指定环境
#ENTRYPOINT java -jar -Dspring.profiles.active=prd /target/server.jar

# 不指定,用application.yml 控制
ENTRYPOINT java -jar  /target/server.jar

#配置时区，不然会发现打包到docker中启动的容器日志里的时间是差8个小时的
#这条命令会先通过 cp 命令将主机上的 /usr/share/zoneinfo/Asia/Shanghai 文件复制到容器中的 /etc/localtime 路径上，
#从而使容器中的系统时区与主机保持一致。接着，echo 命令将 Asia/Shanghai 设置为默认的容器时区，以便在后续操作过程中使用。
#使用上海时区, GTM+8,不同系统可能存在差异
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 暴露端口,需要和服务的端口一致
EXPOSE 45033

# 命令运行
# restart=always; privileged=true; network=mynetwork
# idea配置项
# --restart=always --privileged=true --network=mynetwork

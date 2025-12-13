FROM eclipse-temurin:17-jdk-jammy

#作者
MAINTAINER 阿星
# volume 指定临时文件目录为/tmp, 在主机/var/lib/docker 目录下创建一个临时文件夹并链接到容器的/tmp
VOLUME /logs
VOLUME /data

RUN bash -c 'mkdir -p {config,target}'
#RUN sh -c 'mkdir config target'

#将配置文件添加到容器中
COPY /docker/bootstrap.yml /config/bootstrap.yml

#将本地文件添加到容器中
COPY /target/*.jar /target/server.jar

#配置容器，使其可执行化
# 自动识别config/application.yml
ENTRYPOINT java -jar  /target/server.jar

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 暴露端口,需要和服务的端口一致
#EXPOSE 11000


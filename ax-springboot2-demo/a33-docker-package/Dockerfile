#FROM eclipse-temurin:21-jdk-jammy

## 极致精简 (云原生微服务)
FROM eclipse-temurin:21-jdk-alpine
#
## 复杂依赖/开发环境
#FROM eclipse-temurin:17-jdk
MAINTAINER 阿星

VOLUME /var/log/a33-docker-jar
VOLUME /data
ENV APP_NAME=server.jar

WORKDIR /app

#RUN mkdir -p config target
RUN mkdir -p config

#COPY /docker/application.yml ./config/application.yml
#COPY /docker/bootstrap.yml ./config/bootstrap.yml
#COPY /target/*.jar ./${APP_NAME}
COPY /target/*.jar ./

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 13301

#ENTRYPOINT exec java -Xms512m -Xmx512m -Xmn256m -jar ./${APP_NAME} --spring.profiles.active=prd

ENTRYPOINT exec java -Xms512m -Xmx512m -Xmn256m -jar ./*jar --spring.profiles.active=prd

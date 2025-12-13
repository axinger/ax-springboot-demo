FROM eclipse-temurin:21-jdk-jammy

MAINTAINER 阿星

VOLUME /var/log/a33-docker-jar
VOLUME /data
ENV APP_NAME=server.jar

WORKDIR /app

RUN #mkdir -p config target
RUN mkdir -p config

COPY /docker/application.yml ./config/application.yml
COPY /docker/bootstrap.yml ./config/bootstrap.yml
COPY /target/*.jar ./${APP_NAME}

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE 13301

ENTRYPOINT exec java -Xms512m -Xmx512m -Xmn256m -jar ./${APP_NAME} --spring.profiles.active=prd

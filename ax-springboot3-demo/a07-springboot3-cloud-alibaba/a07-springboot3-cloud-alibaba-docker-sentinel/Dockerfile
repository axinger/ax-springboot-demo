
FROM openjdk:8-jre-slim

#作者
MAINTAINER 阿星

#将本地文件添加到容器中
COPY /target/*.jar /home/sentinel-dashboard.jar

# 不指定,用application.yml 控制
# ENTRYPOINT java '-Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080' -jar  /target/server.jar

# RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 暴露端口,需要和服务的端口一致
#EXPOSE 8080
#EXPOSE 8719


ENV JAVA_OPTS '-Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080'

RUN chmod -R +x /home/sentinel-dashboard.jar

EXPOSE 8080

CMD java ${JAVA_OPTS} -jar /home/sentinel-dashboard.jar

# 命令运行
# restart=always; privileged=true; network=demo-network
# idea配置项
# --restart=always --privileged=true --network=mynetwork

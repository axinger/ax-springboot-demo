FROM eclipse-temurin:21-jdk-jammy

LABEL maintainer="阿星" \
      version="2.0.0" \
      description="Spring Boot应用生产镜像"

# 创建应用用户和目录
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 时区设置
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 环境变量
ENV APP_HOME=/app \
    APP_NAME=server.jar \
    JAVA_OPTS="-Xms512m -Xmx1024m -Xmn256m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError" \
    SPRING_PROFILES_ACTIVE=prd

# 工作目录
WORKDIR ${APP_HOME}

# 创建必要的目录
RUN mkdir -p ${APP_HOME}/config /var/log/a33-docker-jar /data && \
    chown -R appuser:appuser ${APP_HOME} /var/log/a33-docker-jar /data

# 复制外部配置文件（可选）
COPY docker/bootstrap.yml ./config/bootstrap.yml

# 复制JAR包
COPY target/*.jar ./${APP_NAME}

# 切换用户
USER appuser

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:13301/axinger/actuator/health || exit 1

# 暴露端口
EXPOSE 13301

# 启动命令
ENTRYPOINT exec java ${JAVA_OPTS} -jar ./${APP_NAME} --spring.profiles.active=${SPRING_PROFILES_ACTIVE}

FROM openjdk:17-jdk-slim

# 维护者信息
LABEL maintainer="axinger"
LABEL version="1.0.0"
LABEL description="Spring Modulith 企业级电商平台"

# 设置工作目录
WORKDIR /app

# 创建应用用户，避免使用root用户运行
RUN groupadd -r spring && useradd -r -g spring spring

# 复制Maven构建的JAR文件
COPY b43-modulith-app/target/b43-modulith-app-*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs && chown -R spring:spring /app

# 切换到spring用户
USER spring

# 暴露端口
EXPOSE 8080

# JVM参数
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar app.jar
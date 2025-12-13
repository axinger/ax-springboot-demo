#!/bin/sh

# 设置 JAVA_OPTS 变量，用于传递 JVM 参数
JAVA_OPTS="-Xmx512m -Dspring.profiles.active=prd"

# 打印日志，便于调试
echo "Starting application with options:"
echo "JAVA_OPTS: $JAVA_OPTS"

# 启动应用程序，并将日志输出到控制台打印
java $JAVA_OPTS -jar app.jar --logging.path=var/log

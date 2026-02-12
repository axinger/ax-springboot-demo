#!/bin/bash

# 变量定义,请勿修改
ENV=PRD


SERVER_NAME="demo1-service"
JAR_NAME="${SERVER_NAME}.jar"
# java存放位置
JAR_DIR_PATH="/opt/mydata/${SERVER_NAME}/jar"

SPRING_PROFILES_ACTIVE=prd
# nacos 配置服务地址
NACOS_CONFIG_SERVER_ADDR="http://localhost:8848"

JAVA_OPTS="-XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -Xms8g -Xmx8g"
JAVA_OPTS="${JAVA_OPTS} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/logs/${SERVER_NAME}/biz/${SERVER_NAME}_heapdump.hprof"
JAVA_OPTS="${JAVA_OPTS} -Xloggc:/opt/logs/${SERVER_NAME}/biz/${SERVER_NAME}_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
JAVA_ENV="-DSpring.profiles.active=${SPRING_PROFILES_ACTIVE} -DSpring.cloud.nacos:config.enabled = true -DSpring.cloud.nacos:config.server-addr=${NACOS_CONFIG_SERVER_ADDR}"


# 服务进程端id
PID=$(ps -ef | grep "${JAR_DIR_PATH}/${JAR_NAME}" | grep -v grep | grep -v kill | awk '{print $2}')

# 启动服务函数
start_server() {
  # 判断并切换目录
  if [ ! -d "${JAR_DIR_PATH}" ]; then
    echo  "服务目录不存在,请先安装服务!"
    exit 1
  fi

  # 启动服务
  cd "${JAR_DIR_PATH}" || exit
  source /etc/profile
  nohup java ${JAVA_ENV} ${JAVA_OPTS} -jar "${JAR_DIR_PATH}/${JAR_NAME}" >/dev/null 2>&1 &
}

# 启动服务脚本
if [ -n "${PID}" ]; then
  echo "服务已启动,无法再次启动!"
else
  # 启动服务
  start_server
  echo "服务启动成功!"
fi
#!/bin/bash

SERVER_NAME="demo1-service"
JAR_NAME="${SERVER_NAME}.jar"
# java存放位置
JAR_DIR_PATH="/opt/mydata/${SERVER_NAME}/jar"

# 变量定义,请勿修改
MAX_TIMEOUT=10
# 服务进程端id
PID=$(ps -ef|grep "${JAR_DIR_PATH}/${JAR_NAME}"|grep -v grep|grep -v kill|awk '{print $2}')

# 判断服务是否停止函数
check_server_status(){
        stopFlag=0
        for ((i=0;i<MAX_TIMEOUT;i++)); do
          sleep 5
          port_Id=$(ps -ef | grep "${PID}" | grep -v grep | awk '{print $2}')
          if [ -n "${port_Id}" ];then
            echo "${port_Id}:仍在运行!"
          else
            stopFlag=1;
            break;
          fi
        done

        # 服务关闭失败,则强制关闭
        if [ "${stopFlag}" -eq "0" ];then
                echo "系统将被强制关闭!"
                kill -9 "${PID}"
                sleep 10
        else
                echo "系统关闭成功!"
        fi
}

# 执行脚本-优雅关闭
if [ -n "${PID}" ];then
        echo "旧进程：${PID}"
        kill -15 "${PID}"
        check_server_status
else
        echo "系统关闭成功!"
fi
#!/bin/bash
# ============================================================================
# 应用停止脚本 (Emoji 中文版)
# ============================================================================

# 获取当前脚本所在的绝对路径
BASE_DIR=$(cd "$(dirname "$0")" && pwd)

# 默认参数
APP_NAME="demo-application"
PID_FILE="${BASE_DIR}/${APP_NAME}.pid"
TIMEOUT=30 # 优雅停机的最大等待时间（秒）

# 解析命令行参数
while getopts ":n:" opt; do
    case $opt in
        n) APP_NAME=$OPTARG;;
        ?) echo "⚠️ 未知参数"; exit 1;;
    esac
done

PID_FILE="${BASE_DIR}/${APP_NAME}.pid"

# 🔍 检查并获取 PID
if [ ! -f "$PID_FILE" ]; then
    echo "⚠️ 警告: 未找到 PID 文件 ($PID_FILE)。尝试通过进程名查找..."
    # 尝试通过进程名兜底查找
    PID=$(ps ax | grep -i "$APP_NAME" | grep java | grep -v grep | awk '{print $1}')
    if [ -z "$PID" ]; then
        echo "😴 未发现任何正在运行的相关进程。"
        exit 0
    else
        echo "🔍 通过进程名匹配到 PID: $PID"
    fi
else
    PID=$(cat "$PID_FILE")
    # 验证 PID 对应的进程是否存活
    if ! ps -p "$PID" > /dev/null 2>&1; then
        echo "🧹 进程 (PID: $PID) 已不存在，正在清理残留的 PID 文件。"
        rm -f "$PID_FILE"
        exit 0
    fi
fi

echo ""
echo "🛑 正在向应用 ${APP_NAME} (PID: $PID) 发送停止信号..."

# 第一步：发送 SIGTERM 信号，尝试优雅停机
kill "$PID"

# 第二步：轮询检查进程是否已退出
COUNT=0
while [ $COUNT -lt $TIMEOUT ]; do
    if ! ps -p "$PID" > /dev/null 2>&1; then
        echo "✅ 应用已成功优雅关闭。"
        rm -f "$PID_FILE"
        echo ""
        exit 0
    fi
    echo "⏳ 等待应用退出... (剩余 ${TIMEOUT=$((TIMEOUT - COUNT))} 秒)"
    sleep 1
    ((COUNT++))
done

# 第三步：如果超时仍未退出，发送 SIGKILL 强制终止
echo "⏰ 超过 ${TIMEOUT} 秒应用仍未响应，准备强制结束进程..."
kill -9 "$PID"
sleep 1

# 最终确认
if ps -p "$PID" > /dev/null 2>&1; then
    echo "❌ 强制结束失败，请手动检查系统进程！"
    exit 1
else
    echo "💥 应用已被强制结束。"
    rm -f "$PID_FILE"
    echo ""
    exit 0
fi

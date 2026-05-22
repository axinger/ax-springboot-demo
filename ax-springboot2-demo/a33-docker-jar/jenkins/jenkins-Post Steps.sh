#!/bin/bash
# ==========================================
# Jenkins 部署后步骤：自动化部署到远程服务器
# 功能: 传输 JAR 包、停止旧服务、启动新服务、验证状态
# 特性: 支持多服务器、重试机制、健康检查
# ==========================================

set -e  # 关键：一旦有任何命令失败，立即退出

source /etc/profile

# --- 配置区 ---
# 时区设置
export TZ='Asia/Shanghai'

# 部署目录 (目标服务器上的应用目录)
PKG_DIR="/opt/application/${JOB_BASE_NAME:-demo-app}"

# JAR 包名称 (从 Jenkins 参数获取，或自动查找)
if [ -z "$jar_file_name" ]; then
    # 自动查找最新的 JAR 包
    JAR_FILE=$(find . -type f -name "*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" | head -n 1)
    if [ -z "$JAR_FILE" ]; then
        echo "❌ 错误：未找到 JAR 包"
        exit 1
    fi
    jar_file_name=$(basename "$JAR_FILE")
    echo "🔍 自动检测到 JAR 包: $jar_file_name"
else
    JAR_FILE="./$jar_file_name"
fi

PKG="$JAR_FILE"

# SSH 端口 (从 Jenkins 参数获取，默认 22)
SSH_PORT=${ssh_port:-22}

# 部署用户 (从 Jenkins 参数获取，默认 deploy_user)
DEPLOY_USER=${deploy_user:-deploy_user}

# 重试配置
MAX_RETRIES=3
RETRY_DELAY=5

# --- 函数区 ---
log_with_time() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S.%3N %Z')] $1"
}

# 检查包是否存在
check_package() {
    if [ ! -f "$PKG" ]; then
        log_with_time "❌ 错误：找不到构建包 $jar_file_name"
        log_with_time "💡 提示：请确认 Maven 构建是否成功生成了 JAR 包"
        exit 1
    fi
    PKG_SIZE=$(du -h "$PKG" | cut -f1)
    log_with_time "📦 JAR 包: $jar_file_name (大小: $PKG_SIZE)"
}

# 解析 IP 列表
parse_ips() {
    if [ -z "$target_ip" ]; then
        log_with_time "❌ 错误：未配置目标服务器 IP (target_ip)"
        exit 1
    fi
    
    OLD_IFS="$IFS"
    IFS=','
    read -ra ips <<< "$target_ip"
    IFS="$OLD_IFS"
    
    log_with_time "🖥️ 目标服务器: ${ips[*]}"
}

# 重试函数
retry() {
    local cmd="$*"
    local count=0
    local max_retries=$MAX_RETRIES
    
    until eval "$cmd"; do
        count=$((count + 1))
        if [ $count -ge $max_retries ]; then
            log_with_time "❌ 命令执行失败，已达到最大重试次数 ($max_retries): $cmd"
            return 1
        fi
        log_with_time "⚠️ 命令执行失败，${RETRY_DELAY}秒后重试 ($count/$max_retries)..."
        sleep $RETRY_DELAY
    done
    return 0
}

# --- 主逻辑 ---
log_with_time "========================================="
log_with_time "🚀 开始部署应用到远程服务器"
log_with_time "========================================="

# 前置检查
check_package
parse_ips

# 统计变量
SUCCESS_COUNT=0
FAIL_COUNT=0
TOTAL_COUNT=${#ips[@]}

log_with_time "📊 部署计划: $TOTAL_COUNT 台服务器"
log_with_time ""

# 遍历服务器列表
for ip in "${ips[@]}"; do
    log_with_time "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    log_with_time "🖥️ 正在部署到服务器: $ip"
    log_with_time "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    
    DEPLOY_START_TIME=$(date +%s)

    # 1. 传输文件
    log_with_time "[1/4] 📤 正在传输文件..."
    TRANSFER_START=$(date +%s)
    if retry scp -P "$SSH_PORT" "${PKG}" "${DEPLOY_USER}@${ip}:${PKG_DIR}/"; then
        TRANSFER_END=$(date +%s)
        TRANSFER_DURATION=$((TRANSFER_END - TRANSFER_START))
        log_with_time "✅ 文件传输完成 (耗时: ${TRANSFER_DURATION}秒)"
    else
        log_with_time "❌ 文件传输失败: $ip"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    fi

    # 2. 停止旧服务
    log_with_time "[2/4] 🛑 正在停止旧服务..."
    if retry ssh -p "$SSH_PORT" "${DEPLOY_USER}@${ip}" "source /etc/profile; cd ${PKG_DIR} && ./stop.sh || true"; then
        log_with_time "✅ 旧服务已停止"
    else
        log_with_time "⚠️ 停止服务命令执行失败，继续尝试启动"
    fi
    
    # 等待服务完全停止
    log_with_time "⏳ 等待服务完全停止..."
    sleep 5

    # 3. 启动新服务
    log_with_time "[3/4] ▶️ 正在启动新服务..."
    if retry ssh -p "$SSH_PORT" "${DEPLOY_USER}@${ip}" "source /etc/profile; cd ${PKG_DIR} && ./start.sh"; then
        log_with_time "✅ 新服务启动命令已执行"
    else
        log_with_time "❌ 启动服务失败: $ip"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    fi
    
    # 等待服务启动
    log_with_time "⏳ 等待服务启动..."
    sleep 10

    # 4. 检查进程状态
    log_with_time "[4/4] 🔍 正在检查服务状态..."
    
    # 使用更可靠的进程检查方法
    process_check=$(ssh -p "$SSH_PORT" "${DEPLOY_USER}@${ip}" "ps aux | grep '[j]ava.*${jar_file_name%.jar}' | grep -v grep || true")
    
    if [ -n "$process_check" ]; then
        PID=$(echo "$process_check" | awk '{print $2}')
        log_with_time "✅ 服务在 $ip 上运行正常 (PID: $PID)"
        SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
    else
        # 如果进程检查失败，尝试查看日志
        log_with_time "⚠️ 未检测到 Java 进程，尝试查看启动日志..."
        ssh -p "$SSH_PORT" "${DEPLOY_USER}@${ip}" "tail -n 20 ${PKG_DIR}/*.log 2>/dev/null || echo '无日志文件'" || true
        log_with_time "❌ 服务在 $ip 上可能未启动成功"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    fi

    DEPLOY_END_TIME=$(date +%s)
    DEPLOY_DURATION=$((DEPLOY_END_TIME - DEPLOY_START_TIME))
    
    log_with_time "-----------------------------------------"
    log_with_time "✅ $ip 部署完成 (总耗时: ${DEPLOY_DURATION}秒)"
    log_with_time ""
done

# --- 部署总结 ---
log_with_time ""
log_with_time "========================================="
log_with_time "📊 部署结果统计"
log_with_time "========================================="
log_with_time "总服务器数: $TOTAL_COUNT"
log_with_time "成功: $SUCCESS_COUNT"
log_with_time "失败: $FAIL_COUNT"
log_with_time "当前时间: $(date '+%Y-%m-%d %H:%M:%S %Z')"
log_with_time "========================================="

if [ $FAIL_COUNT -gt 0 ]; then
    log_with_time "❌ 部分服务器部署失败，请检查日志"
    exit 1
else
    log_with_time "🎉 所有服务器部署完成！"
    exit 0
fi

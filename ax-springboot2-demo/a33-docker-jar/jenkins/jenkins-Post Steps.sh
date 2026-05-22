#!/bin/bash
set -e  # 关键：一旦有任何命令失败，立即退出

source /etc/profile

PKG_DIR="/opt/application/demo/$JOB_BASE_NAME"
PKG=$(find . -type f -name "$jar_file_name")

# 检查包是否存在
if [ ! -f "$PKG" ]; then
  echo " 错误：找不到构建包 $jar_file_name"
  exit 1
fi

# 设置 IFS 分割 IP
OLD_IFS="$IFS"
IFS=','
read -ra ips <<< "$target_ip"
IFS="$OLD_IFS"

# 重试函数
retry() {
  local cmd="$*"
  local max_retries=3
  local count=0
  until eval "$cmd"; do
    count=$((count + 1))
    if [ $count -ge $max_retries ]; then
      echo " 命令执行失败，已达到最大重试次数: $cmd"
      return 1
    fi
    echo " 命令执行失败，$count 秒后重试 ($count/$max_retries)..."
    sleep $count
  done
}

for ip in "${ips[@]}"; do
  echo "========================================="
  echo "🚀 正在部署到服务器: $ip"
  echo "========================================="

  # 1. 传输文件 (添加重试)
  echo "[1/4] 正在传输文件..."
  retry scp -P "$ssh_port" "${PKG}" "deploy_user@${ip}:${PKG_DIR}"

  # 2. 停止旧服务 (添加重试)
  echo "[2/4] 正在停止旧服务..."
  retry ssh -p "$ssh_port" "deploy_user@${ip}" "source /etc/profile; ${PKG_DIR}/stop.sh"
  sleep 3

  # 3. 启动新服务
  echo "[3/4] 正在启动新服务..."
  retry ssh -p "$ssh_port" "deploy_user@${ip}" "source /etc/profile; ${PKG_DIR}/start.sh"

  # 4. 检查进程状态 (优化匹配逻辑)
  echo "[4/4] 正在检查服务状态..."
  # 使用 [] 包裹 jar 名称的第一个字母，防止匹配到 grep 进程
  process_check=$(ssh -p "$ssh_port" "deploy_user@${ip}" "ps aux | grep \"[\$]{jar_file_name:0:1}\${jar_file_name:1}\" | grep -v grep")

  if [ -n "$process_check" ]; then
    echo "✅ 服务在 $ip 上运行正常"
  else
    echo "❌ 错误：服务在 $ip 上未启动成功"
    echo "调试信息: $process_check"
    exit 1
  fi

  echo "-----------------------------------------"
  echo " $ip 部署完成"
done

echo "========================================="
echo "🎉 所有服务器部署完成！"
echo "========================================="

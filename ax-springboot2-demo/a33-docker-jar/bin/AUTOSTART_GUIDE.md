# 🚀 开机自启动配置指南

> 📚 **返回主目录**: [README.md](README.md) | ⚡ **快速版**: [AUTOSTART_QUICK.md](AUTOSTART_QUICK.md)

## 📋 概述

本指南介绍如何将 Spring Boot 应用配置为 Linux 系统服务，实现开机自启动和系统化管理。

---

## 🔧 方法一：使用 systemd（推荐）✨

适用于：CentOS 7+, Ubuntu 16.04+, Debian 8+ 等现代 Linux 发行版

### 步骤 1：准备服务配置文件

已为你准备了 `demo-app.service` 模板文件，需要根据实际情况修改：

```bash
# 编辑服务配置文件
vim demo-app.service
```

**需要修改的关键配置：**

```ini
[Service]
# 工作目录 - 修改为你的实际路径
WorkingDirectory=/opt/application/demo-app/bin

# 启动命令 - 修改为你的实际路径
ExecStart=/opt/application/demo-app/bin/start.sh

# 停止命令 - 修改为你的实际路径
ExecStop=/opt/application/demo-app/bin/stop.sh

# 运行用户（建议创建专用用户）
User=root
Group=root
```

### 步骤 2：部署服务文件

```bash
# 1. 复制服务文件到 systemd 目录
sudo cp demo-app.service /etc/systemd/system/demo-app.service

# 2. 设置文件权限
sudo chmod 644 /etc/systemd/system/demo-app.service

# 3. 重新加载 systemd 配置
sudo systemctl daemon-reload
```

### 步骤 3：启用开机自启动

```bash
# 启用开机自启动
sudo systemctl enable demo-app.service

# 立即启动服务
sudo systemctl start demo-app.service

# 查看服务状态
sudo systemctl status demo-app.service
```

### 步骤 4：验证服务

```bash
# 检查服务是否正在运行
sudo systemctl is-active demo-app.service

# 检查是否已启用开机自启动
sudo systemctl is-enabled demo-app.service

# 查看服务日志
sudo journalctl -u demo-app.service -f
```

---

## 🛠️ systemd 常用管理命令

### 服务控制

```bash
# 启动服务
sudo systemctl start demo-app.service

# 停止服务
sudo systemctl stop demo-app.service

# 重启服务
sudo systemctl restart demo-app.service

# 重新加载配置（不中断服务）
sudo systemctl reload demo-app.service
```

### 状态查询

```bash
# 查看服务状态
sudo systemctl status demo-app.service

# 查看详细日志
sudo journalctl -u demo-app.service

# 查看最近 100 行日志
sudo journalctl -u demo-app.service -n 100

# 实时查看日志
sudo journalctl -u demo-app.service -f

# 查看今天的日志
sudo journalctl -u demo-app.service --since today
```

### 开机自启动管理

```bash
# 启用开机自启动
sudo systemctl enable demo-app.service

# 禁用开机自启动
sudo systemctl disable demo-app.service

# 检查是否启用
sudo systemctl is-enabled demo-app.service
```

### 服务配置管理

```bash
# 重新加载 systemd 配置
sudo systemctl daemon-reload

# 重置失败状态
sudo systemctl reset-failed demo-app.service
```

---

## 📝 方法二：使用 init.d 脚本（传统方式）

适用于：较老的 Linux 发行版或不支持 systemd 的系统

### 步骤 1：创建 init.d 脚本

```bash
#!/bin/bash
# /etc/init.d/demo-app

### BEGIN INIT INFO
# Provides:          demo-app
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Demo Application Service
# Description:       Spring Boot Application Service
### END INIT INFO

APP_NAME="demo-application"
APP_DIR="/opt/application/demo-app/bin"
START_SCRIPT="$APP_DIR/start.sh"
STOP_SCRIPT="$APP_DIR/stop.sh"

case "$1" in
    start)
        echo "Starting $APP_NAME..."
        cd $APP_DIR
        $START_SCRIPT
        ;;
    stop)
        echo "Stopping $APP_NAME..."
        cd $APP_DIR
        $STOP_SCRIPT
        ;;
    restart)
        $0 stop
        sleep 2
        $0 start
        ;;
    status)
        if [ -f "$APP_DIR/$APP_NAME.pid" ]; then
            PID=$(cat "$APP_DIR/$APP_NAME.pid")
            if ps -p $PID > /dev/null; then
                echo "$APP_NAME is running (PID: $PID)"
                exit 0
            else
                echo "$APP_NAME is not running (stale PID file)"
                exit 1
            fi
        else
            echo "$APP_NAME is not running"
            exit 3
        fi
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
        exit 1
        ;;
esac

exit 0
```

### 步骤 2：部署脚本

```bash
# 1. 复制脚本到 init.d 目录
sudo cp demo-app-initd /etc/init.d/demo-app

# 2. 设置执行权限
sudo chmod +x /etc/init.d/demo-app

# 3. 添加到开机自启动
sudo update-rc.d demo-app defaults

# 4. 启动服务
sudo service demo-app start

# 5. 查看状态
sudo service demo-app status
```

---

## 📊 方法三：使用 crontab（简单方式）

适用于：快速测试或临时需求

### 步骤 1：编辑 crontab

```bash
# 编辑当前用户的 crontab
crontab -e
```

### 步骤 2：添加开机启动任务

```bash
# 方法 1：使用 @reboot
@reboot /opt/application/demo-app/bin/start.sh

# 方法 2：使用特定的运行级别
@reboot sleep 30 && /opt/application/demo-app/bin/start.sh
```

### 步骤 3：验证

```bash
# 查看 crontab 配置
crontab -l

# 重启系统测试
sudo reboot
```

---

## ⚙️ 高级配置

### 1. 创建专用用户运行应用

```bash
# 创建应用用户
sudo useradd -r -s /bin/false demo-app

# 设置应用目录所有权
sudo chown -R demo-app:demo-app /opt/application/demo-app

# 修改服务配置文件
sudo vim /etc/systemd/system/demo-app.service
```

在 `[Service]` 部分修改：
```ini
User=demo-app
Group=demo-app
```

### 2. 配置资源限制

在服务配置文件中添加：

```ini
[Service]
# 最大打开文件数
LimitNOFILE=65536

# 最大进程数
LimitNPROC=4096

# CPU 权重 (100-262144)
CPUWeight=100

# 内存限制
MemoryMax=2G
```

### 3. 配置环境变量

```ini
[Service]
# 方式 1：直接在配置文件中设置
Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="TZ=Asia/Shanghai"

# 方式 2：从文件加载
EnvironmentFile=/opt/application/demo-app/.env
```

### 4. 配置日志轮转

创建 `/etc/logrotate.d/demo-app`：

```bash
/opt/logs/demo/*.log {
    daily
    rotate 30
    compress
    delaycompress
    missingok
    notifempty
    create 0644 root root
    postrotate
        systemctl reload demo-app.service > /dev/null 2>&1 || true
    endscript
}
```

---

## 🔍 故障排查

### 问题 1：服务启动失败

```bash
# 查看详细错误信息
sudo journalctl -u demo-app.service -xe

# 检查服务状态
sudo systemctl status demo-app.service

# 手动执行启动脚本测试
sudo /opt/application/demo-app/bin/start.sh
```

### 问题 2：开机未自动启动

```bash
# 检查是否已启用
sudo systemctl is-enabled demo-app.service

# 重新启用
sudo systemctl disable demo-app.service
sudo systemctl enable demo-app.service

# 检查依赖服务
sudo systemctl list-dependencies demo-app.service
```

### 问题 3：权限问题

```bash
# 检查文件权限
ls -la /opt/application/demo-app/bin/start.sh

# 设置正确的权限
sudo chmod +x /opt/application/demo-app/bin/start.sh
sudo chmod +x /opt/application/demo-app/bin/stop.sh

# 检查目录权限
sudo chown -R root:root /opt/application/demo-app
```

### 问题 4：JAVA_HOME 未找到

```bash
# 在服务配置中明确指定 JAVA_HOME
sudo vim /etc/systemd/system/demo-app.service
```

添加：
```ini
[Service]
Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
Environment="PATH=$JAVA_HOME/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
```

---

## 💡 最佳实践

1. **使用专用用户**：不要使用 root 运行应用
2. **配置资源限制**：防止应用占用过多系统资源
3. **配置日志轮转**：避免日志文件占满磁盘
4. **监控服务状态**：配置监控告警
5. **定期备份配置**：保存重要的配置文件
6. **测试重启策略**：确保应用能正确自动重启
7. **记录变更**：维护配置变更记录

---

## 📚 相关文档

- [systemd 官方文档](https://www.freedesktop.org/software/systemd/man/)
- [Linux 服务管理最佳实践](https://linuxhandbook.com/manage-services-systemd/)
- [Spring Boot 生产环境部署](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html)

---

## ✅ 快速开始示例

假设你的应用部署在 `/opt/myapp`，快速配置开机自启动：

```bash
# 1. 复制并修改服务配置
cp demo-app.service myapp.service
sed -i 's|/opt/application/demo-app|/opt/myapp|g' myapp.service
sed -i 's|Demo Application|My Application|g' myapp.service

# 2. 部署服务
sudo cp myapp.service /etc/systemd/system/
sudo systemctl daemon-reload

# 3. 启用并启动
sudo systemctl enable myapp.service
sudo systemctl start myapp.service

# 4. 验证
sudo systemctl status myapp.service
```

完成！🎉

# ⚡ 开机自启动 - 5分钟快速配置

> 📚 **返回主目录**: [README.md](README.md) | 📖 **详细版**: [AUTOSTART_GUIDE.md](AUTOSTART_GUIDE.md)

## 🎯 目标

将 Spring Boot 应用配置为系统服务，实现开机自启动。

---

## 📝 步骤（systemd 方式）

### 1️⃣ 修改服务配置文件

编辑 `demo-app.service`，修改以下路径为你的实际路径：

```ini
WorkingDirectory=/opt/application/demo-app/bin
ExecStart=/opt/application/demo-app/bin/start.sh
ExecStop=/opt/application/demo-app/bin/stop.sh
```

### 2️⃣ 部署服务

```bash
# 复制服务文件
sudo cp demo-app.service /etc/systemd/system/demo-app.service

# 重新加载 systemd
sudo systemctl daemon-reload
```

### 3️⃣ 启用并启动

```bash
# 启用开机自启动
sudo systemctl enable demo-app.service

# 立即启动
sudo systemctl start demo-app.service
```

### 4️⃣ 验证

```bash
# 查看状态
sudo systemctl status demo-app.service

# 查看日志
sudo journalctl -u demo-app.service -f
```

---

## 🔧 常用命令速查

```bash
# 启动/停止/重启
sudo systemctl start demo-app.service
sudo systemctl stop demo-app.service
sudo systemctl restart demo-app.service

# 查看状态
sudo systemctl status demo-app.service

# 查看日志
sudo journalctl -u demo-app.service -f

# 启用/禁用开机自启动
sudo systemctl enable demo-app.service
sudo systemctl disable demo-app.service

# 重新加载配置
sudo systemctl daemon-reload
```

---

## ✅ 完成检查清单

- [ ] 修改了 `demo-app.service` 中的路径
- [ ] 复制服务文件到 `/etc/systemd/system/`
- [ ] 执行了 `systemctl daemon-reload`
- [ ] 执行了 `systemctl enable demo-app.service`
- [ ] 执行了 `systemctl start demo-app.service`
- [ ] 使用 `systemctl status` 确认服务运行正常
- [ ] 重启系统测试开机自启动

---

## 🆘 遇到问题？

查看详细指南：[AUTOSTART_GUIDE.md](AUTOSTART_GUIDE.md)

常见错误排查：
```bash
# 查看详细错误
sudo journalctl -u demo-app.service -xe

# 检查脚本权限
ls -la /opt/application/demo-app/bin/*.sh

# 手动测试启动脚本
/opt/application/demo-app/bin/start.sh
```

# 📚 Spring Boot 应用运维完整指南

> **项目**: ax-springboot-demo  
> **模块**: a33-docker-jar/bin  
> **版本**: v1.0  
> **最后更新**: 2024-01-15

---

## 🎯 快速导航

本目录包含 Spring Boot 应用的完整运维解决方案，涵盖配置管理、服务部署、开机自启动等功能。

### 📖 文档索引

| 文档 | 说明 | 适用场景 |
|------|------|---------|
| [📘 USAGE.md](USAGE.md) | **应用运维脚本使用指南** | 日常启动/停止、配置管理、故障排查 |
| [🚀 AUTOSTART_GUIDE.md](AUTOSTART_GUIDE.md) | **开机自启动详细配置指南** | systemd/init.d/crontab 完整配置 |
| [⚡ AUTOSTART_QUICK.md](AUTOSTART_QUICK.md) | **开机自启动快速配置** | 5分钟快速设置 systemd 服务 |
| [🔧 JENKINS_CONFIG.md](../jenkins/JENKINS_CONFIG.md) | **Jenkins CI/CD 配置指南** | 自动化构建和部署 |

---

## 🗂️ 文件结构

```
bin/
├── 📄 核心脚本
│   ├── start.sh                    # 应用启动脚本（支持多JDK版本）
│   └── stop.sh                     # 应用停止脚本（优雅停机）
│
├── ⚙️ 配置文件
│   ├── .env                        # 当前环境配置（不会被Git跟踪）
│   ├── .env.example                # 配置模板
│   └── .env.prod.example           # 生产环境示例
│
├── 🔧 系统服务
│   └── demo-app.service            # systemd 服务配置模板
│
└── 📚 文档
    ├── README.md                   # 本文档：总索引
    ├── USAGE.md                    # 应用运维脚本使用指南
    ├── AUTOSTART_GUIDE.md          # 开机自启动详细指南
    └── AUTOSTART_QUICK.md          # 开机自启动快速指南
```

---

## 🚀 快速开始

### 新手入门路径

```
1️⃣ 首次使用？
   → 阅读 [USAGE.md - 快速开始章节](USAGE.md#-快速开始)

2️⃣ 需要配置开机自启动？
   → 先看 [AUTOSTART_QUICK.md](AUTOSTART_QUICK.md)（5分钟搞定）
   → 深入了解 [AUTOSTART_GUIDE.md](AUTOSTART_GUIDE.md)

3️⃣ 需要自动化部署？
   → 查看 [Jenkins 配置指南](../jenkins/JENKINS_CONFIG.md)

4️⃣ 遇到问题？
   → 查看各文档的"常见问题"章节
```

### 常用操作速查

#### 启动应用
```bash
cd bin/
./start.sh                              # 使用默认配置
./start.sh -j 17 -a nacos:8848         # 指定JDK和Nacos
```

#### 停止应用
```bash
./stop.sh                               # 停止默认应用
./stop.sh -n my-app                     # 停止指定应用
```

#### 配置开机自启动
```bash
# 快速方式（推荐）
sudo cp demo-app.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable demo-app.service
sudo systemctl start demo-app.service
```

#### 查看日志
```bash
tail -f /opt/logs/demo/demo-application.log      # 应用日志
sudo journalctl -u demo-app.service -f           # 系统服务日志
```

---

## 📋 核心功能特性

### ✅ 已实现功能

- **多 JDK 版本支持**: 自动检测或指定 JDK 8/11/17/21
- **灵活配置管理**: .env 文件 / 命令行参数 / 脚本默认值三层配置
- **Nacos 集成**: 配置中心和服务发现
- **优雅停机**: 支持超时强制终止
- **PID 管理**: 精准进程控制和重复启动检测
- **日志管理**: 应用日志 + GC 日志 + 日志轮转
- **开机自启动**: systemd / init.d / crontab 三种方式
- **CI/CD 集成**: Jenkins 自动化构建和部署

### 🎯 适用场景

| 场景 | 推荐方案 |
|------|---------|
| 本地开发测试 | 直接运行 `./start.sh` |
| 服务器部署 | 配置 `.env` + systemd 服务 |
| 多环境管理 | 不同 `.env` 文件切换 |
| 多项目共存 | 不同 APP_NAME + 不同端口 |
| 自动化部署 | Jenkins + 部署脚本 |
| 高可用部署 | 多服务器 + 负载均衡 |

---

## 🔍 按主题查找

### 配置管理
- [配置优先级说明](USAGE.md#配置优先级)
- [.env 文件配置](USAGE.md#方式一使用-env-配置文件推荐)
- [命令行参数](USAGE.md#方式三使用命令行参数临时覆盖)
- [可用配置项列表](USAGE.md#-可用的配置项)

### 启动/停止
- [启动示例](USAGE.md#startsh-使用示例)
- [停止示例](USAGE.md#stopsh-使用示例)
- [参数详解](USAGE.md#️-参数详解)

### 开机自启动
- [快速配置（5分钟）](AUTOSTART_QUICK.md)
- [systemd 详细配置](AUTOSTART_GUIDE.md#-方法一使用-systemd推荐)
- [init.d 配置](AUTOSTART_GUIDE.md#-方法二使用-initd-脚本传统方式)
- [crontab 配置](AUTOSTART_GUIDE.md#-方法三使用-crontab简单方式)

### Jenkins 集成
- [Jenkins Job 配置](../jenkins/JENKINS_CONFIG.md#-jenkins-job-配置)
- [参数配置](../jenkins/JENKINS_CONFIG.md#️-jenkins-参数配置)
- [服务器准备](../jenkins/JENKINS_CONFIG.md#️-服务器准备)
- [故障排查](../jenkins/JENKINS_CONFIG.md#-故障排查)

### 故障排查
- [常见问题](USAGE.md#-常见问题与提示)
- [systemd 问题](AUTOSTART_GUIDE.md#-故障排查)
- [Jenkins 问题](../jenkins/JENKINS_CONFIG.md#-故障排查)

---

## 💡 最佳实践

### 1. 配置管理
- ✅ 使用 `.env.example` 作为模板
- ✅ `.env` 文件加入 `.gitignore`
- ✅ 不同环境使用不同的 `.env` 文件
- ❌ 不要在 `.env` 中硬编码敏感信息

### 2. 服务部署
- ✅ 使用 systemd 管理服务
- ✅ 创建专用用户运行应用
- ✅ 配置资源限制和日志轮转
- ✅ 定期备份配置文件

### 3. 监控维护
- ✅ 配置进程监控和告警
- ✅ 定期清理旧日志
- ✅ 记录配置变更
- ✅ 测试重启策略

---

## 📊 版本历史

| 版本 | 日期 | 更新内容 |
|------|------|---------|
| v1.0 | 2024-01-15 | 初始版本，整合所有文档 |

---

## 🤝 贡献指南

如需改进文档或添加新功能：

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📞 支持与反馈

- 📧 问题反馈：提交 Issue
- 💬 讨论交流：参与 Discussions
- 📝 文档建议：提出改进意见

---

## 📜 许可证

本项目遵循 MIT 许可证 - 详见 [LICENSE](../../LICENSE) 文件

---

## 🔗 相关链接

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Nacos 官方文档](https://nacos.io/zh-cn/docs/what-is-nacos.html)
- [systemd 官方文档](https://www.freedesktop.org/software/systemd/man/)
- [Jenkins 官方文档](https://www.jenkins.io/doc/)

---

**最后更新**: 2024-01-15  
**维护者**: axinger  
**文档状态**: ✅ 完整

# 企业级部署指南

## 📋 概述

本项目提供了三种企业级部署方案，每种方案都有其特定的使用场景和优势。

## 🚀 部署方案

### 方案一：配置外部化（推荐）

**特点：**
- JAR包纯净，不包含任何环境特定配置
- 配置完全通过外部文件挂载
- 适合传统部署和容器化部署

**适用场景：**
- 生产环境部署
- 需要灵活配置管理的环境
- 安全要求高的场景

**使用方式：**
```bash
# 构建镜像
docker build -f Dockerfile-external-config -t myapp:external-config .

# 启动服务
docker-compose -f docker-compose.external-config.yml up -d

# 或使用部署脚本
./deploy.sh --scheme external-config --environment production
```

### 方案二：配置中心集成

**特点：**
- 集成Spring Cloud Config
- 配置集中管理
- 支持动态配置刷新

**适用场景：**
- 微服务架构
- 多环境配置管理
- 需要动态配置更新的场景

**使用方式：**
```bash
# 构建镜像
docker build -f Dockerfile-config-center -t myapp:config-center .

# 启动服务（需要配置中心）
docker-compose -f docker-compose.config-center.yml up -d
```

### 方案三：环境变量优先

**特点：**
- 符合12-Factor App原则
- 所有配置通过环境变量注入
- 最安全的配置管理方式

**适用场景：**
- 云原生环境
- Kubernetes部署
- 安全要求极高的场景

**使用方式：**
```bash
# 构建镜像
docker build -f Dockerfile-env-vars -t myapp:env-vars .

# 启动服务
docker-compose -f docker-compose.env-vars.yml up -d

# 或使用部署脚本
./deploy.sh --scheme env-vars --environment production
```

## 📁 文件结构说明

```
a33-docker-jar/
├── Dockerfile                    # 原始Dockerfile
├── Dockerfile.external-config   # 方案一：配置外部化
├── Dockerfile.config-center     # 方案二：配置中心
├── Dockerfile.env-vars          # 方案三：环境变量
├── docker-compose.*.yml         # 对应的docker-compose文件
├── docker/
│   ├── config/                  # 外部配置文件目录
│   │   └── application-prd.yml  # 生产环境配置
│   └── logs/                    # 日志目录
├── .env.production              # 生产环境敏感配置
├── deploy.sh                    # 部署脚本
└── src/main/resources/
    ├── application.yml          # 基础配置（通用）
    ├── application-dev.yml      # 开发环境配置
    └── application-prd.yml      # 生产环境配置（打包时排除）
```

## 🔧 配置管理

### 外部配置文件

**位置：** `docker/config/application-prd.yml`

**说明：** 此文件会被挂载到容器的 `/app/config/` 目录，优先级高于JAR包内的配置。

**敏感配置处理：**
- 敏感信息应通过环境变量注入
- 或使用 `.env.production` 文件管理
- 不要将敏感信息硬编码在配置文件中

### 环境变量配置

**优先级顺序：**
1. 命令行参数
2. 环境变量
3. 外部配置文件
4. JAR包内配置文件

**常用环境变量：**
```bash
# 应用配置
SPRING_PROFILES_ACTIVE=prd
SERVER_PORT=13301
SERVER_CONTEXT_PATH=/axinger

# 日志配置
LOG_LEVEL_ROOT=WARN
LOG_LEVEL_APP=INFO

# 数据库配置
DATABASE_URL=jdbc:mysql://host:3306/db
DATABASE_USERNAME=user
DATABASE_PASSWORD=pass
```

## 🛡️ 安全最佳实践

### 1. 镜像安全
- 使用非root用户运行应用
- 配置文件挂载为只读（`:ro`）
- 启用安全选项（`no-new-privileges`）

### 2. 配置安全
- 敏感信息使用环境变量
- 配置文件权限控制
- 定期轮换密钥和凭证

### 3. 网络安全
- 限制端口暴露
- 使用内部网络通信
- 启用HTTPS

## 📊 监控和健康检查

### 健康检查端点
- `GET /axinger/actuator/health` - 健康状态
- `GET /axinger/actuator/metrics` - 应用指标
- `GET /axinger/actuator/info` - 应用信息

### 日志管理
- 日志输出到挂载的卷：`/var/log/a33-docker-jar/`
- 日志级别可通过环境变量配置
- 支持日志轮转和归档

## 🚀 快速开始

### 开发环境
```bash
# 使用开发profile打包
mvn clean package -Pdev

# 运行应用
mvn spring-boot:run -Pdev
```

### 生产环境部署
```bash
# 1. 选择部署方案（推荐external-config）
# 2. 配置外部配置文件
# 3. 配置环境变量
# 4. 执行部署
./deploy.sh --scheme external-config --environment production

# 5. 验证部署
curl http://localhost:13301/axinger/actuator/health
```

## 🔍 故障排除

### 常见问题

1. **配置文件不生效**
   - 检查文件挂载路径是否正确
   - 确认环境变量是否设置
   - 查看应用启动日志

2. **健康检查失败**
   - 检查端口是否被占用
   - 确认应用是否正常启动
   - 查看详细日志信息

3. **权限问题**
   - 确认挂载目录的权限
   - 检查用户组设置
   - 验证SELinux配置（如启用）

### 日志查看
```bash
# 查看容器日志
docker-compose -f docker-compose.external-config.yml logs -f

# 查看应用日志
tail -f docker/logs/application.log
```

## 📞 支持

如需技术支持，请参考：
- Spring Boot官方文档
- Docker官方文档
- 项目README文件
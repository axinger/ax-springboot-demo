# Spring Modulith 企业级演示 - 快速开始指南

## 🎯 3分钟快速体验

### 步骤1：构建项目
```bash
cd b43-modulith
mvn clean install -DskipTests
```

### 步骤2：启动应用
```bash
cd b43-modulith-app
mvn spring-boot:run
```

### 步骤3：体验功能
```bash
# 查看系统信息
curl http://localhost:8080/api/demo/info

# 创建演示订单
curl -X POST http://localhost:8080/api/demo/create-order

# 访问H2控制台（浏览器打开）
http://localhost:8080/h2-console
```

## 🚀 一键部署脚本

```bash
#!/bin/bash
# quick-start.sh
echo "🚀 正在启动 Spring Modulith 企业级演示..."

# 构建项目
echo "📦 构建项目中..."
mvn clean install -DskipTests

# 启动应用
echo "🎯 启动应用中..."
cd b43-modulith-app
mvn spring-boot:run &

# 等待启动
sleep 10

# 测试API
echo "🧪 测试API..."
curl http://localhost:8080/api/demo/info

echo "✅ 启动完成！访问 http://localhost:8080"
```

## 📋 核心命令速查

### 构建命令
```bash
# 完整构建
mvn clean install

# 跳过测试快速构建
mvn clean install -DskipTests

# 只编译
mvn clean compile

# 清理构建
mvn clean
```

### 运行命令
```bash
# 启动应用
cd b43-modulith-app
mvn spring-boot:run

# 打包应用
mvn package

# 运行测试
mvn test
```

### API 测试命令
```bash
# 系统信息
curl http://localhost:8080/api/demo/info

# 创建订单
curl -X POST http://localhost:8080/api/demo/create-order

# 订单流转
curl -X POST http://localhost:8080/api/demo/order-lifecycle/{orderId}
```

## 🔧 配置速查

### 应用配置
```yaml
# application.yml 关键配置
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: password

# Actuator 端点
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,modulith
```

### 数据库连接
- **URL**: jdbc:h2:mem:testdb
- **控制台**: http://localhost:8080/h2-console
- **用户名**: sa
- **密码**: password

## 🎯 功能速览

### 订单管理
- ✅ 创建订单
- ✅ 确认订单
- ✅ 取消订单
- ✅ 查询订单

### 客户管理
- ✅ 客户信息管理
- ✅ 客户状态管理
- ✅ 地址管理

### 库存管理
- ✅ 库存查询
- ✅ 库存预留
- ✅ 库存释放
- ✅ 库存调整

### 支付管理
- ✅ 支付处理
- ✅ 退款处理
- ✅ 支付状态管理

### 通知管理
- ✅ 邮件通知
- ✅ 短信通知
- ✅ 通知状态跟踪

## 📁 项目结构速查

```
b43-modulith/
├── order-module/          # 订单模块 (核心)
├── customer-module/       # 客户模块
├── inventory-module/      # 库存模块
├── payment-module/        # 支付模块
├── notification-module/   # 通知模块
├── shared-kernel/         # 共享内核
└── b43-modulith-app/     # 主应用
```

## 🐛 常见问题速解

### 问题1：构建失败
```
❌ 错误：找不到依赖
✅ 解决：先构建父项目 mvn clean install
```

### 问题2：端口冲突
```
❌ 错误：端口8080被占用
✅ 解决：修改 server.port 或 kill 占用进程
```

### 问题3：数据库连接失败
```
❌ 错误：数据库连接失败
✅ 解决：检查 H2 配置，确保使用内存数据库
```

### 问题4：模块未找到
```
❌ 错误：Spring Modulith 模块未发现
✅ 解决：检查 @ApplicationModule 注解配置
```

## 📚 文档导航

| 文档 | 内容 | 适用场景 |
|------|------|----------|
| `README.md` | 项目总览 | 首次了解项目 |
| `QUICK_START.md` | 快速开始 | 快速体验 |
| `PROJECT_STRUCTURE.md` | 结构说明 | 了解架构设计 |
| `MODULES.md` | 模块详解 | 深入了解模块 |
| `SPRING_MODULITH_GUIDE.md` | 完整指南 | 企业级应用参考 |

## 🎓 学习路径

### 初学者 (30分钟)
1. 阅读 `README.md` 了解项目
2. 运行快速开始指南
3. 体验API演示
4. 查看H2数据库

### 进阶者 (2小时)
1. 阅读 `PROJECT_STRUCTURE.md`
2. 研究模块设计
3. 分析领域模型
4. 理解模块间通信

### 专家 (1天)
1. 阅读 `SPRING_MODULITH_GUIDE.md`
2. 研究架构测试
3. 分析事件驱动设计
4. 实践模块扩展

## 🚀 生产部署要点

### 环境准备
- Java 17+
- Maven 3.6+
- 生产数据库 (MySQL/PostgreSQL)

### 配置变更
```yaml
# 生产环境配置要点
spring:
  datasource:
    url: jdbc:mysql://prod-db:3306/modulith
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate  # 生产环境不要自动建表

# 安全配置
management:
  endpoints:
    web:
      exposure:
        include: health,info  # 生产环境限制端点
```

### 监控配置
- 启用 Prometheus 监控
- 配置日志聚合
- 设置告警规则
- 配置健康检查

## 📞 支持资源

- **官方文档**: https://docs.spring.io/spring-modulith
- **Spring Boot**: https://spring.io/projects/spring-boot
- **示例代码**: 见各模块源码
- **问题反馈**: 查看相关文档

---

**🎉 恭喜！您现在已掌握 Spring Modulith 企业级演示项目的核心内容！**

**下一步建议**：
1. 探索各个模块的源代码
2. 尝试添加新的业务功能
3. 实践模块间的事件通信
4. 研究架构测试的实现
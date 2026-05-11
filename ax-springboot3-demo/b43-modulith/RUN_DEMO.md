# Spring Modulith 企业级演示 - 运行指南

## 🎯 演示目标

展示如何使用 Spring Modulith 构建企业级模块化架构，包括：
- 模块化设计和依赖管理
- 领域驱动设计实践
- 类型安全的值对象
- 模块间协作机制

## 🚀 快速演示（5分钟）

### 步骤1：启动应用

打开终端，执行以下命令：

```bash
# 进入项目根目录
cd b43-modulith

# 进入应用目录
cd b43-modulith-app

# 启动 Spring Boot 应用
mvn spring-boot:run -DskipTests
```

### 步骤2：体验演示API

应用启动后（约10秒），在另一个终端中执行：

```bash
# 1. 查看系统信息
curl http://localhost:8080/api/demo/info

# 2. 演示值对象
curl http://localhost:8080/api/demo/demo-values

# 3. 查看模块信息
curl http://localhost:8080/api/demo/modules

# 4. 健康检查
curl http://localhost:8080/api/demo/health
```

### 步骤3：查看数据库

在浏览器中访问 H2 控制台：
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- 用户名: sa
- 密码: password

## 📊 演示脚本

```bash
#!/bin/bash
# demo-script.sh

echo "🎯 Spring Modulith 企业级演示开始"
echo "================================"

echo "\n📦 启动应用..."
cd b43-modulith-app
mvn spring-boot:run -DskipTests &
SPRING_PID=$!

echo "⏳ 等待应用启动（10秒）..."
sleep 10

echo "\n🔍 测试API端点:"
echo "------------------------"

echo "\n1. 系统信息:"
curl -s http://localhost:8080/api/demo/info

echo "\n\n2. 值对象演示:"
curl -s http://localhost:8080/api/demo/demo-values

echo "\n\n3. 模块架构:"
curl -s http://localhost:8080/api/demo/modules

echo "\n\n4. 健康状态:"
curl -s http://localhost:8080/api/demo/health

echo "\n\n🎉 演示完成！"
echo "📚 详细文档请查看项目中的各种 .md 文件"
echo "🔍 数据库控制台: http://localhost:8080/h2-console"

echo "\n⏹️  停止应用..."
kill $SPRING_PID 2>/dev/null
```

## 🎭 演示亮点

### 1. 模块化架构
```
✅ 7个独立模块（6业务+1应用）
✅ 清晰的依赖关系
✅ 模块间松耦合
✅ 可独立开发和测试
```

### 2. 领域驱动设计
```
✅ 聚合根设计（Order, Customer, Product等）
✅ 值对象封装（OrderId, CustomerId, Money）
✅ 领域事件通信
✅ 仓储模式实现
```

### 3. 企业级特性
```
✅ 类型安全（Java Record）
✅ 验证逻辑内置
✅ 事件驱动架构
✅ 架构约束验证
```

## 📚 演示内容详解

### API 端点说明

#### 1. `/api/demo/info`
展示系统整体架构信息：
- 核心业务模块列表
- 架构特性说明
- 技术栈介绍

#### 2. `/api/demo/demo-values`
演示值对象的使用：
- 自动生成唯一ID
- 类型安全的值对象
- 内置验证逻辑

#### 3. `/api/demo/modules`
详细展示每个模块：
- 模块职责
- 核心组件
- 依赖关系

#### 4. `/api/demo/health`
应用健康状态检查：
- 应用运行状态
- 当前时间
- 模块加载状态

## 🏗️ 架构演示要点

### 模块依赖关系
```
shared-kernel
    │
    ├─── order-module ──┬── customer-module
    │                   ├── inventory-module  
    │                   └── payment-module
    │
    └─── notification-module
    │
    └─── b43-modulith-app (主应用)
```

### 关键技术点

1. **@ApplicationModule 注解**
   - 定义模块边界
   - 声明依赖关系
   - 启用模块发现

2. **值对象设计**
   - 不可变性
   - 类型安全
   - 内置验证

3. **领域事件**
   - 松耦合通信
   - 异步处理
   - 可扩展性

## 🎯 演示效果

### 观众可以看到

1. **完整的企业级架构** - 7个模块协同工作
2. **清晰的代码组织** - 每个模块职责明确
3. **类型安全的编程** - 使用Java Record和值对象
4. **模块化的优势** - 可独立开发、测试、部署
5. **文档完整性** - 从快速开始到架构指南

### 技术亮点展示

- ✅ Spring Modulith 的实际应用
- ✅ 领域驱动设计的完整实现
- ✅ 企业级最佳实践
- ✅ 可运行的演示代码

## 🚀 后续扩展建议

演示结束后，可以讨论：

1. **如何添加新模块**
   - 创建模块目录
   - 编写POM配置
   - 定义领域模型

2. **如何扩展功能**
   - 添加新的聚合根
   - 实现领域服务
   - 添加REST API

3. **如何部署到生产**
   - 配置生产数据库
   - 配置监控告警
   - 容器化部署

## 📞 演示总结

**Spring Modulith 企业级模块化架构演示** 展示了：

- 🎯 **完整的模块化设计**
- 🏗️ **清晰的架构层次**
- 🔧 **实用的业务功能**
- 📚 **完善的文档体系**
- 🚀 **可运行的演示代码**

这个演示不仅展示了技术实现，更重要的是展示了如何在实际企业项目中应用模块化架构的最佳实践。

---

**🎊 演示准备完成！只需运行 `mvn spring-boot:run` 即可开始演示！**
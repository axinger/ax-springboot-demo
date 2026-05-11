# Spring Modulith 企业级模块化架构演示

## 项目概述

这是一个使用 Spring Modulith 构建的企业级模块化架构演示项目，展示了如何在实际企业应用中实现模块化设计和领域驱动开发（DDD）。

## 技术栈

- **Spring Boot 3.5+**
- **Spring Modulith 1.2.0** - 模块化架构支持
- **JMolecules** - DDD 架构注解
- **ArchUnit** - 架构测试
- **Java 21**
- **Maven** - 构建工具

## 模块架构

### 核心业务模块

#### 1. 订单模块 (order-module)
- **职责**: 订单创建、确认、取消和状态管理
- **依赖**: 客户模块、库存模块、支付模块
- **核心功能**:
  - 创建订单
  - 确认订单
  - 取消订单
  - 订单状态管理

#### 2. 客户模块 (customer-module)
- **职责**: 客户信息管理和客户生命周期
- **核心功能**:
  - 客户注册
  - 客户信息管理
  - 客户状态管理

#### 3. 库存模块 (inventory-module)
- **职责**: 产品库存管理和库存操作
- **核心功能**:
  - 库存查询
  - 库存预留
  - 库存释放
  - 库存调整

#### 4. 支付模块 (payment-module)
- **职责**: 支付处理和交易管理
- **依赖**: 订单模块
- **核心功能**:
  - 支付处理
  - 支付状态管理
  - 退款处理

#### 5. 通知模块 (notification-module)
- **职责**: 系统通知和消息发送
- **核心功能**:
  - 邮件通知
  - 短信通知
  - 应用内通知

### 共享内核 (shared-kernel)

包含跨模块共享的通用领域概念：
- `OrderId` - 订单ID值对象
- `CustomerId` - 客户ID值对象
- `Money` - 金额值对象

## 架构特性

### 1. 模块化设计
- 每个业务模块独立封装
- 明确的模块依赖关系
- 模块间通过定义良好的接口通信

### 2. 领域驱动设计
- 使用聚合根、实体、值对象
- 领域事件驱动架构
- 分层架构（领域层、应用层、基础设施层）

### 3. 架构约束
- 使用 ArchUnit 进行架构测试
- 模块依赖关系验证
- 分层架构约束

## 快速开始

### 构建项目

```bash
# 克隆项目
cd b43-modulith

# 构建所有模块
mvn clean install

# 跳过测试快速构建
mvn clean install -DskipTests
```

### 运行应用

```bash
# 运行 Spring Boot 应用
mvn spring-boot:run

# 访问 H2 控制台
http://localhost:8080/h2-console

# 访问 Actuator 端点
http://localhost:8080/actuator/modulith
```

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行架构测试
mvn test -Dtest=ModulithArchitectureTest

# 验证模块依赖
mvn verify
```

## API 示例

### 创建订单

```bash
POST /api/orders
Content-Type: application/json

{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "items": [
    {
      "productId": "PROD-001",
      "productName": "iPhone 15",
      "quantity": 1,
      "price": 6999.00
    }
  ]
}
```

### 确认订单

```bash
POST /api/orders/{orderId}/confirm
```

### 取消订单

```bash
POST /api/orders/{orderId}/cancel
```

## 架构文档

### 模块依赖图

生成模块文档：

```bash
mvn spring-modulith:documents
```

文档将生成在 `target/modulith-docs/` 目录中。

### 架构验证

项目包含完整的架构测试，验证：
- 模块依赖关系
- 分层架构约束
- 包结构约定
- 循环依赖检测

## 最佳实践

### 1. 模块设计原则
- **单一职责**: 每个模块只负责一个业务领域
- **高内聚**: 相关功能集中在同一模块
- **低耦合**: 模块间依赖最小化
- **明确边界**: 清晰的模块接口定义

### 2. 依赖管理
- **单向依赖**: 避免循环依赖
- **显式声明**: 明确模块依赖关系
- **共享内核**: 通用概念集中管理

### 3. 测试策略
- **单元测试**: 每个模块独立测试
- **集成测试**: 模块间集成测试
- **架构测试**: 架构约束验证

## 部署说明

### Docker 部署

```bash
# 构建 Docker 镜像
docker build -t b43-modulith .

# 运行容器
docker run -p 8080:8080 b43-modulith
```

### 生产环境配置

- 配置生产数据库连接
- 配置邮件服务
- 配置监控和日志
- 配置安全认证

## 性能优化

- 使用缓存减少数据库访问
- 异步处理非关键业务
- 批量处理提高吞吐量
- 监控关键性能指标

## 故障排除

### 常见问题

1. **模块依赖错误**: 检查 `ApplicationModule` 注解的 `allowedDependencies`
2. **循环依赖**: 使用 `mvn verify` 检测循环依赖
3. **架构违规**: 运行架构测试定位问题

### 调试技巧

- 启用 Spring Modulith 调试日志
- 使用 Actuator 端点查看模块状态
- 检查 H2 控制台查看数据

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 添加测试
4. 提交更改
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。
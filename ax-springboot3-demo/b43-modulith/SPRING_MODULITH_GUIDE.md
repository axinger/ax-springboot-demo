# Spring Modulith 企业级模块化架构完整指南

## 🎯 项目目标

本演示项目展示了如何在企业级应用中使用 Spring Modulith 构建模块化架构，实现清晰的模块边界、领域驱动设计和事件驱动架构。

## 🏗️ 架构概述

### 核心架构原则

1. **模块化设计** - 每个业务领域独立封装
2. **单一职责** - 每个模块只负责一个业务领域
3. **明确边界** - 清晰的模块接口和依赖关系
4. **事件驱动** - 模块间通过事件通信
5. **领域驱动** - 使用 DDD 原则设计领域模型

### 模块依赖关系

```
┌─────────────────┐
│  shared-kernel  │
└─────────────────┘
         │
         ▼
┌─────────────────┐    ┌─────────────────┐
│  order-module   │───▶│ customer-module │
└─────────────────┘    └─────────────────┘
         │
         ▼
┌─────────────────┐    ┌─────────────────┐
│ payment-module  │    │inventory-module │
└─────────────────┘    └─────────────────┘
         │
         ▼
┌─────────────────┐
│notification-    │
│module           │
└─────────────────┘
```

## 📦 模块详解

### 1. 共享内核 (shared-kernel)

**职责**: 提供跨模块共享的通用领域概念

**核心组件**:
- `OrderId` - 订单唯一标识符
- `CustomerId` - 客户唯一标识符
- `Money` - 货币计算值对象

**设计要点**:
- 不可变值对象
- 类型安全的ID设计
- 内置验证逻辑

### 2. 订单模块 (order-module)

**职责**: 订单生命周期管理

**领域模型**:
- `Order` - 订单聚合根
- `OrderItem` - 订单项实体
- `OrderStatus` - 订单状态枚举

**业务能力**:
- 创建订单
- 确认订单
- 取消订单
- 订单查询
- 状态流转管理

**API 接口**:
```
POST   /api/orders              - 创建订单
POST   /api/orders/{id}/confirm - 确认订单
POST   /api/orders/{id}/cancel  - 取消订单
GET    /api/orders/{id}         - 查询订单
```

### 3. 客户模块 (customer-module)

**职责**: 客户信息管理和生命周期

**领域模型**:
- `Customer` - 客户聚合根
- `CustomerName` - 客户名称值对象
- `Email` - 邮箱值对象
- `PhoneNumber` - 电话值对象
- `Address` - 地址值对象

**业务规则**:
- 邮箱格式验证
- 手机号格式验证
- 地址完整性检查
- 客户状态管理

### 4. 库存模块 (inventory-module)

**职责**: 产品库存管理和操作

**领域模型**:
- `Product` - 产品聚合根
- `Inventory` - 库存聚合根

**核心功能**:
- 库存查询
- 库存预留
- 库存释放
- 库存消耗
- 库存调整
- 安全库存检查

**业务规则**:
- 库存数量验证
- 预留库存不能超过可用库存
- 库存不足预警

### 5. 支付模块 (payment-module)

**职责**: 支付处理和交易管理

**领域模型**:
- `Payment` - 支付聚合根
- `PaymentMethod` - 支付方式值对象
- `Refund` - 退款聚合根

**支付流程**:
1. 创建支付订单
2. 选择支付方式
3. 处理支付
4. 确认支付结果
5. 处理退款（可选）

### 6. 通知模块 (notification-module)

**职责**: 系统通知和消息发送

**通知类型**:
- 邮件通知
- 短信通知
- 应用内通知
- 推送通知

**核心特性**:
- 多通道支持
- 优先级管理
- 发送状态跟踪
- 失败重试机制

## 🔧 技术实现

### 依赖管理

```xml
<!-- Spring Modulith 核心 -->
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter-core</artifactId>
</dependency>

<!-- 模块测试支持 -->
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 模块定义

```java
@ApplicationModule(
    allowedDependencies = {"customer-module", "inventory-module"},
    displayName = "订单模块"
)
public class OrderModule {
    // 领域模型定义
}
```

### 领域事件

```java
@Service
@Transactional
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;

    public Order createOrder(CustomerId customerId, Set<OrderItem> items) {
        // 创建订单逻辑
        eventPublisher.publishEvent(new OrderCreatedEvent(orderId, customerId, amount));
        return order;
    }

    public record OrderCreatedEvent(OrderId orderId, CustomerId customerId, double amount) {}
}
```

## 🚀 快速开始

### 1. 构建项目

```bash
# 克隆项目
cd b43-modulith

# 编译所有模块
mvn clean compile

# 构建并安装到本地仓库
mvn clean install -DskipTests

# 运行测试
mvn test
```

### 2. 启动应用

```bash
# 启动 Spring Boot 应用
mvn spring-boot:run

# 访问应用
curl http://localhost:8080/api/demo/info

# 创建演示订单
curl -X POST http://localhost:8080/api/demo/create-order
```

### 3. 验证架构

```bash
# 使用 Spring Modulith 验证模块依赖
mvn spring-modulith:verify

# 生成模块文档
mvn spring-modulith:documents
```

## 🧪 测试策略

### 1. 单元测试
- 每个模块独立的业务逻辑测试
- 值对象验证测试
- 聚合根行为测试

### 2. 集成测试
- 模块间集成测试
- 领域事件测试
- 持久化测试

### 3. 架构测试
- 模块依赖关系验证
- 包结构约束验证
- 分层架构验证

### 示例架构测试

```java
@AnalyzeClasses(packagesOf = ModulithApplication.class)
public class ModulithArchitectureTest {
    private final ApplicationModules modules = ApplicationModules.of(ModulithApplication.class);

    @ArchTest
    void shouldRespectModuleDependencies() {
        modules.verify();
    }
}
```

## 📊 监控和运维

### 健康检查

```yaml
# Actuator 配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,modulith
  endpoint:
    modulith:
      enabled: true
```

### 监控端点

- `/actuator/health` - 健康检查
- `/actuator/modulith` - 模块状态
- `/actuator/metrics` - 应用指标

## 🎯 最佳实践

### 1. 模块设计

✅ **推荐做法**:
- 按业务领域划分模块
- 保持模块大小适中
- 定义清晰的模块接口
- 使用领域事件进行通信

❌ **避免做法**:
- 过细的模块粒度
- 循环依赖
- 模块间直接数据库访问
- 共享数据库表

### 2. 依赖管理

✅ **推荐做法**:
- 使用 `allowedDependencies` 明确依赖关系
- 通过事件进行松耦合通信
- 使用共享内核模式

❌ **避免做法**:
- 隐式依赖
- 数据库级别的耦合
- 全局状态共享

### 3. 测试策略

✅ **推荐做法**:
- 为每个模块编写独立的测试套件
- 使用架构测试验证约束
- 测试领域事件
- 验证模块边界

❌ **避免做法**:
- 跨模块的紧耦合测试
- 忽略架构约束测试
- 只测试实现不测试接口

## 🚀 扩展性考虑

### 1. 水平扩展
- 每个模块可独立部署
- 支持微服务架构演进
- 容器化部署友好

### 2. 功能扩展
- 插件化架构支持
- 配置驱动行为
- AOP 切面编程

### 3. 技术演进
- 可逐步迁移到 Spring Cloud
- 支持多数据库
- 消息队列集成

## 📚 学习资源

### 官方文档
- [Spring Modulith 官方文档](https://docs.spring.io/spring-modulith/docs/current/reference/html/)
- [Spring Boot 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)

### 相关技术
- 领域驱动设计 (DDD)
- 事件驱动架构 (EDA)
- 微服务架构
- 模块化设计模式

## 🏁 总结

本演示项目展示了 Spring Modulith 在企业级应用中的完整应用，包括：

1. **模块化架构设计** - 清晰的模块边界和依赖关系
2. **领域驱动设计** - 使用聚合根、实体、值对象
3. **事件驱动架构** - 松耦合的模块间通信
4. **架构约束验证** - 使用 ArchUnit 确保架构质量
5. **完整的企业级功能** - 涵盖订单、客户、库存、支付、通知等核心业务

通过这个项目，您可以学习到如何在实际企业应用中使用 Spring Modulith 构建可维护、可扩展的模块化架构。
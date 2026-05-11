# Spring Modulith 企业级项目结构说明

## 📁 项目整体结构

```
b43-modulith/
├── 📁 order-module/                 # 订单业务模块
├── 📁 customer-module/              # 客户业务模块
├── 📁 inventory-module/             # 库存业务模块
├── 📁 payment-module/               # 支付业务模块
├── 📁 notification-module/          # 通知业务模块
├── 📁 shared-kernel/                # 共享内核模块
├── 📁 b43-modulith-app/            # 主应用程序
├── 📄 pom.xml                       # 父POM（多模块管理）
├── 📄 README.md                     # 项目总览文档
├── 📄 MODULES.md                    # 模块详细说明
├── 📄 SPRING_MODULITH_GUIDE.md      # 架构完整指南
└── 📄 PROJECT_STRUCTURE.md          # 本文件：项目结构说明
```

## 🏗️ 架构层次详解

### 1. 业务模块层

#### 📦 order-module（订单模块）
```
order-module/
├── 📄 pom.xml                                   # 模块依赖配置
└── 📁 src/main/java/com/axinger/order/
    ├── 📄 OrderModule.java                      # 模块定义和领域模型
    ├── 📁 domain/
    │   └── 📄 OrderRepository.java             # 订单仓储接口
    ├── 📁 application/
    │   └── 📄 OrderService.java                # 订单应用服务
    └── 📁 web/
        └── 📄 OrderController.java            # REST API 控制器
```

**职责**: 订单生命周期管理
**依赖**: customer-module, inventory-module, payment-module

#### 📦 customer-module（客户模块）
```
customer-module/
├── 📄 pom.xml
└── 📁 src/main/java/com/axinger/customer/
    ├── 📄 CustomerModule.java                   # 客户领域模型
    └── 📁 domain/
        └── 📄 CustomerRepository.java         # 客户仓储接口
```

**职责**: 客户信息管理
**依赖**: 无（基础模块）

#### 📦 inventory-module（库存模块）
```
inventory-module/
├── 📄 pom.xml
└── 📁 src/main/java/com/axinger/inventory/
    └── 📄 InventoryModule.java                 # 库存领域模型
```

**职责**: 库存管理
**依赖**: 无（基础模块）

#### 📦 payment-module（支付模块）
```
payment-module/
├── 📄 pom.xml
└── 📁 src/main/java/com/axinger/payment/
    └── 📄 PaymentModule.java                   # 支付领域模型
```

**职责**: 支付处理
**依赖**: order-module

#### 📦 notification-module（通知模块）
```
notification-module/
├── 📄 pom.xml
└── 📁 src/main/java/com/axinger/notification/
    └── 📄 NotificationModule.java             # 通知领域模型
```

**职责**: 消息通知
**依赖**: 无（基础模块）

### 2. 共享内核层

#### 📦 shared-kernel（共享内核）
```
shared-kernel/
├── 📄 pom.xml
└── 📁 src/main/java/com/axinger/shared/
    ├── 📄 OrderId.java                         # 订单ID值对象
    ├── 📄 CustomerId.java                      # 客户ID值对象
    └── 📄 Money.java                           # 金额值对象
```

**职责**: 跨模块共享的通用领域概念
**被依赖**: 所有业务模块

### 3. 应用层

#### 📦 b43-modulith-app（主应用程序）
```
b43-modulith-app/
├── 📄 pom.xml                                  # 应用POM（依赖所有模块）
├── 📄 README.md                               # 应用专用文档
└── 📁 src/
    ├── 📁 main/java/com/axinger/modulith/
    │   ├── 📄 ModulithApplication.java        # Spring Boot主应用类
    │   └── 📄 DemoController.java             # 演示控制器
    └── 📁 main/resources/
        └── 📄 application.yml                 # 应用配置文件
```

**职责**: 应用启动、配置管理、演示API
**依赖**: 所有业务模块

## 🔗 模块依赖关系图

```
┌─────────────────────────────────────────────────────────────────┐
│                    b43-modulith-app                             │
│                    (主应用程序)                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────────────────┐
        │                     │                                 │
┌───────▼──────┐    ┌────────▼────────┐                ┌────────▼────────┐
│ order-module  │    │ customer-module │                │ shared-kernel   │
└───────┬───────┘    └─────────────────┘                └────────┬────────┘
        │                                                       │
        ├─────────────────────┐                                   │
        │                     │                                   │
┌───────▼──────┐    ┌────────▼────────┐                ┌────────▼────────┐
│payment-module │    │inventory-module │                │notification-   │
└───────────────┘    └─────────────────┘                │module          │
                                                        └─────────────────┘
```

### 依赖说明

- **shared-kernel** → 被所有业务模块依赖
- **customer-module** → 被 order-module 依赖
- **inventory-module** → 被 order-module 依赖
- **order-module** → 被 payment-module 依赖
- **notification-module** → 独立模块

## 📋 构建顺序

由于模块间的依赖关系，构建时必须按以下顺序：

1. **shared-kernel** (基础依赖)
2. **customer-module** (基础业务模块)
3. **inventory-module** (基础业务模块)
4. **order-module** (依赖customer和inventory)
5. **payment-module** (依赖order)
6. **notification-module** (独立模块)
7. **b43-modulith-app** (依赖所有模块)

Maven会自动处理这个构建顺序。

## 🔧 构建配置

### 父POM (pom.xml)
```xml
<modules>
    <module>order-module</module>
    <module>customer-module</module>
    <module>inventory-module</module>
    <module>payment-module</module>
    <module>notification-module</module>
    <module>shared-kernel</module>
    <module>b43-modulith-app</module>
</modules>
```

### 版本管理
- 所有模块版本统一管理
- 使用 `${project.version}` 继承父POM版本
- 确保模块间版本一致性

## 🚀 运行流程

### 1. 构建阶段
```bash
cd b43-modulith
mvn clean install -DskipTests
```

### 2. 启动阶段
```bash
cd b43-modulith-app
mvn spring-boot:run
```

### 3. 应用启动顺序
1. Spring Boot 启动
2. Spring Modulith 模块发现
3. 各模块 Spring Bean 注册
4. 数据库初始化
5. Web 容器启动
6. Actuator 端点注册

## 📊 模块统计

| 模块 | Java类数 | 主要组件 | 依赖数 |
|------|----------|----------|--------|
| shared-kernel | 3 | OrderId, CustomerId, Money | 0 |
| customer-module | 6 | Customer, Value Objects | 1 |
| inventory-module | 3 | Product, Inventory | 1 |
| order-module | 4 | Order, OrderService, Controller | 3 |
| payment-module | 5 | Payment, PaymentMethod, Refund | 1 |
| notification-module | 6 | Notification, EmailNotification, SmsNotification | 0 |
| b43-modulith-app | 2 | ModulithApplication, DemoController | 6 |

**总计**: 29个Java类，7个模块

## 🎯 设计原则体现

### 1. 单一职责原则 (SRP)
- 每个模块只负责一个业务领域
- 订单模块只处理订单相关逻辑
- 客户模块只处理客户相关逻辑

### 2. 开闭原则 (OCP)
- 模块可扩展（添加新功能）
- 模块可替换（不影响其他模块）

### 3. 依赖倒置原则 (DIP)
- 高层模块不依赖低层模块细节
- 通过接口和抽象进行通信

### 4. 接口隔离原则 (ISP)
- 模块提供清晰的API接口
- 避免胖接口

### 5. 迪米特法则 (LoD)
- 模块间最小知识原则
- 通过事件进行松耦合通信

## 🔍 调试和开发

### 模块隔离开发
```bash
# 单独构建某个模块
cd order-module
mvn clean install
```

### 热重载开发
```bash
# 在应用目录启用开发模式
cd b43-modulith-app
mvn spring-boot:run -Dspring-boot.run.fork=false
```

### 调试技巧
- 使用 `@ConditionalOnProperty` 控制模块加载
- 使用 Profile 区分环境配置
- 使用 Actuator 监控模块状态

## 📚 扩展指南

### 添加新模块
1. 创建新模块目录
2. 编写模块POM文件
3. 定义领域模型
4. 在主POM中添加模块
5. 在主应用中添加依赖

### 模块间通信
- 使用领域事件
- 避免直接调用
- 保持松耦合

### 数据共享
- 通过共享内核
- 避免数据库耦合
- 使用事件同步数据

这个结构为构建大型企业级 Spring Boot 应用提供了清晰的指导和最佳实践。
# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 提供该仓库的代码开发指南。

## 项目概述

这是一个综合性的 Spring Boot 学习和演示仓库，包含多个 Maven 项目，展示各种 Spring Boot 版本和集成技术。该仓库作为 Spring Boot 开发的技术参考和学习资源。

**核心技术栈：**
- **Spring Boot 2.x**：版本 2.7.18 (ax-springboot2-demo)
- **Spring Boot 3.x**：版本 3.5.8 (ax-springboot3-demo)
- **Spring Boot 4.x**：最新版本 (ax-springboot4-demo)
- **Spring Cloud**：2021.0.6 (v2) / 2023.0.3 (v3)
- **Spring Cloud Alibaba**：多个版本
- **Java**：21 (主要版本)，支持 Java 8/11/17
- **Maven**：项目管理和依赖解析

## 仓库结构

该仓库包含多个独立的 Spring Boot 项目：

- **`ax-springboot2-demo/`** - Spring Boot 2.7.x 示例和学习模块
- **`ax-springboot3-demo/`** - Spring Boot 3.5.x 示例和学习模块
- **`ax-springboot4-demo/`** - Spring Boot 4.x 示例（最新）
- **`ax-enterprise-demo/`** - 企业级集成示例
- **`axinger-modules-parent/`** - 父 POM 和共享配置

每个项目都是一个完整的 Maven 多模块项目，都有自己的 CLAUDE.md 和 README.md 文件。

## 常用构建命令

**首先导航到特定项目：**
```bash
cd ax-springboot3-demo  # 或 ax-springboot2-demo 等
```

**构建整个项目：**
```bash
mvn clean install
```

**跳过测试以加快构建：**
```bash
mvn clean install -DskipTests
```

**构建特定模块：**
```bash
cd <模块名称>
mvn clean package
```

**运行 Spring Boot 应用：**
```bash
cd <模块名称>
mvn spring-boot:run
```

**运行测试：**
```bash
# 所有测试
mvn test

# 单个测试类
mvn test -Dtest=MyTestClass

# 单个测试方法
mvn test -Dtest=MyTestClass#myTestMethod
```

**更新项目版本：**
```bash
mvn versions:set -DnewVersion=2026.06.01-3.5
```

## 环境配置

项目使用 Maven 配置文件进行环境管理：

- **`my-prd`** - 生产环境（默认）
- **`my-dev`** - 开发环境

**激活开发环境：**
```bash
mvn clean install -Pmy-dev
```

## 模块分类

### 核心学习模块 (Spring Boot 3.x)
- **`b01-springboot3-learn/`** - Spring Boot 基础、Java 特性、WebFlux
- **`b02-springboot3-db/`** - 数据库集成 (MyBatis、JPA、多数据源)
- **`b04-springboot3-cacheable/`** - 缓存解决方案 (Redis、Caffeine、JetCache)
- **`b07-springboot3-cloud-alibaba/`** - Nacos、Dubbo、Seata、Sentinel
- **`b10-springboot3-mq/`** - 消息队列 (Kafka、RabbitMQ、RocketMQ)
- **`b16-springboot3-security/`** - Spring Security 实现
- **`b21-springboot3-oauth2/`** - OAuth2 和认证流程

### 云基础设施
- **`axinger-spring-boot3-cloud/`** - Spring Cloud 组件
- **`axinger-spring-boot3-common/`** - 共享工具和启动器

### 专业集成模块
项目涵盖广泛的技术集成，包括：
- WebSocket 实现 (JSR-356、STOMP、Netty)
- Excel 处理 (EasyExcel、Apache POI)
- 对象存储 (MinIO、云服务商)
- 搜索引擎 (Elasticsearch)
- 监控 (Prometheus、Spring Boot Admin)
- 工作流引擎 (Flowable)
- AI/ML 集成 (Ollama)
- 容器部署 (Docker)

## 自定义 Spring Boot 启动器

该仓库广泛使用自定义 Spring Boot 启动器来实现模块化功能：

**启动器分类：**
- **工具启动器** - 通用工具、Redis、MongoDB、Excel 处理
- **配置启动器** - 日志、HTTP、JSON 等的自动配置
- **云启动器** - 网关过滤器、服务发现助手

**启动器开发模式：**
1. 工具启动器：`axinger-common/` 目录
2. 配置启动器：`axinger-config/` 目录
3. 命名规范：`*-spring-boot-starter`
4. 在 `META-INF/spring.factories` 或 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中包含自动配置

## 依赖管理

版本属性在每个项目的根 `pom.xml` 中集中管理：
- `spring-boot.version` - Spring Boot 版本
- `spring-cloud.version` - Spring Cloud 版本
- `spring-cloud-alibaba.version` - Spring Cloud Alibaba 版本
- `mybatis-plus.version` - MyBatis Plus 版本
- `java.version` - JDK 版本 (通常 21)

BOM 模块管理内部依赖版本：
- `axinger-common-bom`
- `axinger-config-bom`
- `axinger-cloud-bom`

## 代码组织规范

**模型命名约定：**
- **POJO/Entity** - 数据库实体和简单对象
- **DTO** - 数据传输对象 (API 请求/响应)
- **VO** - 视图对象 (展示层)
- **BO** - 业务对象 (业务层)
- **DO** - 领域对象 (DDD 模式)
- **DAO/Mapper** - 数据访问对象

**包结构：**
- `controller/` - REST 端点和 Web 控制器
- `service/` - 业务逻辑层
- `repository/`/`dao/` - 数据访问层
- `model/`/`entity/` - 领域模型
- `config/` - 配置类
- `common/` - 共享工具

## 文档说明

每个模块都包含详细的中文文档，涵盖：
- 技术实现细节
- 配置示例
- 最佳实践和模式
- 性能优化技术
- 故障排除指南

关键文档文件位于模块内的 `README_FILES/` 目录中。

## 部署脚本

服务管理 Shell 脚本可在 `脚本/` 目录中找到：
- `start.sh` - 服务启动
- `stop.sh` - 服务停止
- `myservice` - 服务管理
- `mymvAndStart` - 部署和重启

## 测试策略

测试遵循 `src/test/java` 中的标准 Maven 结构：
- 业务逻辑的单元测试
- Spring 上下文的集成测试
- 测试配置镜像生产环境设置

运行测试：`mvn test`
构建时跳过测试：`mvn clean install -DskipTests`

## 版本兼容性说明

- **Spring Boot 2.x**：兼容 Java 8-17，较旧的 Spring Cloud 版本
- **Spring Boot 3.x**：需要 Java 17+，Jakarta EE 9+ 命名空间
- **Spring Boot 4.x**：最新特性，需要 Java 17+
- 版本间迁移可能需要更新依赖和导入语句
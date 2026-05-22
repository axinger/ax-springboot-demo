# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 提供该仓库的代码开发指南。

## 项目概述

这是一个大型的多模块 Maven 项目，用于学习和演示 Spring Boot 2 技术。包含众多示例模块，涵盖各种 Spring Boot 集成、云原生模式和自定义 Starter 开发。

**关键技术：**
- Spring Boot 2.7.18
- Spring Cloud 2021.0.6
- Spring Cloud Alibaba 2021.0.6.2
- Java 21
- MyBatis Plus 3.5.16
- Maven

## 构建命令

**构建整个项目：**
```bash
mvn clean install
```

**跳过测试构建：**
```bash
mvn clean install -DskipTests
```

**打包指定模块：**
```bash
cd <module-directory>
mvn clean package
```

**运行 Spring Boot 应用：**
```bash
cd <module-directory>
mvn spring-boot:run
```

**运行单个模块的测试：**
```bash
cd <module-directory>
mvn test
```

**版本管理（更新所有模块）：**
```bash
mvn versions:set -DnewVersion=2026.06.01-2.7
```

## 多环境配置

项目使用 Maven profile 进行环境管理：

- `my-prd` - 生产环境（默认激活）
- `my-dev` - 开发环境

**激活开发环境：**
```bash
mvn clean install -Pmy-dev
```

激活的 profile 会设置 `my-env` 属性，控制配置文件加载。

## 模块结构

### 核心基础设施模块

**`axinger-common/`** - 自定义工具类 starters（共享库）
- `util-spring-boot-starter` - 通用工具类
- `redis-spring-boot-starter` - Redis 集成
- `quartz-spring-boot-starter` - 任务调度
- `mongodb-spring-boot-starter` - MongoDB 支持
- `minio-spring-boot-starter` - MinIO 对象存储
- `excel-spring-boot-starter` - Excel 处理
- `oos-spring-boot-starter` - 对象存储服务
- `axinger-tool-core` - 核心工具类

**`axinger-config/`** - 自动配置 starters
- `base-config-spring-boot-starter` - 基础配置
- `result-config-spring-boot-starter` - 统一响应包装
- `doc-config-spring-boot-starter` - 文档配置
- `mybatis-plus-config-spring-boot-starter` - MyBatis Plus 配置
- `advice-config-spring-boot-starter` - 全局异常处理
- `jackson-config-spring-boot-starter` - JSON 序列化
- `http-config-spring-boot-starter` - HTTP 客户端配置
- `logback-config-spring-boot-starter` - 日志配置
- `executor-config-spring-boot-starter` - 异步执行器配置
- `cors-config-spring-boot-starter` - 跨域配置
- `request-spring-boot-starter` - 请求处理

**`axinger-cloud/`** - 云基础设施
- `cloud-fetch-gateway-starter` - 网关过滤器 starter

### BOM 模块（物料清单）

- `axinger-common-bom/` - 通用依赖 BOM
- `axinger-config-bom/` - 配置 starters BOM
- `axinger-cloud-bom/` - 云依赖 BOM

### 示例模块

**`a01-spring-boot-learn/`** - Spring Boot 基础和 Java 特性
- 请求处理、参数校验、MVC
- Java 8/11/17/21 新特性
- 并发（CompletableFuture、JUC）
- WebFlux、响应式编程
- 注解、AOP、事件机制
- Thymeleaf、脚本

**`a02-spring-boot-db/`** - 数据库集成
- MyBatis、MyBatis Plus、MyBatis Flex
- JPA、Spring Data
- 动态数据源、分布式事务（Atomikos、Narayana）
- MongoDB、PostgreSQL、IoTDB、DuckDB

**`a03-websocket/`** - WebSocket 实现
- Javax WebSocket、Spring WebSocket、STOMP
- Netty WebSocket

**`a04-spring-boot-cacheable/`** - 缓存方案
- Caffeine（本地缓存）
- Redis、Redisson
- JetCache

**`a05-spring-boot-excel/`** - EasyExcel 处理 Excel

**`a06-spring-cloud/`** - Spring Cloud 组件
- Gateway、Config Server/Client
- Consul 集成

**`a07-spring-cloud-alibaba/`** - 阿里云技术栈
- Nacos（服务发现、配置中心）
- Dubbo（RPC）
- Seata（分布式事务）
- Sentinel（熔断限流）

**其他专项模块：**
- `a08-spring-boot-mapstruct/` - 对象映射
- `a09-spring-boot-statemachine/` - 状态机
- `a10-spring-boot-mq/` - 消息队列（Kafka、RabbitMQ、RocketMQ）
- `a11-spring-boot-swagger3/` - API 文档
- `a12-spring-boot-minio/` - 对象存储
- `a14-spring-boot-netty/` - Netty 网络编程
- `a15-auth/` / `a15-spring-boot-shiro/` / `a16-spring-security/` - 认证授权
- `a17-spring-batch/` - 批处理
- `a18-custom-spring-boot-start/` - 自定义 Starter 开发示例
- `a19-scheduled/` / `a20-spring-boot-quartz/` / `a38-xxl-job/` - 任务调度
- `a21-grpc/` - gRPC 服务
- `a21-spring-boot-data-es/` - Elasticsearch
- `a23-integration/` - Spring Integration
- `a25-admin-server/` / `a25-admin-client/` - Spring Boot Admin
- `a26-prometheus/` - 监控
- `a27-springdoc/` / `a27-springdoc2/` - OpenAPI 文档
- `a28-wordToPdf/` - 文档转换
- `a29-elk/` - ELK 栈集成
- `a30-sa-token/` - Sa-Token 认证
- `a31-poi/` / `a32-tika/` - Office 文档处理
- `a33-docker-jar/` / `a33-docker-package/` - Docker 打包
- `a34-keycloak/` - Keycloak 集成
- `a35-springboot2-flowable/` - Flowable 工作流
- `a36-logback/` / `a36-logback2/` - 日志
- `a39-liteflow/` - LiteFlow 规则引擎
- `a40-ai-ollama/` - AI/Ollama 集成
- `a41-ocr/` - OCR 识别
- `a42-queue-disruptor/` - Disruptor 队列
- `a43-camel/` - Apache Camel
- `a44-IoT/` - IoT 相关

## 自定义 Starter 开发

本项目大量使用自定义 Spring Boot starters。创建新 starter 时：

1. 工具类 starters 放在 `axinger-common/`
2. 配置类 starters 放在 `axinger-config/`
3. 命名规范：`*.spring.boot.starter`
4. 包含 `spring-boot-autoconfigure` 和 `spring-boot-configuration-processor` 依赖
5. 在 `META-INF/spring.factories` 或 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中定义自动配置

## 依赖管理

版本属性集中在根 `pom.xml` 中管理：
- `spring-boot.version` - Spring Boot 版本
- `spring-cloud.version` - Spring Cloud 版本
- `spring-cloud-alibaba.version` - Spring Cloud Alibaba 版本
- `mybatis-plus.version` - MyBatis Plus 版本
- `hutool.version` - Hutool 版本
- `guava.version` - Guava 版本

BOM 模块（`axinger-common-bom`、`axinger-config-bom`、`axinger-cloud-bom`）管理内部依赖。

## 模型命名规范

项目遵循标准 Java 模型命名规范（详见 `README_FILES/README.md`）：

- **POJO** - 简单 Java 对象
- **PO/Entity** - 持久化对象（数据库实体）
- **DTO** - 数据传输对象（API 请求/响应）
- **VO** - 视图对象（展示层）
- **BO** - 业务对象（业务逻辑）
- **DO** - 领域对象（DDD 领域模型）
- **DAO/Mapper** - 数据访问对象

## 部署脚本

Shell 脚本位于 `脚本/` 目录：
- `start.sh` - 服务启动脚本
- `stop.sh` - 服务停止脚本
- `myservice` - 服务管理脚本
- `mymvAndStart` - 部署重启脚本

## 测试

测试代码位于 `src/test/java`，遵循标准 Maven 结构。运行测试：
```bash
mvn test
```

构建时跳过测试：
```bash
mvn clean install -DskipTests
```

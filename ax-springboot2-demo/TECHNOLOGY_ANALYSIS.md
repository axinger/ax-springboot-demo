# ax-springboot2-demo 技术栈分析报告

## 项目概述

**项目名称**: ax-springboot2-demo
**Spring Boot 版本**: 2.7.18
**Java 版本**: 21
**Spring Cloud 版本**: 2021.0.6
**Spring Cloud Alibaba 版本**: 2021.0.6.2

这是一个综合性的大型 Spring Boot 2 学习和演示项目，包含 45+ 个模块，涵盖各种技术栈和最佳实践。

## 模块概览

项目包含 45+ 个独立模块，分为以下主要类别：

1. **核心学习模块 (a01-spring-boot-learn)** - 30+ 个子模块
2. **数据库相关 (a02-spring-boot-db)** - 15+ 个子模块
3. **Web 技术 (a03-websocket, a11-swagger)** - 多个子模块
4. **缓存技术 (a04-spring-boot-cacheable)**
5. **云原生 (a06-spring-cloud, a07-spring-cloud-alibaba)**
6. **消息队列 (a10-spring-boot-mq)**
7. **安全认证 (a15-auth, a16-spring-security)**
8. **其他专项技术** - 20+ 个模块

## 核心技术分类详细分析

### 1. 核心 Spring Boot 特性

#### 基础 Starter
- **spring-boot-starter-web**: REST API 开发 (30+ 模块使用)
- **spring-boot-starter-webflux**: 响应式编程 (10+ 模块使用)
- **spring-boot-starter-actuator**: 应用监控 (5+ 模块使用)
- **spring-boot-starter-validation**: 参数校验 (15+ 模块使用)
- **spring-boot-starter-aop**: 面向切面编程 (8+ 模块使用)
- **spring-boot-starter-test**: 测试支持 (所有模块)

#### 模板引擎
- **spring-boot-starter-thymeleaf**: 服务器端页面渲染 (3 模块使用)
- **spring-boot-starter-freemarker**: 模板引擎 (1 模块使用)

#### 日志
- **spring-boot-starter-log4j2**: Log4j2 日志框架 (1 模块使用)
- **logback-config-spring-boot-starter**: 自定义 Logback 配置

#### 自定义 Starter
- **util-spring-boot-starter**: 通用工具类
- **advice-config-spring-boot-starter**: 全局异常处理
- **doc-config-spring-boot-starter**: 文档配置
- **request-spring-boot-starter**: 请求处理
- **result-config-spring-boot-starter**: 统一响应包装
- **jackson-config-spring-boot-starter**: JSON 序列化配置

### 2. 数据库相关技术

#### ORM 框架
- **MyBatis Plus 3.5.16**: 主流 ORM (25+ 模块使用)
- **MyBatis Flex**: 新一代 MyBatis (1 模块使用)
- **原生 MyBatis**: XML/注解配置 (1 模块使用)

#### Spring Data
- **spring-boot-starter-data-jpa**: JPA 实现 (5+ 模块使用)
- **spring-boot-starter-data-jdbc**: Spring Data JDBC
- **spring-boot-starter-data-redis**: Redis 集成
- **spring-boot-starter-data-mongodb**: MongoDB 集成 (1 模块使用)

#### 数据库类型
- **MySQL 8/9**: 主数据库 (20+ 模块使用)
- **PostgreSQL 42.7.3**: 关系型数据库 (5+ 模块使用)
- **H2 Database**: 内存数据库，测试用 (10+ 模块使用)
- **MongoDB**: NoSQL 数据库
- **IoTDB**: 时序数据库 (1 模块使用)
- **DuckDB**: 嵌入式 OLAP 数据库 (1 模块使用)

#### 多数据源和事务
- **dynamic-datasource-spring-boot-starter**: 动态数据源切换
- **Atomikos**: 分布式事务 (1 模块使用)
- **Narayana JTA**: JTA 事务管理器 (1 模块使用)

#### 数据库迁移
- **Flyway**: 数据库版本控制 (5+ 模块使用)

### 3. Web 和 API 开发

#### API 文档
- **springdoc-openapi-ui 1.7.0**: OpenAPI/Swagger UI
- **springdoc-openapi-webflux-ui 1.8.0**: WebFlux 支持
- **knife4j-openapi3-spring-boot-starter 4.5.0**: 增强 Swagger
- **knife4j-gateway-spring-boot-starter 4.5.0**: 网关文档聚合

#### 模板引擎
- **Thymeleaf**: 服务器端页面渲染
- **FreeMarker**: 模板引擎

#### WebSocket
- **Javax WebSocket**: 标准 WebSocket API
- **Spring WebSocket**: Spring 封装 WebSocket
- **STOMP**: 消息协议
- **Netty WebSocket**: 高性能 WebSocket

#### HTTP 客户端
- **spring-cloud-starter-openfeign**: 声明式 HTTP 客户端
- **WebClient**: 响应式 HTTP 客户端
- **OkHttp**: 高效 HTTP 客户端 (1 模块使用)

### 4. 安全认证

#### Spring Security
- **spring-boot-starter-security**: Spring Security (3 模块使用)
- **spring-security-web 5.8.16**: Web 安全

#### 其他安全框架
- **Apache Shiro 1.13.0**: 轻量级安全框架 (1 模块使用)
- **Sa-Token**: 国产认证框架 (1 模块使用)
- **Keycloak**: 身份和访问管理 (1 模块使用)

#### JWT
- **hutool-jwt**: JWT 工具类

#### 加密
- **jasypt-spring-boot-starter 3.0.5**: 配置文件加密

### 5. 消息队列和异步处理

#### 消息中间件
- **Kafka**: 高吞吐量消息系统
- **RabbitMQ**: AMQP 协议消息队列
- **RocketMQ**: 阿里云消息队列
- **Pulsar**: 云原生消息系统

#### 任务调度
- **spring-boot-starter-quartz**: Quartz 任务调度 (1 模块使用)
- **XXL-JOB**: 分布式任务调度平台 (1 模块使用)

#### 工作流引擎
- **Flowable**: BPMN 工作流引擎 (1 模块使用)
- **LiteFlow**: 轻量级规则引擎 (1 模块使用)

#### 异步处理
- **@Async**: Spring 异步注解
- **CompletableFuture**: Java 异步编程
- **Disruptor**: 高性能无锁队列 (1 模块使用)

### 6. 缓存技术

#### 本地缓存
- **Caffeine 2.8.8**: 高性能本地缓存
- **Guava Cache**: Google 缓存库

#### 分布式缓存
- **Redis**: 内存数据结构存储
- **Redisson 3.17.7**: Redis Java 客户端
- **spring-boot-starter-data-redis**: Spring Redis 集成

### 7. 云原生和微服务

#### Spring Cloud
- **spring-cloud-starter-gateway**: API 网关
- **spring-cloud-config**: 配置中心
- **spring-cloud-starter-openfeign**: 声明式服务调用

#### Spring Cloud Alibaba
- **Nacos**: 服务发现和配置中心
- **Dubbo 3.2.14**: RPC 服务框架
- **Seata**: 分布式事务
- **Sentinel**: 熔断限流

#### 服务注册与发现
- **Consul**: 服务发现

#### 监控和管理
- **Spring Boot Admin 2.7.16**: 应用监控
- **Prometheus**: 指标收集 (1 模块使用)
- **Actuator**: 健康检查和管理端点

#### 容器化
- **Docker**: 容器打包和部署

### 8. 存储和文件处理

#### 对象存储
- **MinIO 8.6.0**: 兼容 S3 的对象存储

#### 文件处理
- **Apache POI 5.4.0**: Excel/Word/PPT 处理
- **EasyExcel 3.3.2**: 简化 Excel 操作
- **iTextPDF**: PDF 生成和处理
- **Tika**: 文档内容提取

### 9. 搜索引擎

- **Elasticsearch**: 全文搜索引擎 (1 模块使用)

### 10. 网络编程

- **Netty 4.2.0.Final**: 异步事件驱动网络框架

### 11. 工具库和实用框架

#### 基础工具库
- **Hutool 5.8.42**: 国产 Java 工具包
- **Guava 33.5.0**: Google 核心库
- **Apache Commons Lang3**: 语言工具
- **Apache Commons Collections4**: 集合工具
- **Apache Commons IO**: IO 工具
- **FastJSON2 2.0.49**: JSON 处理

#### 代码生成和映射
- **MapStruct 1.5.5**: 对象映射
- **Lombok 1.18.34**: 代码生成

#### 响应式编程
- **RxJava**: 响应式扩展

### 12. 测试框架

- **spring-boot-starter-test**: Spring Boot 测试
- **JUnit**: 单元测试
- **AssertJ**: 断言库

### 13. 特殊技术

#### AI/ML
- **Ollama**: AI 模型集成 (1 模块使用)

#### IoT
- **OPC-UA**: 工业通信协议 (1 模块使用)

#### 其他
- **Apache Camel**: 企业集成模式 (1 模块使用)
- **gRPC**: 高性能 RPC (1 模块使用)
- **状态机**: Spring Statemachine (1 模块使用)

## 自定义 Spring Boot Starter

项目广泛使用自定义 Starter 来实现模块化功能：

### 工具类 Starter (axinger-common)
- util-spring-boot-starter
- redis-spring-boot-starter
- quartz-spring-boot-starter
- mongodb-spring-boot-starter
- minio-spring-boot-starter
- excel-spring-boot-starter
- oos-spring-boot-starter

### 配置类 Starter (axinger-config)
- base-config-spring-boot-starter
- result-config-spring-boot-starter
- doc-config-spring-boot-starter
- mybatis-plus-config-spring-boot-starter
- advice-config-spring-boot-starter
- jackson-config-spring-boot-starter
- http-config-spring-boot-starter
- logback-config-spring-boot-starter
- executor-config-spring-boot-starter
- cors-config-spring-boot-starter
- request-spring-boot-starter

## 依赖管理

### 核心 BOM
- **spring-boot-dependencies 2.7.18**
- **spring-cloud-dependencies 2021.0.6**
- **spring-cloud-alibaba-dependencies 2021.0.6.2**
- **hutool-bom 5.8.42**
- **camel-spring-boot-dependencies 3.22.4**

### 内部 BOM
- **axinger-common-bom**
- **axinger-config-bom**
- **axinger-cloud-bom**

## 项目结构最佳实践

### 包结构规范
- `controller/`: REST 端点和 Web 控制器
- `service/`: 业务逻辑层
- `repository/`/`dao/`: 数据访问层
- `model/`/`entity/`: 领域模型
- `config/`: 配置类
- `common/`: 共享工具

### 模型命名约定
- **POJO/Entity**: 数据库实体和简单对象
- **DTO**: 数据传输对象 (API 请求/响应)
- **VO**: 视图对象 (展示层)
- **BO**: 业务对象 (业务层)
- **DO**: 领域对象 (DDD 模式)
- **DAO/Mapper**: 数据访问对象

## 环境配置

- **生产环境**: my-prd (默认激活)
- **开发环境**: my-dev
- 通过 Maven Profile 管理不同环境的配置

## 构建和部署

### 构建命令
```bash
mvn clean install                    # 构建所有模块
mvn clean install -DskipTests       # 跳过测试构建
mvn versions:set -DnewVersion=x.x.x # 批量更新版本
```

### 运行命令
```bash
cd <module-directory>
mvn spring-boot:run                  # 运行 Spring Boot 应用
mvn test                             # 运行测试
```

## 总结

ax-springboot2-demo 是一个极其全面的 Spring Boot 2 学习和演示项目，具有以下特点：

1. **技术栈全面**: 涵盖 Spring Boot 生态系统的几乎所有重要组件
2. **模块划分清晰**: 每个技术点都有专门的模块进行演示
3. **自定义 Starter**: 大量使用自定义 Spring Boot Starter 实现模块化
4. **最佳实践**: 遵循行业标准的项目结构和命名规范
5. **版本管理**: 统一的依赖版本管理，避免版本冲突
6. **多环境支持**: 完善的开发和生产环境配置

这是一个非常适合作为 Spring Boot 2 技术参考和学习资源的项目。

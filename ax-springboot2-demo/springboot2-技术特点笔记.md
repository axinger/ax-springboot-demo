# Spring Boot 2 技术特点笔记

## 项目概述

这是一个综合性的 Spring Boot 2.7.18 学习和演示项目，包含众多技术模块，展示了 Spring Boot 2.x 生态系统的各种集成和最佳实践。

## 核心技术栈

### 基础框架
- **Spring Boot**: 2.7.18
- **Spring Cloud**: 2021.0.6
- **Spring Cloud Alibaba**: 2021.0.6.2
- **Java**: 21 (支持 8/11/17)
- **Maven**: 项目管理

### 核心依赖版本管理
```xml
<spring-boot.version>2.7.18</spring-boot.version>
<spring-cloud.version>2021.0.6</spring-cloud.version>
<spring-cloud-alibaba.version>${spring-cloud.version}.2</spring-cloud-alibaba.version>
<mybatis-plus.version>3.5.16</mybatis-plus.version>
<java.version>21</java.version>
```

## 模块架构设计

### 1. 核心学习模块 (a01-spring-boot-learn)

#### 请求处理与验证
- **参数校验**: 使用 `@Validated` + `javax.validation` 注解
- **控制器设计**: RESTful API 设计模式
- **WebFlux**: 响应式编程支持
- **文件处理**: 多格式文件上传下载

```java
@RestController
@RequestMapping("/getTest")
@Validated // GET请求参数校验
public class GetController {
    @GetMapping("/login")
    public Object login(
        @RequestParam @NotEmpty(message = "username不能为空") String username,
        @RequestParam @NotBlank(message = "password不能为空") 
        @Length(min = 2, message = "password长度不能小于2") String password
    ) {
        return List.of(username, password);
    }
}
```

#### Java 特性集成
- **Java 8+**: Stream API, Optional, Lambda
- **Java 11+**: HTTP Client, 新特性
- **Java 17+**: Records, Pattern Matching
- **Java 21**: Virtual Threads (虚拟线程)

#### 并发编程
- **CompletableFuture**: 异步编程
- **JUC**: 并发工具类
- **线程池**: 自定义执行器配置

### 2. 数据库集成模块 (a02-spring-boot-db)

#### ORM 框架
- **MyBatis**: 基础数据访问
- **MyBatis Plus**: 增强功能 (3.5.16)
- **MyBatis Flex**: 新一代 ORM
- **Spring Data JPA**: JPA 实现

#### 数据源管理
- **动态数据源**: 多数据源切换
- **分布式事务**: 
  - Atomikos: JTA 事务管理
  - Narayana: 替代 Atomikos
  - Seata: 阿里云分布式事务

#### 数据库支持
- **MySQL**: 9.3.0
- **PostgreSQL**: 42.7.3
- **MongoDB**: NoSQL 数据库
- **IoTDB**: 时序数据库
- **DuckDB**: 嵌入式分析数据库
- **H2**: 内存数据库 (测试)

#### 数据库工具
- **Flyway**: 数据库版本管理
- **Jasypt**: 配置加密
- **UUID Creator**: 分布式ID生成

### 3. WebSocket 模块 (a03-websocket)

#### 实现方式
- **JSR-356**: 标准 WebSocket API
- **Spring WebSocket**: Spring 封装
- **STOMP**: 消息协议
- **Netty**: 高性能网络框架

### 4. 缓存解决方案 (a04-spring-boot-cacheable)

#### 本地缓存
- **Caffeine**: 高性能本地缓存 (2.8.8)
- **Guava Cache**: Google 缓存实现 (33.5.0)

#### 分布式缓存
- **Redis**: 主流缓存数据库
- **Redisson**: Redis 客户端 (3.36.0)
- **JetCache**: 多级缓存框架

### 5. 消息队列 (a10-spring-boot-mq)

#### 消息中间件
- **Kafka**: 高吞吐量消息系统
- **RabbitMQ**: AMQP 协议消息队列
- **RocketMQ**: 阿里云消息队列

#### Spring Cloud Stream
- **函数式编程模型**
- **Binder 抽象层**

### 6. 云原生技术 (a06-spring-cloud, a07-spring-cloud-alibaba)

#### Spring Cloud 组件
- **Gateway**: API 网关
- **Config**: 配置中心
- **Consul**: 服务发现
- **OpenFeign**: 声明式 HTTP 客户端

#### Spring Cloud Alibaba
- **Nacos**: 服务发现 + 配置中心
- **Dubbo**: RPC 框架 (3.2.14)
- **Sentinel**: 熔断限流
- **Seata**: 分布式事务

#### 网关特性
- **路由配置**: 动态路由
- **过滤器**: 自定义过滤器链
- **限流**: Redis + Lua 脚本
- **跨域**: CORS 配置

### 7. 安全认证 (a15-auth, a16-spring-security, a30-sa-token)

#### 安全框架
- **Spring Security**: 标准安全框架
- **Apache Shiro**: 轻量级安全框架
- **Sa-Token**: 国产认证框架
- **Keycloak**: 身份认证服务器

#### 认证方式
- **OAuth2**: 授权框架
- **JWT**: JSON Web Token
- **Session**: 会话管理

### 8. API 文档 (a11-spring-boot-swagger3, a27-springdoc)

#### 文档工具
- **SpringDoc OpenAPI**: OpenAPI 3.0 支持
- **Knife4j**: 增强 UI 界面 (4.5.0)
- **Swagger**: 传统 API 文档

#### 特性
- **自动生成**: 基于注解的文档生成
- **在线调试**: Swagger UI 集成
- **网关聚合**: 多服务文档聚合

### 9. 对象存储 (a12-spring-boot-minio, a13-springboot-oss)

#### 存储方案
- **MinIO**: 对象存储 (8.6.0)
- **OSS**: 云服务商对象存储

### 10. 搜索引擎 (a21-spring-boot-data-es)

- **Elasticsearch**: 分布式搜索引擎
- **Spring Data Elasticsearch**: 集成支持

## 自定义 Spring Boot Starter

### 架构设计

项目大量使用自定义 Starter 实现模块化：

#### 工具类 Starter (axinger-common)
- `util-spring-boot-starter`: 通用工具类
- `redis-spring-boot-starter`: Redis 集成
- `mongodb-spring-boot-starter`: MongoDB 支持
- `minio-spring-boot-starter`: MinIO 对象存储
- `excel-spring-boot-starter`: Excel 处理
- `quartz-spring-boot-starter`: 任务调度

#### 配置类 Starter (axinger-config)
- `base-config-spring-boot-starter`: 基础配置
- `result-config-spring-boot-starter`: 统一响应包装
- `doc-config-spring-boot-starter`: 文档配置
- `mybatis-plus-config-spring-boot-starter`: MyBatis Plus 配置
- `advice-config-spring-boot-starter`: 全局异常处理
- `jackson-config-spring-boot-starter`: JSON 序列化
- `http-config-spring-boot-starter`: HTTP 客户端配置
- `logback-config-spring-boot-starter`: 日志配置
- `executor-config-spring-boot-starter`: 异步执行器配置
- `cors-config-spring-boot-starter`: 跨域配置

### Starter 开发规范

1. **命名规范**: `*.spring.boot.starter`
2. **依赖管理**: 包含 `spring-boot-autoconfigure`
3. **配置处理**: 使用 `spring-boot-configuration-processor`
4. **自动配置**: 在 `META-INF/spring.factories` 或 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 中定义

## 监控运维

### 监控组件
- **Spring Boot Admin**: 应用监控 (2.7.16)
- **Prometheus**: 指标收集
- **Grafana**: 监控面板
- **ELK**: 日志分析
- **Arthas**: Java 诊断工具
- **HertzBeat**: 实时监控

### 健康检查
- **Actuator**: Spring Boot 健康端点
- **自定义指标**: 业务指标监控

## 任务调度

### 调度框架
- **Spring Scheduled**: 基础调度
- **Quartz**: 功能丰富的调度框架
- **XXL-Job**: 分布式任务调度
- **LiteFlow**: 规则引擎

## 文档处理

### Office 文档
- **Apache POI**: Excel/Word 处理 (5.4.0)
- **EasyExcel**: 阿里 Excel 工具 (3.3.2)
- **POI-TL**: Word 模板引擎
- **iText**: PDF 生成 (5.5.13.3)

### 格式转换
- **Word to PDF**: 文档格式转换
- **OCR**: 文字识别

## 工具库集成

### 常用工具
- **Hutool**: 国产工具库 (5.8.42)
- **Guava**: Google 工具库 (33.5.0)
- **FastJSON2**: 阿里 JSON 库 (2.0.49)
- **MapStruct**: 对象映射 (1.5.5.Final)

### Apache Commons
- **commons-lang3**: 语言工具 (3.17.0)
- **commons-collections4**: 集合工具 (4.5.0)
- **commons-io**: IO 工具 (2.18.0)
- **commons-text**: 文本处理 (1.14.0)
- **commons-csv**: CSV 处理 (1.14.0)

## 网络与通信

### 网络框架
- **Netty**: 异步事件驱动网络框架 (4.2.0.Final)
- **gRPC**: 高性能 RPC 框架

### 协议支持
- **HTTP/HTTPS**: 标准协议
- **WebSocket**: 全双工通信
- **TCP/UDP**: 底层协议

## 容器化部署

### Docker 支持
- **Dockerfile**: 容器镜像构建
- **Docker Compose**: 多容器编排
- **JAR 打包**: 可执行 JAR

## 构建与部署

### Maven 配置
- **多环境配置**: dev/prd 环境切换
- **Profile 管理**: Maven profiles
- **版本管理**: versions-maven-plugin

### 构建命令
```bash
# 构建整个项目
mvn clean install

# 跳过测试
mvn clean install -DskipTests

# 激活开发环境
mvn clean install -Pmy-dev

# 运行应用
mvn spring-boot:run
```

## 测试策略

### 测试类型
- **单元测试**: JUnit + Mockito
- **集成测试**: SpringBootTest
- **性能测试**: JMH
- **契约测试**: Pact

### 测试配置
- **H2 数据库**: 内存数据库测试
- **TestContainers**: 容器化测试

## 代码规范

### 模型命名规范
- **POJO**: 简单 Java 对象
- **Entity**: 数据库实体
- **DTO**: 数据传输对象
- **VO**: 视图对象
- **BO**: 业务对象
- **DO**: 领域对象
- **DAO/Mapper**: 数据访问对象

### 包结构
- `controller/`: REST 控制器
- `service/`: 业务逻辑层
- `repository/`: 数据访问层
- `model/entity/`: 领域模型
- `config/`: 配置类
- `common/`: 共享工具

## 性能优化

### 缓存策略
- **多级缓存**: 本地 + 分布式
- **缓存穿透**: 布隆过滤器
- **缓存雪崩**: 随机过期时间

### 数据库优化
- **连接池**: HikariCP
- **分页优化**: MyBatis Plus 分页
- **批量操作**: 批量插入/更新

### 异步处理
- **@Async**: 异步方法
- **线程池**: 自定义执行器
- **消息队列**: 削峰填谷

## 安全特性

### 应用安全
- **CSRF 防护**: 跨站请求伪造
- **XSS 防护**: 跨站脚本攻击
- **SQL 注入**: 参数化查询
- **配置加密**: Jasypt 加密

### 网络安全
- **HTTPS**: SSL/TLS 加密
- **CORS**: 跨域资源共享
- **限流**: 防止 DoS 攻击

## 扩展技术

### AI/ML 集成
- **Ollama**: 本地 AI 模型
- **OCR**: 文字识别

### IoT 支持
- **物联网协议**: MQTT/CoAP
- **时序数据库**: IoTDB

### 工作流引擎
- **Flowable**: BPMN 工作流

### 规则引擎
- **LiteFlow**: 轻量级规则引擎

## 开发工具

### 代码生成
- **MyBatis Generator**: 代码生成
- **MapStruct**: 对象映射
- **Lombok**: 代码简化

### 开发辅助
- **Spring Boot DevTools**: 热部署
- **JRebel**: 热重载

## 总结

这个项目展示了 Spring Boot 2.x 生态系统的完整技术栈，从基础的 Web 开发到复杂的分布式系统，从传统的数据库操作到现代的云原生架构，涵盖了 Java 企业级开发的各个方面。通过自定义 Starter 的架构设计，体现了模块化、可复用的工程实践理念。
# Spring Modulith 企业级应用 - 主程序

## 🎯 应用概述

这是 Spring Modulith 企业级模块化架构的主应用程序，负责启动和协调各个业务模块。它演示了如何在实际企业应用中使用 Spring Modulith 构建可维护、可扩展的模块化系统。

## 🏗️ 应用架构

### 模块依赖关系

```
┌─────────────────────────────────────────────────────────────┐
│                    b43-modulith-app                         │
│                    (主应用程序)                             │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼──────┐    ┌────────▼────────┐    ┌───────▼──────┐
│ order-module  │    │ customer-module │    │shared-kernel │
└───────────────┘    └─────────────────┘    └──────────────┘
        │
        ├─────────────────────┐
        │                     │
┌───────▼──────┐    ┌────────▼────────┐
│payment-module │    │inventory-module │
└───────────────┘    └─────────────────┘
        │
        │
┌───────▼──────┐
│notification- │
│module        │
└──────────────┘
```

## 🚀 快速开始

### 前置要求

- Java 17+
- Maven 3.6+
- 已构建所有依赖模块

### 构建和运行

```bash
# 1. 首先构建所有模块
cd ..
mvn clean install -DskipTests

# 2. 运行应用程序
cd b43-modulith-app
mvn spring-boot:run

# 3. 访问应用
# 应用启动后，访问以下端点：
# http://localhost:8080/api/demo/info
# http://localhost:8080/h2-console (H2数据库控制台)
# http://localhost:8080/actuator/health (健康检查)
```

### API 演示

#### 获取系统信息
```bash
curl http://localhost:8080/api/demo/info
```

#### 创建演示订单
```bash
curl -X POST http://localhost:8080/api/demo/create-order
```

#### 订单状态流转
```bash
curl -X POST http://localhost:8080/api/demo/order-lifecycle/{orderId}
```

## 📦 核心功能

### 订单管理 API

```
POST   /api/orders              - 创建新订单
POST   /api/orders/{id}/confirm - 确认订单
POST   /api/orders/{id}/cancel  - 取消订单
GET    /api/orders/{id}         - 获取订单详情
```

### 演示 API

```
GET    /api/demo/info           - 获取系统信息
POST   /api/demo/create-order  - 创建演示订单
POST   /api/demo/order-lifecycle/{id} - 演示订单流转
```

## 🔧 配置说明

### 应用配置 (application.yml)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true

# Actuator 监控端点
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,modulith
```

### 数据库配置

- **数据库**: H2 内存数据库
- **控制台**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb
- **用户名**: sa
- **密码**: password

## 🎯 核心特性

### 1. 模块化启动

```java
@SpringBootApplication
@Modulith(
    systemName = "企业级电商平台",
    sharedModules = {"shared-kernel"},
    additionalPackages = {"com.axinger"}
)
public class ModulithApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModulithApplication.class, args);
    }
}
```

### 2. 模块间协作

```java
@RestController
@RequestMapping("/api/demo")
public class DemoController {
    private final OrderService orderService;  // 来自 order-module

    public DemoController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public String createOrder() {
        // 使用多个模块的服务
        CustomerId customerId = CustomerId.create();  // 来自 shared-kernel
        Order order = orderService.createOrder(customerId, items);  // 来自 order-module
        return "订单创建成功! ID: " + order.id();
    }
}
```

### 3. 依赖注入

应用自动发现并注入所有模块中的 Spring Bean：
- `@Service` 注解的服务类
- `@Repository` 注解的仓储接口
- `@RestController` 注解的控制器
- `@Component` 注解的组件

## 📊 监控和管理

### Actuator 端点

- `/actuator/health` - 应用健康状态
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 性能指标
- `/actuator/modulith` - 模块状态（Spring Modulith 特有）

### 日志配置

```yaml
logging:
  level:
    com.axinger: DEBUG
    org.springframework.modulith: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

## 🧪 测试策略

### 应用级测试

```java
@SpringBootTest
class ModulithApplicationTests {
    @Test
    void contextLoads() {
        // 验证 Spring 上下文正常启动
    }
}
```

### 集成测试

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderApiIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateOrder() {
        // 测试完整的 API 流程
    }
}
```

## 🚀 部署选项

### 1. 传统部署

```bash
# 构建可执行 JAR
mvn clean package

# 运行应用
java -jar target/b43-modulith-app-2026.01.01-3.5.jar
```

### 2. Docker 部署

```dockerfile
FROM openjdk:17-jre-slim
COPY target/b43-modulith-app-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t b43-modulith-app .
docker run -p 8080:8080 b43-modulith-app
```

### 3. 生产环境配置

- 配置生产数据库（MySQL/PostgreSQL）
- 配置邮件服务
- 配置日志聚合
- 配置监控告警
- 配置安全认证

## 🔍 故障排除

### 常见问题

1. **模块未找到**
   ```
   解决方案: 确保所有模块都已正确构建并安装到本地仓库
   mvn clean install -DskipTests
   ```

2. **数据库连接失败**
   ```
   解决方案: 检查数据库配置和连接信息
   ```

3. **端口冲突**
   ```
   解决方案: 修改 server.port 配置或停止占用端口的应用
   ```

### 调试技巧

- 启用 DEBUG 日志级别查看详细启动信息
- 使用 Actuator 端点检查应用状态
- 查看 H2 控制台验证数据
- 使用 Spring Boot DevTools 进行热部署

## 📚 相关资源

- [父项目 README](../README.md) - 完整的项目文档
- [模块详细设计](../MODULES.md) - 各模块详细设计
- [架构指南](../SPRING_MODULITH_GUIDE.md) - 企业级架构指南
- [Spring Modulith 官方文档](https://docs.spring.io/spring-modulith/docs/current/reference/html/)

## 🏁 总结

这个主应用程序展示了：

1. **模块化应用启动** - 如何启动包含多个业务模块的 Spring Boot 应用
2. **模块间协作** - 演示不同模块如何协同工作
3. **企业级配置** - 生产就绪的应用配置
4. **监控和管理** - 完整的运维支持
5. **API 设计** - 清晰的 REST API 设计

通过这个应用，您可以了解如何在实际项目中使用 Spring Modulith 构建企业级模块化系统。
# Spring Modulith 快速开始指南

## 🚀 快速启动

### 1. 环境要求
- Java 17+
- Maven 3.6+
- Git

### 2. 克隆和构建

```bash
# 进入项目目录
cd b43-modulith

# 构建项目（跳过测试）
mvn clean install -DskipTests

# 或者构建并运行测试
mvn clean install
```

### 3. 运行应用

```bash
# 方式1: 使用 Maven
mvn spring-boot:run

# 方式2: 运行 JAR 文件
java -jar b43-modulith-app/target/b43-modulith-app-*.jar
```

### 4. 访问应用

- **主应用**: http://localhost:8080
- **H2 控制台**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - 用户名: `sa`
  - 密码: `123456`
- **Actuator**: http://localhost:8080/actuator
- **Modulith 文档**: http://localhost:8080/actuator/modulith

## 📋 API 快速测试

### 1. 注册客户

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "张",
    "lastName": "三",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "province": "北京市",
    "city": "北京市",
    "district": "海淀区",
    "street": "中关村大街",
    "detail": "1号楼101室",
    "zipCode": "100000"
  }'
```

### 2. 激活客户

```bash
curl -X POST http://localhost:8080/api/customers/{customerId}/activate
```

### 3. 创建产品

```bash
curl -X POST http://localhost:8080/api/inventory/products \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "PROD-001",
    "name": "iPhone 15",
    "description": "苹果最新手机",
    "category": "ELECTRONICS"
  }'
```

### 4. 初始化库存

```bash
curl -X POST http://localhost:8080/api/inventory/products/PROD-001/initialize \
  -H "Content-Type: application/json" \
  -d '{
    "initialQuantity": 100,
    "safetyStockLevel": 10
  }'
```

### 5. 创建订单

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "{customer-id}",
    "items": [
      {
        "productId": "PROD-001",
        "productName": "iPhone 15",
        "quantity": 1,
        "price": 6999.00
      }
    ]
  }'
```

### 6. 创建支付

```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "{order-id}",
    "amount": 6999.00,
    "paymentMethod": "ALIPAY"
  }'
```

### 7. 处理支付

```bash
curl -X POST http://localhost:8080/api/payments/{payment-id}/process
```

### 8. 确认订单

```bash
curl -X POST http://localhost:8080/api/orders/{order-id}/confirm
```

### 9. 发送通知

```bash
# 发送邮件通知
curl -X POST http://localhost:8080/api/notifications/email \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "customer@example.com",
    "subject": "订单确认",
    "content": "您的订单已确认，感谢您的购买！",
    "priority": "NORMAL"
  }'

# 发送短信通知
curl -X POST http://localhost:8080/api/notifications/sms \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "13800138000",
    "content": "您的订单已确认，感谢您的购买！",
    "priority": "NORMAL"
  }'
```

## 🏗️ 项目结构

```
b43-modulith/
├── b43-modulith-app/           # 主应用模块
├── customer-module/            # 客户模块
├── inventory-module/           # 库存模块
├── order-module/               # 订单模块
├── payment-module/             # 支付模块
├── notification-module/        # 通知模块
├── shared-kernel/              # 共享内核
├── pom.xml                     # 根 POM
└── README.md                   # 项目文档
```

## 🔧 配置说明

### 数据库配置 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: 123456
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

### 邮件配置
```yaml
spring:
  mail:
    host: smtp.example.com
    port: 587
    username: your-email@example.com
    password: your-password
```

## 📊 监控和运维

### Actuator 端点
- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 性能指标
- `/actuator/modulith` - Modulith 模块信息

### 日志级别
```yaml
logging:
  level:
    com.axinger: DEBUG
    org.springframework.modulith: INFO
```

## 🧪 测试策略

### 运行测试
```bash
# 运行所有测试
mvn test

# 运行特定模块测试
cd order-module
mvn test

# 跳过测试构建
mvn clean install -DskipTests
```

## 🐳 Docker 部署（待实现）

```bash
# 构建镜像
docker build -t b43-modulith .

# 运行容器
docker run -p 8080:8080 b43-modulith
```

## 🚨 故障排除

### 常见问题

1. **端口冲突**
   ```bash
   # 修改端口
   server.port=8081
   ```

2. **数据库连接问题**
   - 检查 H2 控制台连接参数
   - 确认数据库配置正确

3. **邮件发送失败**
   - 检查邮件服务器配置
   - 确认网络连接正常

4. **模块依赖错误**
   - 检查 `@ApplicationModule` 注解的 `allowedDependencies`
   - 运行 `mvn verify` 检测循环依赖

### 调试技巧

1. **启用 SQL 日志**
   ```yaml
   spring.jpa.show-sql: true
   spring.jpa.properties.hibernate.format_sql: true
   ```

2. **启用 Modulith 调试**
   ```yaml
   logging.level.org.springframework.modulith: DEBUG
   ```

3. **使用 Actuator**
   - 访问 `/actuator/modulith` 查看模块状态
   - 使用 `/actuator/health` 检查应用健康状态

## 📚 学习资源

### Spring Modulith 文档
- [官方文档](https://docs.spring.io/spring-modulith/docs/current/reference/html/)
- [GitHub 仓库](https://github.com/spring-projects/spring-modulith)

### 相关技术
- Spring Boot 3.x
- Spring Data JPA
- Domain-Driven Design
- RESTful API Design

## 🎯 最佳实践

1. **模块设计**
   - 保持模块职责单一
   - 明确定义模块边界
   - 避免循环依赖

2. **领域建模**
   - 使用聚合根保护业务规则
   - 值对象确保数据完整性
   - 领域事件解耦业务逻辑

3. **API 设计**
   - 遵循 RESTful 原则
   - 使用合适的 HTTP 状态码
   - 提供清晰的错误信息

4. **测试策略**
   - 编写单元测试覆盖核心逻辑
   - 集成测试验证模块协作
   - 架构测试确保设计约束

## 🚀 下一步

1. 添加完整的测试覆盖
2. 实现领域事件处理器
3. 添加 Docker 配置
4. 集成消息队列
5. 添加 API 文档（Swagger）
6. 实现缓存层
7. 添加性能监控

---

**祝您在 Spring Modulith 的学习和使用中取得成功！** 🎉
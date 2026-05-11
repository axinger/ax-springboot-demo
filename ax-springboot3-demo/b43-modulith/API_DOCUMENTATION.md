# API 文档指南

## 🚀 Swagger UI 集成

项目已成功集成 `springdoc-openapi-ui`，提供完整的 API 文档和交互式测试功能。

## 📖 访问 API 文档

### 启动应用后访问

1. **Swagger UI**: http://localhost:8080/swagger-ui.html
2. **OpenAPI JSON**: http://localhost:8080/v3/api-docs
3. **Actuator**: http://localhost:8080/actuator

### 快速启动

```bash
# 构建并运行项目
cd b43-modulith
mvn clean install
mvn spring-boot:run

# 访问 Swagger UI
open http://localhost:8080/swagger-ui.html
```

## 📋 API 模块概览

### 🛒 订单管理 API
- `POST /api/orders` - 创建新订单
- `POST /api/orders/{orderId}/confirm` - 确认订单
- `POST /api/orders/{orderId}/cancel` - 取消订单
- `GET /api/orders/{orderId}` - 获取订单详情

### 👥 客户管理 API
- `POST /api/customers` - 注册新客户
- `POST /api/customers/{customerId}/activate` - 激活客户
- `PUT /api/customers/{customerId}/address` - 更新客户地址
- `POST /api/customers/{customerId}/suspend` - 暂停客户
- `POST /api/customers/{customerId}/reactivate` - 恢复客户
- `GET /api/customers/{customerId}` - 获取客户详情
- `GET /api/customers/by-email/{email}` - 根据邮箱查找客户
- `GET /api/customers/by-phone/{phone}` - 根据手机号查找客户

### 📦 库存管理 API
- `POST /api/inventory/products` - 创建新产品
- `POST /api/inventory/products/{productId}/activate` - 激活产品
- `POST /api/inventory/products/{productId}/initialize` - 初始化库存
- `POST /api/inventory/products/{productId}/reserve` - 预留库存
- `POST /api/inventory/products/{productId}/release` - 释放库存
- `POST /api/inventory/products/{productId}/consume` - 消耗库存
- `POST /api/inventory/products/{productId}/add` - 增加库存
- `GET /api/inventory/products/{productId}` - 获取库存信息

### 💳 支付管理 API
- `POST /api/payments` - 创建支付
- `POST /api/payments/{paymentId}/process` - 处理支付
- `POST /api/payments/{paymentId}/cancel` - 取消支付
- `POST /api/payments/{paymentId}/refund` - 创建退款
- `GET /api/payments/{paymentId}` - 获取支付详情
- `GET /api/payments/by-order/{orderId}` - 根据订单获取支付

### 📧 通知管理 API
- `POST /api/notifications/email` - 发送邮件通知
- `POST /api/notifications/sms` - 发送短信通知
- `POST /api/notifications/in-app` - 发送应用内通知
- `POST /api/notifications/{notificationId}/mark-read` - 标记通知为已读
- `GET /api/notifications/{notificationId}` - 获取通知详情
- `GET /api/notifications/user/{recipient}` - 获取用户通知列表

## 🎯 使用 Swagger UI

### 1. 浏览 API
- 左侧显示所有 API 分组
- 点击展开查看具体接口
- 每个接口显示详细的参数说明

### 2. 测试 API
- 点击 "Try it out" 按钮
- 填写必要的参数
- 点击 "Execute" 执行请求
- 查看响应结果和状态码

### 3. 查看模型定义
- 页面底部显示所有数据模型
- 包含字段说明和示例
- 支持模型展开/收起

## 📊 API 模型说明

### 订单相关模型
```json
{
  "orderId": "string",
  "customerId": "string",
  "status": "PENDING|CONFIRMED|PAID|SHIPPED|DELIVERED|CANCELLED|REFUNDED",
  "totalAmount": 0.0
}
```

### 客户相关模型
```json
{
  "customerId": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "email": "string",
  "phone": "string",
  "status": "PENDING|ACTIVE|INACTIVE|SUSPENDED|BLACKLISTED"
}
```

### 库存相关模型
```json
{
  "productId": "string",
  "totalQuantity": 0,
  "availableQuantity": 0,
  "reservedQuantity": 0,
  "safetyStockLevel": 0,
  "isLowStock": true
}
```

### 支付相关模型
```json
{
  "paymentId": "string",
  "orderId": "string",
  "amount": 0.0,
  "paymentMethod": "ALIPAY|WECHAT_PAY|CREDIT_CARD|BANK_TRANSFER",
  "status": "PENDING|PROCESSING|COMPLETED|FAILED|CANCELLED|EXPIRED"
}
```

### 通知相关模型
```json
{
  "notificationId": "string",
  "recipient": "string",
  "subject": "string",
  "content": "string",
  "type": "EMAIL|SMS|IN_APP",
  "priority": "LOW|NORMAL|HIGH|URGENT",
  "status": "PENDING|PROCESSING|SENT|DELIVERED|READ|FAILED"
}
```

## 🔧 配置说明

### Swagger 配置 (application.yml)
```yaml
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
  show-actuator: true
  default-consumes-media-type: application/json
  default-produces-media-type: application/json
  disable-swagger-default-url: true
```

### 自定义 OpenAPI 配置
```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Spring Modulith 企业级电商平台 API"))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("本地开发环境")
            ));
    }
}
```

## 🎨 Swagger UI 特性

### 1. 交互式文档
- 实时 API 测试
- 参数自动验证
- 响应格式预览

### 2. 代码生成
- 支持多种语言 SDK 生成
- curl 命令示例
- HTTP 请求示例

### 3. 文档导出
- OpenAPI JSON 格式
- PDF/HTML 文档生成
- 离线文档支持

## 🔍 调试技巧

### 1. API 测试
- 使用 Swagger UI 快速测试接口
- 查看请求/响应头信息
- 验证数据格式

### 2. 问题排查
- 检查 HTTP 状态码
- 查看错误响应信息
- 验证请求参数格式

### 3. 性能监控
- 查看请求响应时间
- 监控 API 调用频率
- 分析错误率统计

## 📚 学习资源

### Swagger/OpenAPI
- [OpenAPI 规范](https://spec.openapis.org/oas/latest.html)
- [Swagger 官方文档](https://swagger.io/docs/)
- [springdoc-openapi 文档](https://springdoc.org/)

### API 设计最佳实践
- RESTful API 设计指南
- HTTP 状态码使用规范
- API 版本控制策略

## 🚀 快速开始示例

### 1. 创建客户
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

### 2. 创建订单
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "{customer-id}",
    "items": [{
      "productId": "PROD-001",
      "productName": "iPhone 15",
      "quantity": 1,
      "price": 6999.00
    }]
  }'
```

### 3. 处理支付
```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "{order-id}",
    "amount": 6999.00,
    "paymentMethod": "ALIPAY"
  }'
```

## 🎉 总结

通过 Swagger UI，您可以：
- ✅ 实时浏览和测试所有 API
- ✅ 查看详细的接口文档
- ✅ 生成客户端代码
- ✅ 导出 API 规范
- ✅ 提高开发效率

**现在就开始使用 Swagger UI 探索和测试您的 API 吧！** 🎯
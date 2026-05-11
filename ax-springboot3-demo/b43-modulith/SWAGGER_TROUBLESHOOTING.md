# Swagger/OpenAPI 文档问题排查指南

## 🚨 常见问题：404 Not Found

### 问题描述
用户报告 `/v3/api-docs/orders` 返回 404 错误。

### 原因分析
这是 SpringDoc OpenAPI 的一个已知限制。虽然 `application.yml` 中配置了模块特定的 URL 端点：
```yaml
urls:
  - name: 订单模块
    url: /v3/api-docs/orders
  - name: 客户模块
    url: /v3/api-docs/customers
  # ...其他模块
```

但是这些端点**不会自动生成**，因为：
1. **业务模块不是独立的 Spring Boot 应用**
2. **每个业务模块没有自己的 @SpringBootApplication 主类**
3. **SpringDoc 只能扫描到主应用模块中的控制器**

### 实际可用的 API 文档

#### ✅ 正确可用的端点
- **主 API 文档**: http://localhost:8080/v3/api-docs
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Actuator 健康检查**: http://localhost:8080/actuator/health

#### ❌ 不会自动生成的端点
- `/v3/api-docs/orders` - 404 Not Found
- `/v3/api-docs/customers` - 404 Not Found
- `/v3/api-docs/inventory` - 404 Not Found
- `/v3/api-docs/payments` - 404 Not Found
- `/v3/api-docs/notifications` - 404 Not Found

### 解决方案

#### 方案一：使用主 API 文档（推荐）
所有模块的 API 都在主 `v3/api-docs` 中：

```bash
curl http://localhost:8080/v3/api-docs
```

这个响应包含了所有业务模块的 API 定义。

#### 方案二：为每个模块添加独立的 API 文档支持

如果需要模块特定的文档端点，需要：

1. **在每个业务模块中添加 OpenAPI 配置**
2. **为每个模块创建独立的 Spring Boot 应用入口**
3. **或者创建一个聚合的文档服务**

#### 方案三：手动组织 API 文档

在 Swagger UI 中，您可以：
- 浏览所有 API 端点
- 按标签分组查看（如 demo-controller）
- 使用搜索功能查找特定模块的 API

### 当前项目架构的限制

这个项目采用 **Spring Modulith 模块化架构**，有以下特点：

✅ **优点**:
- 模块间松耦合
- 可以独立开发和测试
- 清晰的模块边界
- 生产就绪的架构

❌ **局限性**:
- 业务模块不是独立的 Web 应用
- 无法自动生成模块特定的 OpenAPI 文档
- 需要在主应用中统一管理 API 文档

### 推荐的替代方案

#### 1. 使用标签分组（最佳实践）
在 Swagger UI 中，API 已经按控制器进行了分组：
- `demo-controller`: 演示相关的 API
- 业务模块的 API 会显示在 Swagger UI 中

#### 2. 创建 API 文档索引页面
```html
<!DOCTYPE html>
<html>
<head>
    <title>Spring Modulith API 文档索引</title>
</head>
<body>
    <h1>📚 API 文档导航</h1>

    <h2>🔗 快速访问</h2>
    <ul>
        <li><a href="/swagger-ui.html">Swagger UI (交互式)</a></li>
        <li><a href="/v3/api-docs">OpenAPI JSON 文档</a></li>
        <li><a href="/actuator/health">健康检查</a></li>
    </ul>

    <h2>📋 业务模块 API</h2>
    <p>所有业务模块的 API 都包含在 <strong>/v3/api-docs</strong> 中</p>
    <p>在 Swagger UI 中使用搜索功能查找特定模块:</p>
    <ul>
        <li>搜索 "order" 查看订单相关 API</li>
        <li>搜索 "customer" 查看客户相关 API</li>
        <li>搜索 "inventory" 查看库存相关 API</li>
        <li>搜索 "payment" 查看支付相关 API</li>
        <li>搜索 "notification" 查看通知相关 API</li>
    </ul>

    <h2>🎯 演示 API</h2>
    <ul>
        <li><a href="/api/demo/info">系统信息 (/api/demo/info)</a></li>
        <li><a href="/api/demo/demo-values">值对象演示 (/api/demo/demo-values)</a></li>
        <li><a href="/api/demo/modules">模块信息 (/api/demo/modules)</a></li>
        <li><a href="/api/demo/health">健康检查 (/api/demo/health)</a></li>
    </ul>
</body>
</html>
```

#### 3. 扩展主应用以支持模块文档

如果需要模块特定的文档端点，可以在主应用中添加：

```java
@Configuration
public class ModuleApiDocsConfig {

    @Bean
    public GroupedOpenApi orderModuleApi() {
        return GroupedOpenApi.builder()
                .group("order-module")
                .pathsToMatch("/api/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi customerModuleApi() {
        return GroupedOpenApi.builder()
                .group("customer-module")
                .pathsToMatch("/api/customers/**")
                .build();
    }

    // 其他模块...
}
```

然后在 application.yml 中配置对应的 URL：

```yaml
springdoc:
  swagger-ui:
    urls:
      - name: 订单模块
        url: /v3/api-docs/order-module
      - name: 客户模块
        url: /v3/api-docs/customer-module
```

### 最佳实践建议

1. **使用 Swagger UI 搜索功能** - 比特定端点更方便
2. **关注 API 标签** - Swagger 会自动为不同控制器打标签
3. **利用 OpenAPI JSON** - 可以编程处理完整的 API 文档
4. **在团队文档中说明** - 让团队成员了解这个架构限制

### 验证当前状态

您可以通过以下命令验证当前可用的 API：

```bash
# 查看所有可用的 API 端点
curl -s http://localhost:8080/v3/api-docs | grep '"path":'

# 查看 Swagger UI 中显示的 API 数量
curl -s http://localhost:8080/swagger-ui.html | grep -c "operationId"

# 检查健康状态
curl -s http://localhost:8080/actuator/health | jq '.status'
```

### 总结

**这不是一个 bug，而是一个架构设计选择的结果。**

Spring Modulith 的模块化架构带来了：
- ✅ 更好的模块隔离性
- ✅ 更清晰的依赖管理
- ✅ 更灵活的部署选项

但代价是：
- ❌ 需要手动管理模块特定的 API 文档端点
- ❌ 无法自动生成每个模块的独立 OpenAPI 文档

**推荐使用 Swagger UI 的搜索和过滤功能来查找特定模块的 API。** 🚀
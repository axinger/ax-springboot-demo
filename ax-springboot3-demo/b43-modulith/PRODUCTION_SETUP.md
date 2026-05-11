# Spring Modulith 生产环境配置指南

## 📋 概述

本文档说明如何在**不引入第三方消息队列**（Kafka/RabbitMQ）的情况下，使用 Spring Modulith 构建可靠的生产级应用。

---

## ✅ 已添加的生产级功能

### 1. **Actuator 监控支持** ⭐⭐⭐

#### Maven 依赖
```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-actuator</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### 可用端点

| 端点 | URL | 说明 |
|------|-----|------|
| 健康检查 | `/actuator/health` | 应用健康状态 |
| 模块信息 | `/actuator/modulith` | 模块和事件统计 |
| 环境信息 | `/actuator/env` | 环境变量（脱敏） |
| 指标监控 | `/actuator/metrics` | 性能指标 |

#### 使用示例

```bash
# 查看模块和事件状态
curl http://localhost:8080/actuator/modulith | jq

# 返回示例
{
  "modules": [
    {
      "name": "order-module",
      "displayName": "订单模块",
      "dependencies": ["customer-module", "inventory-module"]
    }
  ],
  "eventPublications": {
    "pending": 5,      // 待处理事件
    "completed": 100,  // 已完成事件
    "failed": 2        // 失败事件
  }
}
```

---

### 2. **事件持久化和重试** ⭐⭐⭐

#### 配置说明

```yaml
spring:
  modulith:
    events:
      jdbc:
        # 自动创建 EVENT_PUBLICATION 表
        schema-initialization:
          enabled: true
        
        # 事件清理策略
        completion-retention: 7d  # 已完成事件保留7天
        cleanup:
          enabled: true
          cron: "0 0 2 * * ?"  # 每天凌晨2点清理
        
        # 重试机制
        retry:
          max-attempts: 5       # 最大重试5次
          initial-delay: 5s     # 初始延迟5秒
          max-delay: 1m         # 最大延迟1分钟
          multiplier: 2.0       # 指数退避（5s → 10s → 20s → 40s → 1m）
```

#### 工作原理

```
事件发布
  ↓
保存到数据库 (EVENT_PUBLICATION 表)
  ↓
异步处理
  ↓
成功 → 标记为 COMPLETED
失败 → 等待重试（指数退避）
  ↓
超过最大重试次数 → 标记为 FAILED
  ↓
定期清理已完成的事件
```

#### 优势

✅ **无需 Kafka/RabbitMQ**：使用现有数据库  
✅ **可靠性保证**：事件不丢失，支持重试  
✅ **简化架构**：减少外部依赖  
✅ **易于调试**：可直接查询数据库  

---

### 3. **事件监控服务** ⭐⭐

#### 功能特性

- ✅ 每5分钟检查未完成事件
- ✅ 检测超时事件（>1小时）
- ✅ 每日生成统计报告
- ✅ 提供事件统计 API

#### 代码位置

[EventMonitoringService.java](file:///D:/code/ax-springboot-demo/ax-springboot3-demo/b43-modulith/b43-modulith-app/src/main/java/com/axinger/modulith/service/EventMonitoringService.java)

#### 日志示例

```
2026-05-11 10:30:00 WARN  - 发现 5 个未完成的事件
2026-05-11 10:30:00 ERROR - 事件处理超时: xxx - 类型: OrderCreatedEvent, 发布时间: 2026-05-11T09:00:00
2026-05-11 01:00:00 INFO  - === 每日事件统计报告 ===
2026-05-11 01:00:00 INFO  - 未完成事件数: 3
2026-05-11 01:00:00 INFO  - 报告生成时间: 2026-05-11T01:00:00
```

---

## 🔧 生产环境关键配置

### 1. **数据库连接池优化**

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20        # 最大连接数
      minimum-idle: 5              # 最小空闲连接
      connection-timeout: 30000    # 连接超时30秒
      idle-timeout: 600000         # 空闲超时10分钟
      max-lifetime: 1800000        # 最大生命周期30分钟
```

### 2. **JPA 性能优化**

```yaml
spring:
  jpa:
    properties:
      hibernate:
        # 批量操作
        jdbc.batch_size: 50
        order_inserts: true
        order_updates: true
        
        # 缓存
        cache.use_second_level_cache: true
        cache.use_query_cache: true
        
        # SQL 优化
        generate_statistics: false  # 生产环境关闭统计
```

### 3. **线程池配置**

```yaml
spring:
  task:
    execution:
      pool:
        core-size: 10           # 核心线程数
        max-size: 50            # 最大线程数
        queue-capacity: 1000    # 队列容量
        keep-alive: 60s         # 空闲线程存活时间
```

### 4. **日志配置**

```yaml
logging:
  level:
    root: INFO
    com.axinger: DEBUG
    org.springframework.modulith: INFO
    org.hibernate.SQL: WARN     # 生产环境关闭 SQL 日志
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
    max-size: 100MB
    max-history: 30
```

---

## 📊 监控和告警

### 1. **Prometheus 集成**

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

```yaml
management:
  metrics:
    export:
      prometheus:
        enabled: true
  endpoints:
    web:
      exposure:
        include: health,info,metrics,modulith,prometheus
```

访问：http://localhost:8080/actuator/prometheus

### 2. **关键指标**

| 指标 | 说明 | 告警阈值 |
|------|------|---------|
| `modulith.events.pending` | 待处理事件数 | > 100 |
| `modulith.events.failed` | 失败事件数 | > 10 |
| `hikaricp.connections.active` | 活跃连接数 | > 80% |
| `jvm.memory.used` | JVM 内存使用 | > 85% |

### 3. **Grafana 仪表板**

推荐监控项：
- 事件处理延迟
- 事件成功率
- 数据库连接池状态
- JVM 内存和 GC
- HTTP 请求响应时间

---

## 🛡️ 安全性和稳定性

### 1. **幂等性处理**

```java
@Component
public class OrderEventHandler {
    
    @TransactionalEventListener
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        String eventId = event.orderId().toString();
        
        // 检查是否已处理
        if (eventProcessingRepository.existsByEventId(eventId)) {
            log.warn("事件已处理，跳过: {}", eventId);
            return;
        }
        
        // 处理业务逻辑
        processOrder(event);
        
        // 记录处理状态
        eventProcessingRepository.save(new EventProcessingRecord(eventId));
    }
}
```

### 2. **优雅停机**

```yaml
server:
  shutdown: graceful  # 优雅停机
  
spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s  # 停机超时30秒
```

### 3. **健康检查增强**

```yaml
management:
  health:
    db:
      enabled: true
    diskspace:
      enabled: true
      threshold: 10MB  # 磁盘空间低于10MB时告警
```

---

## 🚀 部署建议

### 1. **Docker 部署**

```dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY target/b43-modulith-app.jar app.jar

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. **环境变量配置**

```bash
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:h2:file:/data/modulith_db
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# 事件清理
SPRING_MODULITH_EVENTS_JDBC_COMPLETION_RETENTION=7d

# JVM 参数
JAVA_OPTS=-Xms512m -Xmx2g -XX:+UseG1GC
```

### 3. **Kubernetes 配置**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: modulith-app
spec:
  replicas: 2
  template:
    spec:
      containers:
      - name: app
        image: modulith-app:latest
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "2000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 5
```

---

## 🐛 故障排查

### Q1: 事件积压过多？

**检查**：
```bash
# 查看待处理事件数
curl http://localhost:8080/actuator/modulith | jq '.eventPublications.pending'

# 查询数据库
SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL;
```

**解决**：
- 增加线程池大小
- 检查事件处理器是否有性能瓶颈
- 考虑水平扩展（多实例）

### Q2: 事件处理失败？

**检查**：
```bash
# 查看失败事件
SELECT * FROM EVENT_PUBLICATION 
WHERE status = 'FAILED' 
ORDER BY publication_date DESC 
LIMIT 10;
```

**解决**：
- 查看应用日志定位错误
- 手动重放失败事件
- 修复代码后重启

### Q3: 数据库连接耗尽？

**检查**：
```bash
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
```

**解决**：
- 增加连接池大小
- 检查是否有连接泄漏
- 优化慢查询

---

## 📝 总结

### 当前配置优势

✅ **无需 Kafka/RabbitMQ**：降低架构复杂度  
✅ **事件可靠性**：持久化 + 重试机制  
✅ **完整监控**：Actuator + 自定义监控服务  
✅ **生产就绪**：优雅停机、健康检查、日志管理  

### 适用场景

- ✅ 中小型应用（QPS < 10000）
- ✅ 团队规模适中（< 50人）
- ✅ 希望简化运维
- ✅ 未来可能演进到微服务

### 何时考虑引入 MQ？

- ❌ QPS > 10000
- ❌ 需要跨数据中心通信
- ❌ 需要复杂的路由规则
- ❌ 已有成熟的 MQ 基础设施

---

## 🔗 相关文档

- [SPRING_MODULITH_GUIDE.md](SPRING_MODULITH_GUIDE.md) - 项目完整指南
- [DATABASE_PERSISTENCE.md](DATABASE_PERSISTENCE.md) - 数据库持久化方案
- [API_TEST_GUIDE.md](API_TEST_GUIDE.md) - API 测试指南

---

**祝您的生产环境稳定运行！** 🎉

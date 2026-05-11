# spring-modulith-starter-jdbc 使用指南

## 📋 概述

`spring-modulith-starter-jdbc` 是 Spring Modulith 提供的**事件持久化模块**，它将领域事件保存到数据库，确保事件的可靠处理。

---

## ✅ 核心功能

### 1. **事件持久化**

```java
// 发布事件
eventPublisher.publishEvent(new OrderCreatedEvent(orderId, customerId, amount));

// spring-modulith-starter-jdbc 自动：
// 1. 将事件保存到 EVENT_PUBLICATION 表
// 2. 异步处理事件
// 3. 处理成功后标记为完成
// 4. 即使应用崩溃，重启后继续处理
```

### 2. **自动重试**

Spring Modulith 内置基础重试机制，但**不支持 YAML 配置**。
如需自定义重试策略，需要通过代码实现（见下方“自定义实现”章节）。

### 3. **事件清理**

Spring Modulith **不提供自动清理配置**，需要通过代码实现。

**推荐使用 `completion-mode: DELETE`**：
```yaml
spring:
  modulith:
    events:
      completion-mode: DELETE  # 完成后直接删除，无需手动清理
```

或者通过定时任务手动清理（见下方“自定义实现”章节）。

### 4. **监控支持**

- ✅ Actuator 端点查看事件状态
- ✅ 数据库直接查询
- ✅ 自定义监控服务

---

## 🔧 配置说明

### 1. **Maven 依赖**

已在父 POM 中配置：

```xml
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter-jdbc</artifactId>
</dependency>
```

**注意**：版本由 BOM 统一管理，无需指定版本号。

---

### 2. **YAML 配置**

#### 基础配置（必需）

```yaml
spring:
  modulith:
    events:
      jdbc:
        schema-initialization:
          enabled: true  # 自动创建 EVENT_PUBLICATION 表
```

**注意**：当前项目已升级到 **Spring Modulith 1.4.8**，支持所有高级配置。

#### 完整配置（推荐）

**当前项目使用 Spring Modulith 1.4.11**，支持以下配置：

```yaml
spring:
  modulith:
    events:
      jdbc:
        # 自动创建表
        schema-initialization:
          enabled: true
      
      # 事件完成模式（1.3+）
      # UPDATE: 标记完成日期，保留记录（默认）
      # DELETE: 完成后直接删除记录
      # ARCHIVE: 归档到archive表
      completion-mode: UPDATE
```

**注意**：Spring Modulith **不提供** `completion-retention`、`cleanup`、`retry` 等 YAML 配置。
这些功能需要通过代码自定义实现（见下方“自定义实现”章节）。

---

### 3. **配置参数详解**

| 参数 | 默认值 | 说明 | 推荐值 |
|------|--------|------|--------|
| `schema-initialization.enabled` | false | 是否自动创建表 | true |
| `completion-retention` | 7d | 已完成事件保留时间 | 7d-30d |
| `cleanup.enabled` | true | 是否启用清理 | true |
| `cleanup.cron` | 0 0 2 * * ? | 清理任务执行时间 | 低峰期 |
| `retry.max-attempts` | 5 | 最大重试次数 | 3-10 |
| `retry.initial-delay` | 5s | 首次重试延迟 | 5s-30s |
| `retry.max-delay` | 1m | 最大重试延迟 | 1m-5m |
| `retry.multiplier` | 2.0 | 退避倍数 | 1.5-2.0 |

---

## 📊 数据库表结构

### EVENT_PUBLICATION 表

启动时自动创建：

```sql
CREATE TABLE EVENT_PUBLICATION (
    id UUID PRIMARY KEY,              -- 事件唯一ID
    listener_id VARCHAR(255),         -- 监听器标识
    event_type VARCHAR(255),          -- 事件类型（全限定类名）
    serialized_event TEXT,            -- 序列化的事件数据（JSON）
    publication_date TIMESTAMP,       -- 发布时间
    completion_date TIMESTAMP,        -- 完成时间（NULL=未完成）
    status VARCHAR(50)                -- 状态（PENDING/COMPLETED/FAILED）
);

-- 常用索引
CREATE INDEX idx_completion_date ON EVENT_PUBLICATION(completion_date);
CREATE INDEX idx_publication_date ON EVENT_PUBLICATION(publication_date);
CREATE INDEX idx_event_type ON EVENT_PUBLICATION(event_type);
```

---

## 💻 使用示例

### 1. **发布事件**

```java
@Service
public class OrderService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public Order createOrder(CustomerId customerId, Set<OrderItem> items) {
        // 1. 创建订单
        Order order = new Order(customerId, items);
        orderRepository.save(order);
        
        // 2. 发布事件（自动持久化到数据库）
        eventPublisher.publishEvent(new OrderCreatedEvent(
            order.getId(),
            customerId,
            order.getTotalAmount()
        ));
        
        return order;
    }
}
```

### 2. **监听事件**

```java
@Component
@Slf4j
public class OrderEventHandler {
    
    /**
     * 事务性事件监听器
     * 在事务提交后触发
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("处理订单创建事件: {}", event.orderId());
        
        // 业务逻辑
        // 1. 预留库存
        // 2. 发送通知
        // 3. 记录日志
        
        // 如果抛出异常，事件会标记为 FAILED 并自动重试
    }
}
```

### 3. **查询事件状态**

#### 方式 1：通过 Actuator

```bash
curl http://localhost:8080/actuator/modulith | jq '.eventPublications'
```

返回：
```json
{
  "pending": 5,      // 待处理
  "completed": 100,  // 已完成
  "failed": 2        // 失败
}
```

#### 方式 2：直接查询数据库

```sql
-- 查询待处理事件
SELECT * FROM EVENT_PUBLICATION 
WHERE completion_date IS NULL;

-- 查询失败事件
SELECT * FROM EVENT_PUBLICATION 
WHERE status = 'FAILED';

-- 统计各状态事件数量
SELECT 
    status,
    COUNT(*) as count
FROM EVENT_PUBLICATION
GROUP BY status;

-- 查询超时事件（超过1小时未处理）
SELECT * FROM EVENT_PUBLICATION
WHERE completion_date IS NULL
AND publication_date < NOW() - INTERVAL '1 hour';
```

---

## 🔄 事件生命周期

```
1. 发布事件
   ↓
2. 💾 保存到 EVENT_PUBLICATION 表（status=PENDING）
   ↓
3. 🔄 异步处理事件
   ├─ 成功 → 标记为 COMPLETED（设置 completion_date）
   └─ 失败 → 标记为 FAILED，等待重试
       ↓
   4. 重试机制（指数退避）
       ├─ 第1次重试：5s 后
       ├─ 第2次重试：10s 后
       ├─ 第3次重试：20s 后
       ├─ 第4次重试：40s 后
       └─ 第5次重试：1m 后
           ↓
       仍失败 → 保持 FAILED 状态
   ↓
5. 🗑️ 定期清理（每天凌晨2点）
   - 删除 completed_date < (NOW - 7天) 的记录
```

---

## 🛠️ 高级用法

### 1. **手动重放失败事件**

```java
@Service
public class EventReplayService {
    
    private final JdbcTemplate jdbcTemplate;
    
    public void replayFailedEvents() {
        // 查询失败事件
        String sql = "SELECT * FROM EVENT_PUBLICATION WHERE status = 'FAILED'";
        
        List<Map<String, Object>> failedEvents = jdbcTemplate.queryForList(sql);
        
        failedEvents.forEach(event -> {
            try {
                // 反序列化事件
                String eventType = (String) event.get("event_type");
                String serializedEvent = (String) event.get("serialized_event");
                
                // 重新发布事件
                // ... 根据事件类型重新处理
                
                log.info("重放事件成功: {}", event.get("id"));
            } catch (Exception e) {
                log.error("重放事件失败: {}", event.get("id"), e);
            }
        });
    }
}
```

### 2. **自定义事件过滤器**

```java
@Configuration
public class EventFilterConfig {
    
    @Bean
    public EventPublicationFilter eventPublicationFilter() {
        return publication -> {
            // 只持久化特定类型的事件
            return publication.getEventType().contains("Order");
        };
    }
}
```

### 3. **事件监控服务**

参考 [EventMonitoringService.java](file:///D:/code/ax-springboot-demo/ax-springboot3-demo/b43-modulith/b43-modulith-app/src/main/java/com/axinger/modulith/service/EventMonitoringService.java)

```java
@Service
public class EventMonitoringService {
    
    private final JdbcTemplate jdbcTemplate;
    
    // 每5分钟检查未完成事件
    @Scheduled(fixedRate = 300000)
    public void checkIncompleteEvents() {
        String sql = "SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        
        if (count > 0) {
            log.warn("发现 {} 个未完成的事件", count);
        }
    }
}
```

### 4. **自定义事件清理**

由于 Spring Modulith 不提供自动清理配置，需要手动实现：

#### 方案 1：使用 DELETE 模式（推荐）

```yaml
spring:
  modulith:
    events:
      completion-mode: DELETE  # 完成后直接删除记录
```

**优势**：
- ✅ 无需编写清理代码
- ✅ 自动管理存储空间
- ✅ 简单可靠

**缺点**：
- ❌ 无法查询已完成事件历史

---

#### 方案 2：定时任务清理

```java
@Service
@Slf4j
public class EventCleanupService {
    
    private final JdbcTemplate jdbcTemplate;
    
    /**
     * 每天凌晨2点清理7天前的已完成事件
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupCompletedEvents() {
        try {
            String sql = "DELETE FROM EVENT_PUBLICATION " +
                        "WHERE completion_date IS NOT NULL " +
                        "AND completion_date < ?";
            
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            int deletedCount = jdbcTemplate.update(sql, sevenDaysAgo);
            
            log.info("清理了 {} 条已完成的事件记录", deletedCount);
        } catch (Exception e) {
            log.error("清理事件失败", e);
        }
    }
}
```

**注意**：需要在主类添加 `@EnableScheduling` 启用定时任务。

---

#### 方案 3：使用 ARCHIVE 模式

```yaml
spring:
  modulith:
    events:
      completion-mode: ARCHIVE  # 归档到archive表
```

**优势**：
- ✅ 保留历史记录
- ✅ 主表保持干净
- ✅ 可查询归档数据

**缺点**：
- ⚠️ 需要额外存储空间

## 🐛 故障排查

### Q1: 表未自动创建？

**检查**：
```yaml
spring.modulith.events.jdbc.schema-initialization.enabled: true
```

**手动创建**：
```sql
-- 查看建表 SQL
-- Spring Modulith 启动时会输出建表语句
-- 或参考官方文档手动执行
```

### Q2: 事件未持久化？

**检查清单**：
- [ ] 依赖是否正确引入
- [ ] 配置是否启用
- [ ] 数据库连接是否正常
- [ ] 查看日志是否有错误

**调试日志**：
```yaml
logging:
  level:
    org.springframework.modulith.events: DEBUG
```

### Q3: 事件处理失败？

**查看失败事件**：
```sql
SELECT * FROM EVENT_PUBLICATION 
WHERE status = 'FAILED' 
ORDER BY publication_date DESC 
LIMIT 10;
```

**查看应用日志**：
```bash
grep "ERROR" logs/application.log | grep "Event"
```

### Q4: 事件积压过多？

**检查**：
```sql
-- 待处理事件数
SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL;

-- 按事件类型统计
SELECT event_type, COUNT(*) as count
FROM EVENT_PUBLICATION
WHERE completion_date IS NULL
GROUP BY event_type;
```

**解决**：
- 增加线程池大小
- 优化事件处理器性能
- 检查是否有死锁或慢查询

---

## 📈 性能优化

### 1. **数据库索引**

```sql
-- 添加索引提高查询性能
CREATE INDEX idx_completion_status ON EVENT_PUBLICATION(completion_date, status);
CREATE INDEX idx_publication_status ON EVENT_PUBLICATION(publication_date, status);
```

### 2. **批量清理**

```yaml
spring:
  modulith:
    events:
      jdbc:
        cleanup:
          batch-size: 1000  # 每次清理1000条
```

### 3. **连接池优化**

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

---

## 🎯 最佳实践

### 1. **事件设计原则**

✅ **推荐**：
```java
// 不可变记录
public record OrderCreatedEvent(
    OrderId orderId,
    CustomerId customerId,
    double amount,
    LocalDateTime createdAt
) {}
```

❌ **避免**：
```java
// 可变对象
public class OrderCreatedEvent {
    private OrderId orderId;  // 可能被修改
    // ...
}
```

### 2. **幂等性保证**

```java
@TransactionalEventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    // 检查是否已处理
    if (alreadyProcessed(event.orderId())) {
        return;  // 跳过重复处理
    }
    
    // 处理业务逻辑
    processOrder(event);
}
```

### 3. **异常处理**

```java
@TransactionalEventListener
public void handleOrderCreated(OrderCreatedEvent event) {
    try {
        processOrder(event);
    } catch (BusinessException e) {
        // 业务异常：记录日志，不重试
        log.error("业务处理失败: {}", e.getMessage());
        throw e;  // 触发重试
    } catch (Exception e) {
        // 系统异常：记录日志，重试
        log.error("系统异常，将重试", e);
        throw e;
    }
}
```

### 4. **监控告警**

```yaml
# Prometheus 指标
management:
  metrics:
    export:
      prometheus:
        enabled: true

# 告警规则（Prometheus）
groups:
  - name: modulith-alerts
    rules:
      - alert: HighPendingEvents
        expr: modulith_events_pending > 100
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "待处理事件过多"
```

---

## 📝 总结

### 优势

✅ **可靠性**：事件不丢失，支持重试  
✅ **简单性**：无需 Kafka/RabbitMQ  
✅ **可观测**：Actuator + 数据库查询  
✅ **易维护**：标准 SQL，易于调试  

### 适用场景

- ✅ 中小型应用（QPS < 10000）
- ✅ 单体或模块化单体架构
- ✅ 团队规模适中
- ✅ 希望简化运维

### 不适用场景

- ❌ 超大规模分布式系统
- ❌ 需要跨数据中心通信
- ❌ 已有成熟的 MQ 基础设施

---

## 🔗 相关资源

- [Spring Modulith 官方文档](https://spring.io/projects/spring-modulith)
- [PRODUCTION_SETUP.md](PRODUCTION_SETUP.md) - 生产环境配置
- [DATABASE_PERSISTENCE.md](DATABASE_PERSISTENCE.md) - 数据库持久化方案

---

**祝您的事件处理稳定可靠！** 🎉

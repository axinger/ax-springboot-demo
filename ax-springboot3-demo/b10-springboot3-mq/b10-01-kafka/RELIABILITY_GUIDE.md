# Kafka 消息发送可靠性保证指南

## 目录
- [1. Kafka 内置可靠性机制](#1-kafka-内置可靠性机制)
- [2. 应用层可靠性策略](#2-应用层可靠性策略)
- [3. 失败处理方案对比](#3-失败处理方案对比)
- [4. 最佳实践建议](#4-最佳实践建议)

---

## 1. Kafka 内置可靠性机制

### 1.1 acks 配置（最关键）

acks 参数决定了 Producer 需要等待多少个副本确认才算发送成功：

| 值 | 说明 | 性能 | 可靠性 | 适用场景 |
|----|------|------|--------|----------|
| `0` | 不等待任何确认 | 最快 | 最低，可能丢消息 | 日志收集等可容忍丢失的场景 |
| `1` | 只等待 leader 确认 | 较快 | 中等，leader 故障会丢消息 | 一般业务场景 |
| `all` / `-1` | 等待所有 ISR 副本确认 | 最慢 | 最高，不会丢消息 | 金融、订单等重要业务 |

**配置示例：**
```yaml
spring:
  kafka:
    producer:
      acks: all  # 最可靠
```

### 1.2 重试机制

Kafka Producer 内置了自动重试功能：

```yaml
spring:
  kafka:
    producer:
      retries: 3              # 重试次数
      retry-backoff-ms: 1000  # 重试间隔（毫秒）
```

**注意：**
- 重试只会处理临时性错误（如网络抖动、leader 选举）
- 对于永久性错误（如序列化失败、Topic 不存在）不会重试

### 1.3 幂等性 Producer

启用幂等性可以防止因重试导致的消息重复：

```yaml
spring:
  kafka:
    producer:
      properties:
        enable.idempotence: true  # 启用幂等性
```

**要求：**
- `acks` 必须为 `all`
- `retries` 必须大于 0
- `max.in.flight.requests.per.connection` 必须为 1（Spring Kafka 会自动设置）

**效果：**
- Producer 会为每条消息分配唯一的 PID（Producer ID）和序列号
- Broker 会检测并拒绝重复的消息

### 1.4 事务支持（最高级别）

对于需要跨多个 Topic 或分区的原子性操作，可以使用事务：

```java
@Autowired
private KafkaTemplate<String, MessageUserDTO> kafkaTemplate;

public void sendWithTransaction() {
    kafkaTemplate.executeInTransaction(operations -> {
        operations.send(Topic.USER_JSON, user1);
        operations.send(Topic.ORDER_TOPIC, order);
        return true; // 提交事务
    });
}
```

---

## 2. 应用层可靠性策略

### 2.1 同步发送（强一致性）

**优点：**
- 立即知道发送结果
- 适合对可靠性要求极高的场景

**缺点：**
- 性能较差，吞吐量低
- 阻塞当前线程

**示例代码：**
```java
@GetMapping("/test1-sync")
String test1Sync(String groupId) {
    try {
        // 同步等待结果，最多等待10秒
        SendResult<String, MessageUserDTO> result = kafkaTemplate.send(record)
                .get(10, TimeUnit.SECONDS);
        
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("发送成功: topic={}, partition={}, offset={}",
                metadata.topic(), metadata.partition(), metadata.offset());
        
        return "发送成功";
        
    } catch (Exception e) {
        log.error("发送失败", e);
        // 执行失败处理逻辑
        handleSendFailure(user, e);
        return "发送失败: " + e.getMessage();
    }
}
```

### 2.2 异步回调（推荐）

**优点：**
- 非阻塞，性能好
- 可以处理发送结果

**缺点：**
- 需要妥善处理回调中的异常

**示例代码：**
```java
kafkaTemplate.send(record).whenComplete((result, ex) -> {
    if (ex == null) {
        // 发送成功
        log.info("发送成功: offset={}", result.getRecordMetadata().offset());
    } else {
        // 发送失败
        log.error("发送失败", ex);
        handleSendFailure(user, ex);
    }
});
```

### 2.3 本地消息表（最终一致性）

**适用场景：** 分布式事务、重要业务消息

**实现步骤：**

1. **创建本地消息表**
```sql
CREATE TABLE local_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message_id VARCHAR(64) UNIQUE,
    topic VARCHAR(128),
    content TEXT,
    status TINYINT DEFAULT 0,  -- 0:待发送, 1:已发送, 2:发送失败
    retry_count INT DEFAULT 0,
    error_msg TEXT,
    create_time DATETIME,
    update_time DATETIME
);
```

2. **业务代码**
```java
@Transactional
public void sendMessage(MessageUserDTO user) {
    // 1. 保存业务数据
    businessService.save(user);
    
    // 2. 保存消息到本地表
    LocalMessage message = new LocalMessage();
    message.setMessageId(IdUtil.fastSimpleUUID());
    message.setTopic(Topic.USER_DTO);
    message.setContent(JsonUtils.toJson(user));
    message.setStatus(0); // 待发送
    localMessageRepository.save(message);
}
```

3. **定时任务重试**
```java
@Scheduled(fixedDelay = 5000)  // 每5秒执行
public void retryFailedMessages() {
    List<LocalMessage> pendingMessages = 
        localMessageRepository.findByStatusAndRetryCountLessThan(0, 3);
    
    for (LocalMessage msg : pendingMessages) {
        try {
            kafkaTemplate.send(msg.getTopic(), msg.getContent())
                .get(5, TimeUnit.SECONDS);
            
            // 更新状态为已发送
            msg.setStatus(1);
            localMessageRepository.save(msg);
            
        } catch (Exception e) {
            // 更新重试次数和错误信息
            msg.setRetryCount(msg.getRetryCount() + 1);
            msg.setErrorMsg(e.getMessage());
            msg.setStatus(2); // 发送失败
            localMessageRepository.save(msg);
        }
    }
}
```

### 2.4 死信队列（DLQ）

**适用场景：** 无法立即处理的消息

**实现方式：**

```java
private void handleSendFailure(MessageUserDTO user, Throwable exception) {
    // 发送到死信 Topic
    String deadLetterTopic = Topic.USER_DTO + ".DLT";
    kafkaTemplate.send(deadLetterTopic, user)
        .whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("死信队列发送也失败", ex);
                // 记录到数据库，人工干预
            }
        });
}
```

**消费者配置：**
```yaml
spring:
  kafka:
    listener:
      dead-letter-topic: USER_DTO.DLT
```

---

## 3. 失败处理方案对比

| 方案 | 可靠性 | 复杂度 | 性能影响 | 适用场景 |
|------|--------|--------|----------|----------|
| **仅靠 Kafka 重试** | 中 | 低 | 小 | 临时性故障，非关键业务 |
| **同步发送** | 高 | 低 | 大 | 低频、高可靠性要求的场景 |
| **异步回调 + 日志** | 中 | 低 | 小 | 一般业务，可接受少量丢失 |
| **死信队列** | 高 | 中 | 小 | 需要后续处理的失败消息 |
| **本地消息表** | 最高 | 高 | 中 | 金融、订单等核心业务 |
| **事务消息** | 最高 | 中 | 中 | 需要原子性的多消息发送 |

---

## 4. 最佳实践建议

### 4.1 配置建议

```yaml
spring:
  kafka:
    producer:
      # 可靠性配置
      acks: all                    # 等待所有副本确认
      retries: 3                   # 重试3次
      properties:
        enable.idempotence: true   # 启用幂等性
        request.timeout.ms: 30000  # 请求超时30秒
        max.block.ms: 30000        # 最大阻塞30秒
      
      # 性能优化
      batch-size: 16384            # 批次大小16KB
      buffer-memory: 33554432      # 缓冲区32MB
      linger.ms: 10                # 等待10ms凑批
```

### 4.2 代码建议

1. **始终处理回调异常**
```java
kafkaTemplate.send(record).whenComplete((result, ex) -> {
    if (ex != null) {
        // 必须有失败处理逻辑
        handleFailure(ex);
    }
});
```

2. **记录关键信息**
```java
log.error("消息发送失败 - messageId={}, topic={}, error={}",
    messageId, topic, ex.getMessage(), ex);
```

3. **设置合理的超时时间**
```java
// 不要无限等待
sendResult.get(10, TimeUnit.SECONDS);
```

### 4.3 监控建议

1. **监控指标**
   - 发送成功率
   - 平均延迟
   - 重试次数
   - 死信队列积压量

2. **告警规则**
   - 发送失败率 > 1%
   - 死信队列消息数 > 100
   - 平均延迟 > 1秒

### 4.4 根据业务选择策略

| 业务类型 | 推荐方案 | 配置 |
|---------|---------|------|
| **日志收集** | 异步 + 少量重试 | acks=1, retries=1 |
| **用户行为追踪** | 异步回调 | acks=1, retries=2 |
| **订单通知** | 本地消息表 | acks=all, 幂等性=true |
| **支付结果** | 事务 + 本地消息表 | acks=all, 事务 enabled |
| **实时风控** | 同步发送 | acks=all, timeout=5s |

---

## 5. 常见问题 FAQ

### Q1: 消息发送失败后会自动重试吗？
**A:** 会的，但只重试临时性错误。需要在配置中设置 `retries > 0`。

### Q2: 如何保证消息不丢失？
**A:** 
1. Producer: `acks=all` + `enable.idempotence=true`
2. Broker: `replication.factor >= 3`, `min.insync.replicas >= 2`
3. Consumer: 手动提交偏移量，处理完再提交

### Q3: 如何保证消息不重复？
**A:** 
1. Producer: 启用幂等性 `enable.idempotence=true`
2. Consumer: 实现幂等消费（通过唯一ID去重）

### Q4: 同步发送和异步发送如何选择？
**A:** 
- 高频场景：用异步，性能好
- 低频高可靠：用同步，立即知道结果
- 混合使用：关键业务同步，非关键异步

### Q5: 本地消息表会影响性能吗？
**A:** 会有一定影响，因为多了数据库操作。但对于重要业务，这是值得的。可以通过以下方式优化：
- 批量插入消息记录
- 异步发送消息
- 定期清理已发送的消息

---

## 6. 总结

**Kafka 本身提供的可靠性：**
- ✅ acks 机制
- ✅ 自动重试
- ✅ 幂等性 Producer
- ✅ 事务支持

**需要应用层补充的：**
- ⚠️ 失败后的业务处理（本地消息表、死信队列）
- ⚠️ 监控和告警
- ⚠️ 幂等消费
- ⚠️ 消息追溯能力

**最佳实践：**
1. 根据业务重要性选择合适的可靠性级别
2. 始终处理发送失败的回调
3. 重要业务使用本地消息表保证最终一致性
4. 建立完善的监控和告警机制
5. 定期进行故障演练

记住：**没有银弹**，需要根据具体业务场景权衡可靠性、性能和复杂度！

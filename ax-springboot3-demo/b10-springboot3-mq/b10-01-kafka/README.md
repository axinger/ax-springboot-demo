# Spring Boot 3 Kafka 示例项目

## 项目概述

本项目演示了 Spring Boot 3 与 Apache Kafka 的集成，包括消息生产、消费以及消费者组的使用。

### 模块结构

```
a10-springboot3-01-kafka/
├── b10-01-kafka-api/          # API 模块（共享模型和常量）
├── b10-01-kafka-producer/     # 消息生产者（端口: 11013）
├── b10-01-kafka-consumer/     # 消息消费者 Group1（端口: 11011）
└── b10-01-kafka-consumer2/    # 消息消费者 Group2（端口: 11012）
```

## 快速开始

### 前置条件

1. 安装并启动 Kafka（推荐使用 Docker）
```bash
docker run -d --name kafka \
  -p 9092:9092 \
  -e KAFKA_CFG_NODE_ID=0 \
  -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@localhost:9093 \
  -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  bitnami/kafka:latest
```

2. 创建必要的 Topic
```bash
# 进入 Kafka 容器
docker exec -it kafka bash

# 创建 Topic
kafka-topics.sh --create --topic USER_DTO-v2 --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
kafka-topics.sh --create --topic user-json --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
kafka-topics.sh --create --topic simple --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### 启动应用

1. **启动 Producer**
```bash
cd b10-01-kafka-producer
mvn spring-boot:run
```
访问: http://localhost:11013

2. **启动 Consumer Group1**
```bash
cd b10-01-kafka-consumer
mvn spring-boot:run
```

3. **启动 Consumer Group2**
```bash
cd b10-01-kafka-consumer2
mvn spring-boot:run
```

## API 接口说明

### Producer 接口

#### 1. 发送带 Header 的消息
```
GET /test1?groupId=my_group_id1
```
- 功能：发送包含自定义 Header 的消息到 `USER_DTO-v2` 主题
- 参数：`groupId` - 目标消费者组标识
- 返回：异步回调结果

#### 2. 发送普通 JSON 消息
```
GET /test2
```
- 功能：发送普通 JSON 格式的用户消息到 `user-json` 主题
- 返回：异步回调结果

## 消费者组行为说明

### 配置差异

| 项目 | Consumer (Group1) | Consumer2 (Group2) |
|------|-------------------|--------------------|
| 端口 | 11011 | 11012 |
| Group ID | my_group_id1 | my_group_id2 |
| 并发数 | 3 | 3 |
| 确认模式 | manual_immediate | manual_immediate |

### 行为特点

当两个消费者订阅同一主题但使用不同的 `group-id` 时：

1. **独立消费**：每个消费者组都会独立地消费主题中的所有消息
2. **独立偏移量管理**：每个消费者组维护自己的消费偏移量
3. **消息重复消费**：不同消费者组会各自消费相同的消息

### 实际应用场景

- **不同业务处理**：不同服务需要对相同消息进行不同的处理
- **数据备份**：多个服务需要备份相同的数据
- **A/B测试**：不同版本的服务处理相同的消息流
- **实时分析与归档**：一个消费者组用于实时处理，另一个用于数据归档

## 技术要点

### 1. 手动确认模式

所有消费者都配置为手动确认模式（`ack-mode: manual_immediate`），需要显式调用：
```java
ack.acknowledge();  // 确认消息
consumer.commitAsync(...);  // 异步提交偏移量
```

### 2. 消息序列化

- **Producer**：使用 `JsonSerializer` 序列化对象
- **Consumer**：使用 `JsonDeserializer` 反序列化对象
- 配置信任包：`spring.json.trusted.packages: 'com.github.axinger.api.model'`

### 3. CompletableFuture 回调

Spring Kafka 3.x 使用 `CompletableFuture` 替代了已废弃的 `ListenableFuture`：
```java
CompletableFuture<SendResult<String, MessageUserDTO>> future = kafkaTemplate.send(record);
future.whenComplete((result, ex) -> {
    if (ex == null) {
        // 发送成功
    } else {
        // 发送失败
    }
});
```

### 4. Headers 处理

可以在消息中添加自定义 Header：
```java
ProducerRecord<String, MessageUserDTO> record = new ProducerRecord<>(Topic.USER_DTO, user);
record.headers().add("target-consumer", groupId.getBytes());
```

## 注意事项

1. **偏移量管理**：手动确认模式下，务必在业务处理成功后再调用 `ack.acknowledge()`
2. **异常处理**：建议在 catch 块中记录失败消息，便于后续重试或人工处理
3. **消费者组**：相同 group-id 的消费者实例会竞争消费，不同 group-id 会独立消费
4. **JSON 安全**：生产环境应限制 `trusted.packages` 为具体的包路径，避免使用 `*`

## 常见问题

### Q1: 消息没有被消费？
- 检查 Kafka 是否正常运行
- 确认 Topic 已创建
- 检查消费者组 ID 配置
- 查看应用日志是否有错误

### Q2: 消息重复消费？
- 这是正常现象，不同消费者组会独立消费
- 如果需要幂等性，需在业务层实现去重逻辑

### Q3: 如何查看消费进度？
```bash
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group my_group_id1
```

## 扩展阅读

- [Spring Kafka 官方文档](https://docs.spring.io/spring-kafka/reference/)
- [Apache Kafka 官方文档](https://kafka.apache.org/documentation/)
- [Kafka 消费者组概念](https://kafka.apache.org/intro#intro_consumers)

# Redisson Spring Boot Demo

基于 `redisson-spring-boot-starter` 的综合示例项目，覆盖**消息、分布式锁、定时任务、分布式数据结构**四大场景。

---

## 技术栈

| 组件 | 说明 |
|------|------|
| Spring Boot 2.7.x | 基础框架 |
| redisson-spring-boot-starter | Redisson 自动配置 |
| Redis 2.0+ / 5.0+ / 7.0+ | 不同特性对应不同 Redis 版本 |
| Swagger3 | API 文档（`/swagger-ui.html`） |

> **注意**：Redisson **没有内置消息注解**（不像 `@KafkaListener`），所有 API 都是编程式调用。

---

## 项目结构

```
src/main/java/com/github/axinger/
│
├── controller/
│   ├── MessageController        — 消息: 普通消息 + 延迟消息 + Stream
│   ├── LockController           — 锁: 可重入锁/读写锁/信号量/秒杀
│   ├── ScheduledController      — 定时任务: RScheduledExecutorService
│   └── DataStructureController  — 数据结构: 14 种分布式对象（新特性）
│
├── service/
│   ├── MessageService           — 消息服务（纯 Redisson 编程 API）
│   ├── LockService              — 分布式锁 + 秒杀
│   ├── DataStructureService     — 分布式数据结构演示
│   └── scheduled/
│       └── RedisScheduledTaskService  — 定时任务服务
│
├── model/
│   ├── MessageBody              — 消息体（支持延迟投递时间）
│   └── Student                  — 演示自定义对象序列化
│
└── resources/
    ├── application.yml          — Spring Boot 配置
    └── redisson.yml             — Redisson 客户端配置
```

11 个源文件，按业务场景归类，结构扁平。

---

## 一、消息系统

`MessageService` + `MessageController` — 3 种消息模式：

| 模式 | API | Redis 版本 | 持久化 | 适用场景 |
|------|-----|-----------|--------|---------|
| **普通消息** | `RTopic.addListener()` | 2.0+ | ❌ | 实时通知 |
| **延迟消息** | `RDelayedQueue.offer()` | 2.0+ | ⚠️ ZSet | 订单超时取消 |
| **流式消息** | `RStream.readGroup()` | 5.0+ | ✅ | 可靠消息、消费者组 |

### 1.1 普通消息（Pub/Sub）

```java
// 订阅 — 应用启动时自动注册
redissonClient.getTopic("demo:topic:channel")
    .addListener(MessageBody.class, (ch, msg) -> log.info("收到: {}", msg));

// 发布
redissonClient.getTopic("demo:topic:channel").publish(MessageBody.of("Hello"));
```

还支持 `RShardedTopic`（Redis 7.0+, 分片避免广播风暴）和 `RPatternTopic`（模式匹配 `demo:*`）。

### 1.2 延迟消息（RDelayedQueue）

```java
// 发送 — ZSet 实现，score = 到期时间戳
delayedQueue.offer(MessageBody.of("延迟消息"), 10, TimeUnit.SECONDS);

// 消费 — 守护线程阻塞等待
MessageBody msg = blockingQueue.take();  // 到期后自动投递
```

### 1.3 流式消息（RStream）

```java
// 写入 — 消息持久化
stream.add(StreamAddArgs.entry(msg.getId(), msg));

// 消费 — 消费者组 + ACK 确认
stream.readGroup("group", "consumer-1",
    StreamReadGroupArgs.greaterThan(StreamMessageId.NEVER_DELIVERED));
stream.ack("group", msgId);
```

### API 端点（`/message`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/message/topic/send` | 发送普通消息 |
| GET | `/message/sharded/send` | 发送分片消息 (Redis 7.0+) |
| GET | `/message/delayed/send?delay=5` | 发送延迟消息 |
| GET | `/message/stream/send` | 发送 Stream 消息 |
| GET | `/message/result` | 查看全部消息 |
| DELETE | `/message/clear` | 清空 |
| GET | `/message/overview` | 消息模式对比 |

---

## 二、分布式锁

`LockService` + `LockController` — 4 种锁模式：

| 锁类型 | API | 特点 |
|--------|-----|------|
| **可重入锁** | `RLock.lock()` / `tryLock()` | 看门狗自动续期 30s |
| **读写锁** | `RReadWriteLock` | 读+读并发, 写互斥 |
| **信号量** | `RSemaphore` | 限流/连接池 |
| **秒杀** | `RLock` + Redis 库存 | 高并发抢购实战 |

```java
// 可重入锁 — 看门狗自动续期
RLock lock = redissonClient.getLock("key");
lock.lock();
try { /* 业务 */ } finally { lock.unlock(); }

// tryLock — 指定等待和持有时间
if (lock.tryLock(3, 10, TimeUnit.SECONDS)) { ... }

// 读写锁
RReadWriteLock rw = redissonClient.getReadWriteLock("rw");
rw.writeLock().lock();  // 写锁
rw.readLock().lock();   // 读锁（可并发）

// 信号量
RSemaphore semaphore = redissonClient.getSemaphore("park");
semaphore.tryAcquire();  // 非阻塞获取
semaphore.release();     // 释放
```

### API 端点（`/lock`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/lock/lock` | 阻塞加锁（看门狗） |
| GET | `/lock/tryLock` | 尝试加锁（超时返回） |
| GET | `/lock/write` | 写锁 |
| GET | `/lock/read` | 读锁 |
| GET | `/lock/semaphore/init` | 初始化信号量 |
| GET | `/lock/semaphore/acquire` | 获取信号量 |
| GET | `/lock/semaphore/release` | 释放信号量 |
| GET | `/lock/secKill/init` | 初始化秒杀库存 |
| GET | `/lock/secKill` | 单次秒杀 |
| GET | `/lock/secKill/concurrent` | 多线程压测 |
| GET | `/lock/overview` | 锁系统概览 |

---

## 三、分布式定时任务

`RedisScheduledTaskService` + `ScheduledController` — 3 种模式：

| 模式 | API | 示例 |
|------|-----|------|
| **ONE_SHOT** | `schedule(task, delay, unit)` | 5s 后执行一次 |
| **FIXED_RATE** | `scheduleAtFixedRate(task, delay, period, unit)` | 每 3s 执行一次 |
| **CRON** | `schedule(task, CronSchedule.of("..."))` | 每 10 秒 |

### 与 @Scheduled 对比

| | @Scheduled | RScheduledExecutorService |
|--|-----------|--------------------------|
| 分布式 | ❌ 多实例重复执行 | ✅ 集群中仅一个节点执行 |
| 故障转移 | ❌ | ✅ 自动迁移 |
| 动态管理 | ❌ | ✅ 运行时新增/取消 |
| 持久化 | ❌ | ✅ Redis 存储 |

```java
RScheduledExecutorService executor = redissonClient.getExecutorService("demo");
executor.registerWorkers(WorkerOptions.defaults().workers(4));

// 一次性
executor.schedule(() -> doWork(), 5, TimeUnit.SECONDS);
// 固定频率
executor.scheduleAtFixedRate(() -> doWork(), 0, 3, TimeUnit.SECONDS);
// CRON
executor.schedule(() -> doWork(), CronSchedule.of("0/10 * * * * ?"));
```

### API 端点（`/scheduled`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/scheduled/one-shot` | 一次性延迟任务 |
| GET | `/scheduled/fixed-rate` | 固定频率 |
| GET | `/scheduled/fixed-rate/delayed` | 固定频率（延迟启动） |
| GET | `/scheduled/cron` | CRON 定时 |
| GET | `/scheduled/cron/examples` | Cron 表达式参考 |
| GET | `/scheduled/results` | 执行记录 |
| DELETE | `/scheduled/cancel` | 取消任务 |
| DELETE | `/scheduled/cancel-all` | 取消所有 |
| GET | `/scheduled/overview` | 概览 |

---

## 四、分布式数据结构（新特性）

`DataStructureService` + `DataStructureController` — 14 种分布式对象：

| 类型 | 场景 | 类比 |
|------|------|------|
| `RBucket` | 简单键值 | Redis String |
| `RAtomicLong` | 分布式计数器/ID 生成 | AtomicLong |
| `RMap` | 分布式 HashMap | Redis Hash |
| `RList` | 分布式列表 | Redis List |
| `RSet` | 去重集合 | Redis Set |
| `RScoredSortedSet` | 排行榜 | Redis ZSet |
| `RQueue` | FIFO 队列 | Redis List |
| `RBloomFilter` | 缓存穿透防护 | Guava BloomFilter |
| `RRateLimiter` | 接口限流 | Guava RateLimiter |
| `RCountDownLatch` | 分布式协调 | CountDownLatch |
| `RBitSet` | 签到/在线状态 | Redis Bitmap |
| `RGeo` | 附近的人/门店 | Redis Geo |
| `RHyperLogLog` | UV 统计（12KB 存 10 万） | Redis HyperLogLog |
| `RLocalCachedMap` | 读多写少本地缓存 | Caffeine + Redis |

### API 端点（`/ds`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/ds/bucket` | 简单键值 |
| GET | `/ds/atomic-long` | 原子计数器 |
| GET | `/ds/map` | 分布式 Map |
| GET | `/ds/list` | 分布式 List |
| GET | `/ds/set` | 去重 Set |
| GET | `/ds/scored-sorted-set` | 排行榜 |
| GET | `/ds/queue` | FIFO 队列 |
| GET | `/ds/bloom-filter` | 布隆过滤器 |
| GET | `/ds/rate-limiter` | 限流器 |
| GET | `/ds/countdown/init` | 倒计时初始化 |
| GET | `/ds/countdown/count` | 倒计时 -1 |
| GET | `/ds/bitset` | 位图签到 |
| GET | `/ds/geo` | 地理位置 |
| GET | `/ds/hyper-log-log` | UV 统计 |
| GET | `/ds/local-cached-map` | 本地缓存 Map |
| GET | `/ds/overview` | 结构概览 |

---

## 五、配置

### application.yml

```yaml
server:
  port: 11014

spring:
  redis:
    host: 127.0.0.1
    port: 16379
    database: 12
    timeout: 5000
    redisson:
      file: classpath:redisson.yml
```

### redisson.yml

```yaml
singleServerConfig:
  address: redis://127.0.0.1:16379
  database: 12
  connectionPoolSize: 64
  connectionMinimumIdleSize: 32
codec:
  class: "org.redisson.codec.JsonJacksonCodec"
transportMode: "NIO"
lockWatchdogTimeout: 10000
```

---

## 六、API 速查表

| 场景 | Controller | 路径前缀 |
|------|-----------|---------|
| 消息 | MessageController | `/message` |
| 锁 | LockController | `/lock` |
| 定时任务 | ScheduledController | `/scheduled` |
| 数据结构 | DataStructureController | `/ds` |

所有 Controller 均有 `/overview` 端点提供概览说明。

---

## 七、常见问题

**Q: Redisson 有没有 `@KafkaListener` 那样的消息注解？**

没有。Redisson 只提供编程式 API，所有订阅都必须写 `addListener()`。如需注解驱动消息，用 **Spring Kafka / RabbitMQ / RocketMQ**。

**Q: RDelayedQueue 底层是什么？**

Redis Sorted Set (ZSet)，score 为到期时间戳。Redisson 后台线程定时扫描到期消息并移入目标队列。

**Q: 秒杀怎么保证不超卖？**

分布式锁 (`RLock`) 包裹"查库存→扣库存→记录用户"整体操作，保证原子性。

**Q: 定时任务多实例会重复执行吗？**

`RScheduledExecutorService` 不会 — 任务保存在 Redis，集群中仅一个节点执行。`@Scheduled` 会重复，需加分布式锁。

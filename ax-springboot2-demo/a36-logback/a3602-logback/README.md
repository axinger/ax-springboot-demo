# Logback 日志示例 - a3602-logback

本项目演示 Spring Boot 中 Logback 的完整配置，包括**通用日志**和**业务日志**的分离。

## 项目结构

```
src/main/
├── java/com/github/axinger/
│   ├── controller/
│   │   ├── DemoController.java       # MDC 日志演示
│   │   └── LogDemoController.java    # 通用日志 + 业务日志演示
│   ├── service/
│   │   ├── OrderService.java         # 订单业务日志示例
│   │   └── PaymentService.java       # 支付业务日志示例
│   └── config/
│       ├── MdcInterceptor.java       # MDC 拦截器（traceId/userId）
│       ├── WebMvcConfig.java         # Web 配置
│       └── GlobalExceptionHandler.java # 全局异常处理
└── resources/
    ├── logback-spring.xml            # 日志配置文件
    └── application.yml               # 应用配置
```

## 日志配置说明

### 1. 通用日志（按级别分离）

| Appender | 文件 | 级别 | 说明 |
|---------|------|------|------|
| CONSOLE | 控制台 | ALL | 带颜色的控制台输出 |
| DEBUG | debug.log | DEBUG | 仅 DEBUG 级别 |
| INFO | info.log | INFO | 仅 INFO 级别 |
| WARN | warn.log | WARN | 仅 WARN 级别 |
| ERROR | error.log | ERROR | 仅 ERROR 级别 |
| ALL | all.log | ALL | 所有级别（不过滤） |

**使用方式：**
```java
@Slf4j
@RestController
public class MyController {

    @GetMapping("/test")
    public void test() {
        log.debug("调试信息");  // 输出到 debug.log, all.log
        log.info("普通信息");   // 输出到 info.log, all.log, 控制台
        log.warn("警告信息");   // 输出到 warn.log, all.log, 控制台
        log.error("错误信息");  // 输出到 error.log, all.log, 控制台
    }
}
```

### 2. 业务日志（按业务分离）

| Logger | Appender | 文件 | 说明 |
|--------|----------|------|------|
| ORDER_LOG | ORDER_INFO + ORDER_ERROR | order_info.log / order_error.log | 订单业务日志 |
| PAYMENT_LOG | PAYMENT_INFO + PAYMENT_ERROR | payment_info.log / payment_error.log | 支付业务日志 |

**使用方式：**
```java
@Service
public class OrderService {
    // 通过指定 name 获取业务 logger
    private static final Logger orderLog = LoggerFactory.getLogger("ORDER_LOG");

    public void createOrder(String orderNo) {
        orderLog.info("订单创建 | orderNo={}", orderNo);  // 输出到 order_info.log

        try {
            // 业务处理...
        } catch (Exception e) {
            orderLog.error("订单失败 | orderNo={}", orderNo, e); // 输出到 order_error.log
        }
    }
}
```

## API 接口列表

### 通用日志测试
```bash
# 测试各级别日志输出
curl http://localhost:8080/log/common
```

### 订单业务日志测试
```bash
# 创建订单（正常流程）
curl -X POST "http://localhost:8080/log/order/create?orderNo=ORD2024001&productName=iPhone&amount=5999.00"

# 模拟订单异常（测试 error 日志）
curl -X POST "http://localhost:8080/log/order/error?orderNo=ORD2024002"
```

### 支付业务日志测试
```bash
# 处理支付
curl -X POST "http://localhost:8080/log/payment/pay?payNo=PAY2024001&orderNo=ORD2024001&amount=5999.00"
```

### 完整业务流程测试
```bash
# 订单 + 支付完整流程
curl -X POST "http://localhost:8080/log/full-process?orderNo=ORD2024003&productName=iPad&amount=3999.00"
```

### MDC 日志测试（带 traceId）
```bash
# 测试 MDC 链路追踪
curl http://localhost:8080/demo/test1

# 测试异常日志
curl http://localhost:8080/demo/error
```

## 日志文件位置

默认路径：`./logs/a3602-logback/`

启动后会在项目目录生成以下日志文件：
```
logs/
└── a3602-logback/
    ├── all.log              # 所有日志
    ├── debug.log            # DEBUG 级别日志
    ├── info.log             # INFO 级别日志
    ├── warn.log             # WARN 级别日志
    ├── error.log            # ERROR 级别日志
    ├── order_info.log       # 订单业务 INFO 日志
    ├── order_error.log      # 订单业务 ERROR 日志
    ├── payment_info.log     # 支付业务 INFO 日志
    └── payment_error.log    # 支付业务 ERROR 日志
```

## 日志格式说明

### 文件日志格式
```
%d{yyyy-MM-dd HH:mm:ss.SSS}  %-5level  ${PID} --- [%t] [%X{traceId}] [%X{userId}] %logger{100} : %msg%n

示例：
2025-05-10 19:17:19.686  INFO  2152 --- [http-nio-8080-exec-1] [abc123] [user01] c.g.a.c.LogDemoController : 订单创建成功
```

### 控制台日志格式（带颜色）
- 时间、PID、线程名、traceId、userId 等关键信息
- 不同级别显示不同颜色
- logger 名使用黄色高亮

### MDC 字段
- `traceId`：请求链路追踪 ID（每次请求唯一）
- `userId`：用户 ID（登录后设置）

## 配置项说明

### application.yml
```yaml
spring:
  application:
    name: a3602-logback  # 影响日志目录名

logging:
  file:
    path: ./logs         # 日志根目录
    maxHistory: 60       # 保留 60 天
    maxFileSize: 10MB    # 单个文件最大 10MB
    totalSizeCap: 100GB  # 总日志容量上限
```

### logback-spring.xml 关键配置

```xml
<!-- 1. 定义日志格式（带 MDC 字段） -->
<property name="log.pattern"
          value="%d{yyyy-MM-dd HH:mm:ss.SSS}  %-5level  ${PID:- } --- [%t] [%X{traceId:-}] [%X{userId:-}] %logger{100} : %msg%n"/>

<!-- 2. 通用日志 Appender（按级别过滤） -->
<appender name="INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <filter class="ch.qos.logback.classic.filter.LevelFilter">
        <level>info</level>
        <onMatch>ACCEPT</onMatch>
        <onMismatch>DENY</onMismatch>
    </filter>
    <!-- 滚动策略... -->
</appender>

<!-- 3. 业务日志 Appender -->
<appender name="ORDER_INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${log.dir}/order_info.log</file>
    <filter class="ch.qos.logback.classic.filter.LevelFilter">
        <level>info</level>
        <onMatch>ACCEPT</onMatch>
        <onMismatch>DENY</onMismatch>
    </filter>
</appender>

<!-- 4. 业务 Logger 定义 -->
<logger name="ORDER_LOG" level="info" additivity="false">
    <appender-ref ref="ORDER_ERROR"/>
    <appender-ref ref="ORDER_INFO"/>
</logger>
```

## 滚动策略

- **时间维度**：按天滚动，保留 60 天
- **大小维度**：单个文件 10MB 时滚动，添加 %i 序号
- **总容量**：所有日志最多占用 100GB

示例滚动文件名：
```
info.2025-03-29.0.log
info.2025-03-29.1.log
info.2025-03-30.0.log
```

## 使用建议

1. **通用日志**：用于系统日志、框架日志、调试信息
2. **业务日志**：用于关键业务流程记录，便于后续审计和分析
3. **MDC 字段**：在拦截器中设置 traceId，实现全链路追踪
4. **日志级别**：
   - DEBUG：开发调试使用
   - INFO：正常业务流程
   - WARN：可恢复的问题
   - ERROR：需要人工介入的错误

# Redis JSON 工具类 - 类型转换器扩展指南

## 概述

`RedisJsonUtil` 使用插件式的类型转换器机制，支持灵活扩展自定义类型转换。

## 架构设计

```
TypeConverter (接口)
    ↑
    ├── BuiltInConverters (内置转换器)
    │   ├── BooleanConverter
    │   ├── IntegerConverter
    │   ├── LocalDateTimeConverter
    │   ├── DateConverter
    │   └── ...
    │
    └── 自定义转换器
        ├── EnumConverter
        ├── URLConverter
        └── ...

TypeConverterRegistry (注册中心)
    ├── 管理所有转换器
    ├── 支持动态注册
    └── 按优先级查找

RedisJsonUtil
    └── 使用注册中心进行类型转换
```

## 内置支持的类型

### 基本类型
- `Boolean` / `boolean`
- `Integer` / `int`
- `Long` / `long`
- `Double` / `double`
- `Float` / `float`
- `Byte` / `byte`
- `Short` / `short`

### 大数类型
- `BigDecimal`
- `BigInteger`

### 日期时间类型
- `LocalDateTime` - ISO-8601: `2024-01-01T10:30:00`
- `LocalDate` - ISO-8601: `2024-01-01`
- `LocalTime` - ISO-8601: `10:30:00`
- `Date` - 时间戳(毫秒) 或 ISO-8601
- `Instant` - 时间戳(毫秒) 或 ISO-8601

## 使用方式

### 1. 基本使用

```java
@Service
@RequiredArgsConstructor
public class ExampleService {

    private final RedisJsonUtil redisJsonUtil;

    public void example() {
        // 自动使用内置转换器
        redisJsonUtil.set("count", 100);
        Integer count = redisJsonUtil.get("count", Integer.class);

        redisJsonUtil.set("price", new BigDecimal("99.99"));
        BigDecimal price = redisJsonUtil.get("price", BigDecimal.class);

        redisJsonUtil.set("timestamp", new Date());
        Date date = redisJsonUtil.get("timestamp", Date.class);
    }
}
```

### 2. 自定义转换器

#### 示例1：枚举类型转换器

```java
/**
 * 枚举类型转换器
 */
public class StatusEnumConverter implements TypeConverter<Status> {

    @Override
    public Status convert(String value) throws Exception {
        return Status.valueOf(value.toUpperCase());
    }

    @Override
    public Class<Status> supportType() {
        return Status.class;
    }

    @Override
    public int priority() {
        return 150;  // 自定义转换器建议使用 100+
    }
}

// 注册转换器
@Configuration
public class RedisConfig {

    @Bean
    public CommandLineRunner registerConverters(RedisJsonUtil redisJsonUtil) {
        return args -> {
            redisJsonUtil.registerConverter(new StatusEnumConverter());
        };
    }
}

// 使用
public enum Status {
    ACTIVE, INACTIVE, PENDING
}

redisJsonUtil.set("status", "ACTIVE");
Status status = redisJsonUtil.get("status", Status.class);  // ✅ 自动转换
```

#### 示例2：URL 类型转换器

```java
import java.net.URL;

public class URLConverter implements TypeConverter<URL> {

    @Override
    public URL convert(String value) throws Exception {
        return new URL(value);
    }

    @Override
    public Class<URL> supportType() {
        return URL.class;
    }

    @Override
    public int priority() {
        return 120;
    }
}

// 注册
redisJsonUtil.registerConverter(new URLConverter());

// 使用
redisJsonUtil.set("homepage", "https://example.com");
URL url = redisJsonUtil.get("homepage", URL.class);
```

#### 示例3：自定义日期格式转换器

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 支持自定义日期格式的转换器
 * 优先级高于内置转换器，优先尝试自定义格式
 */
public class CustomDateTimeConverter implements TypeConverter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime convert(String value) throws Exception {
        return LocalDateTime.parse(value, FORMATTER);
    }

    @Override
    public Class<LocalDateTime> supportType() {
        return LocalDateTime.class;
    }

    @Override
    public int priority() {
        return 5;  // 优先级高于内置转换器（30）
    }
}

// 注册（会覆盖内置的 LocalDateTime 转换器）
redisJsonUtil.registerConverter(new CustomDateTimeConverter());

// 使用
redisJsonUtil.setString("time", "2024-01-01 10:30:00");
LocalDateTime time = redisJsonUtil.get("time", LocalDateTime.class);  // ✅ 使用自定义格式
```

#### 示例4：通用枚举转换器

```java
/**
 * 通用枚举转换器（支持所有枚举类型）
 */
public class GenericEnumConverter<E extends Enum<E>> implements TypeConverter<E> {

    private final Class<E> enumType;

    public GenericEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public E convert(String value) throws Exception {
        return Enum.valueOf(enumType, value.toUpperCase());
    }

    @Override
    public Class<E> supportType() {
        return enumType;
    }

    @Override
    public int priority() {
        return 150;
    }
}

// 注册多个枚举类型
@Configuration
public class RedisConfig {

    @Bean
    public CommandLineRunner registerEnumConverters(RedisJsonUtil redisJsonUtil) {
        return args -> {
            redisJsonUtil.registerConverter(new GenericEnumConverter<>(Status.class));
            redisJsonUtil.registerConverter(new GenericEnumConverter<>(UserRole.class));
            redisJsonUtil.registerConverter(new GenericEnumConverter<>(OrderType.class));
        };
    }
}
```

### 3. 批量注册转换器

```java
@Configuration
public class RedisConverterConfig {

    @Bean
    public CommandLineRunner registerAllConverters(RedisJsonUtil redisJsonUtil) {
        return args -> {
            TypeConverterRegistry registry = TypeConverterRegistry.getInstance();

            // 批量注册
            registry.registerAll(
                new StatusEnumConverter(),
                new URLConverter(),
                new CustomDateTimeConverter()
            );
        };
    }
}
```

## 转换优先级

转换器按优先级执行（数字越小优先级越高）：

| 优先级范围 | 用途 | 示例 |
|-----------|------|------|
| 0-9 | 最高优先级（保留） | - |
| 10-19 | 基本类型 | Boolean, Integer, Long... |
| 20-29 | 大数类型 | BigDecimal, BigInteger |
| 30-39 | 日期时间类型 | LocalDateTime, Date |
| 40-99 | 内置扩展类型 | Instant |
| 100+ | 自定义类型 | 用户自定义转换器 |

## 转换流程

```
convertValue(value, type)
    ↓
1. type == String? → 直接返回
    ↓
2. 查找注册的转换器
    ↓
3. 找到转换器? → 使用转换器.convert(value)
    ↓
4. 转换成功? → 返回结果
    ↓
5. 转换失败 → 尝试 JSON 反序列化
    ↓
6. JSON 反序列化 → 返回结果
```

## 高级用法

### 1. 运行时动态注册

```java
@Service
public class DynamicConverterService {

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    public void addCustomConverter(TypeConverter<?> converter) {
        redisJsonUtil.registerConverter(converter);
    }
}
```

### 2. 检查是否支持某个类型

```java
TypeConverterRegistry registry = TypeConverterRegistry.getInstance();

if (registry.supports(MyCustomType.class)) {
    // 支持转换
} else {
    // 不支持，需要注册转换器
    registry.register(new MyCustomTypeConverter());
}
```

### 3. 获取所有支持的类型

```java
TypeConverterRegistry registry = TypeConverterRegistry.getInstance();
List<Class<?>> supportedTypes = registry.getSupportedTypes();

log.info("Supported types: {}", supportedTypes);
```

### 4. 重置转换器（测试时使用）

```java
// 清空所有转换器
TypeConverterRegistry.getInstance().clear();

// 重置为默认状态（重新加载内置转换器）
TypeConverterRegistry.getInstance().reset();
```

## 实际应用场景

### 场景1：值班系统状态管理

```java
public enum DutyStatus {
    OPEN, CLOSED, EMERGENCY
}

// 注册枚举转换器
redisJsonUtil.registerConverter(new GenericEnumConverter<>(DutyStatus.class));

// 使用
redisJsonUtil.hSet("duty:status", storeCode, DutyStatus.OPEN);
DutyStatus status = redisJsonUtil.hGet("duty:status", storeCode, DutyStatus.class);
```

### 场景2：货币金额处理

```java
// BigDecimal 自动支持，无需注册
redisJsonUtil.hSet("store:revenue", storeCode, new BigDecimal("12345.67"));
BigDecimal revenue = redisJsonUtil.hGet("store:revenue", storeCode, BigDecimal.class);
```

### 场景3：时间戳处理

```java
// Date 自动支持时间戳和 ISO-8601 格式
redisJsonUtil.set("lastSync", System.currentTimeMillis());  // 存储时间戳
Date lastSync = redisJsonUtil.get("lastSync", Date.class);  // 自动解析

redisJsonUtil.setString("lastSync", "2024-01-01T10:30:00");  // 存储 ISO-8601
Date lastSync2 = redisJsonUtil.get("lastSync", Date.class);  // 自动解析
```

## 性能优化

1. **转换器缓存**：`TypeConverterRegistry` 内部使用 `ConcurrentHashMap` 缓存转换器，查找时间复杂度 O(1)
2. **基本类型映射**：自动处理基本类型（int, long...）到包装类型（Integer, Long...）的映射
3. **失败快速降级**：转换器失败时立即降级到 JSON 反序列化

## 线程安全

- `TypeConverterRegistry` 是线程安全的（使用 synchronized）
- 支持多线程并发注册和查找
- 单例模式确保全局唯一

## 最佳实践

1. ✅ **转换器应该是无状态的**：不要在转换器中保存状态
2. ✅ **优先级合理设置**：自定义转换器使用 100+ 优先级
3. ✅ **异常处理**：转换器抛出异常时会自动降级到 JSON 反序列化
4. ✅ **启动时注册**：使用 `@Configuration` + `CommandLineRunner` 在启动时注册
5. ❌ **避免循环依赖**：转换器内部不要使用 `RedisJsonUtil`

## 总结

通过插件式的类型转换器机制，`RedisJsonUtil` 提供了：

- 🔌 **可扩展性**：轻松添加新类型支持
- 🎯 **类型安全**：编译时类型检查
- ⚡ **高性能**：O(1) 查找，失败快速降级
- 🔧 **灵活性**：支持运行时动态注册
- 📦 **开箱即用**：内置 15+ 常用类型转换器
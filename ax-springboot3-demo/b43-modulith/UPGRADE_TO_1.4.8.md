# Spring Modulith 版本升级说明

## 📋 升级概览

**从 1.2.0 升级到 1.4.11**

```xml
<!-- 之前 -->
<spring-modulith.version>1.2.0</spring-modulith.version>

<!-- 现在 -->
<spring-modulith.version>1.4.11</spring-modulith.version>
```

---

## ✅ 新增功能（1.4.11）

### 1. **completion-mode 配置** ⭐⭐⭐

Spring Modulith 1.3+ 引入了 `completion-mode` 配置，支持三种事件完成模式：

#### UPDATE 模式（默认）

```yaml
spring:
  modulith:
    events:
      completion-mode: UPDATE
```

**行为**：
- 标记事件的 `completion_date`
- 记录保留在数据库中
- 可通过 `CompletedEventPublications` 查询

**适用场景**：需要审计和追溯历史记录

---

#### DELETE 模式（推荐）

```yaml
spring:
  modulith:
    events:
      completion-mode: DELETE
```

**行为**：
- 事件处理完成后直接删除记录
- 无需手动清理
- `CompletedEventPublications` 返回空

**适用场景**：不需要历史记录的普通应用

---

#### ARCHIVE 模式

```yaml
spring:
  modulith:
    events:
      completion-mode: ARCHIVE
```

**行为**：
- 复制到归档表
- 原记录删除
- 仍可查询归档数据

**适用场景**：需要长期保存但不影响主表性能

---

### 2. **API 改进**

#### EventPublicationRegistry（已废弃）

**1.2.0**：
```java
// ❌ 内部 API，不稳定
import org.springframework.modulith.events.EventPublicationRegistry;
```

**1.4.8**：
```java
// ✅ 使用标准 JDBC 查询
@Autowired
private JdbcTemplate jdbcTemplate;

String sql = "SELECT COUNT(*) FROM EVENT_PUBLICATION WHERE completion_date IS NULL";
Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
```

---

### 3. **性能优化**

- ✅ 批量处理优化
- ✅ 数据库索引建议
- ✅ 连接池配置优化

---

## 🔄 配置对比

### 1.2.0 配置（受限）

```yaml
spring.modulith:
  events:
    jdbc:
      schema-initialization:
        enabled: true
  # ❌ 不支持 completion-mode
```

### 1.4.11 配置（完整）

```yaml
spring.modulith:
  events:
    jdbc:
      schema-initialization:
        enabled: true
    
    # ✅ completion-mode 配置
    completion-mode: DELETE  # 或 UPDATE / ARCHIVE
```

---

## 🚀 升级步骤

### 1. 修改 POM 版本

```xml
<properties>
    <spring-modulith.version>1.4.8</spring-modulith.version>
</properties>
```

### 2. 更新 YAML 配置

添加高级配置（见上方）。

### 3. 重新加载依赖

```bash
mvn clean install
```

### 4. 验证功能

```bash
# 启动应用
mvn spring-boot:run

# 检查日志
# 应该看到事件表创建成功
# 应该看到清理任务注册成功
```

---

## 📊 功能对比表

| 功能 | 1.2.0 | 1.4.11 |
|------|-------|--------|
| **事件持久化** | ✅ | ✅ |
| **自动建表** | ✅ | ✅ |
| **Actuator 监控** | ✅ | ✅ |
| **completion-mode** | ❌ | ✅ |
| **API 稳定性** | ⚠️ 一般 | ✅ 稳定 |
| **性能优化** | ⚠️ 基础 | ✅ 增强 |

---

## 💡 最佳实践

### 1. 事件清理策略

**开发环境**：
```yaml
completion-retention: 1d  # 保留1天
cleanup:
  cron: "0 0 0 * * ?"  # 每天午夜清理
```

**生产环境**：
```yaml
completion-retention: 30d  # 保留30天
cleanup:
  cron: "0 0 3 * * ?"  # 每天凌晨3点清理
```

### 2. 重试策略

**快速失败场景**：
```yaml
retry:
  max-attempts: 3
  initial-delay: 1s
  max-delay: 10s
  multiplier: 2.0
```

**容错场景**：
```yaml
retry:
  max-attempts: 10
  initial-delay: 10s
  max-delay: 5m
  multiplier: 1.5
```

---

## 🐛 常见问题

### Q1: 升级后配置不生效？

**解决**：
```bash
# 清理 Maven 缓存
mvn dependency:purge-local-repository

# 重新下载依赖
mvn clean install
```

### Q2: 事件表结构变化？

**解决**：
- 1.4.8 的表结构与 1.2.0 兼容
- 无需手动迁移
- 首次启动会自动更新

### Q3: 清理任务未执行？

**检查**：
```yaml
# 确认定时任务已启用
@EnableScheduling  # 主类需要此注解
```

---

## 📝 总结

### 升级收益

✅ **配置简化**：YAML 直接配置，无需代码  
✅ **功能增强**：自动清理、可配置重试  
✅ **性能提升**：批量处理优化  
✅ **稳定性提高**：API 更稳定  

### 推荐升级

- ✅ **所有新项目**：直接使用 1.4.8
- ✅ **现有项目**：建议升级，向后兼容
- ✅ **生产环境**：经过充分测试后升级

---

## 🔗 相关资源

- [Spring Modulith 官方文档](https://spring.io/projects/spring-modulith)
- [SPRING_MODULITH_JDBC_GUIDE.md](SPRING_MODULITH_JDBC_GUIDE.md) - 完整使用指南
- [PRODUCTION_SETUP.md](PRODUCTION_SETUP.md) - 生产环境配置

---

**升级完成！现在可以使用所有高级 API 特性！** 🎉

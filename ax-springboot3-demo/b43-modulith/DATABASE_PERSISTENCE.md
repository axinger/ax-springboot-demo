# 数据库持久化方案

## 📋 问题说明

**原配置问题**：
- 使用 H2 内存数据库 (`jdbc:h2:mem:testdb`)
- 服务重启后所有数据丢失
- 不适合生产环境

---

## ✅ 解决方案

### 方案对比

| 方案 | 适用场景 | 优点 | 缺点 |
|------|---------|------|------|
| **H2 文件数据库** | 开发/测试 | 无需安装、配置简单 | 性能一般、不适合高并发 |
| **MySQL/MariaDB** | 生产环境 | 性能优秀、成熟稳定 | 需要安装数据库 |
| **PostgreSQL** | 生产环境 | 功能强大、开源免费 | 需要安装数据库 |

---

## 🔧 方案 1：H2 文件数据库（当前已配置）

### 配置说明

**application.yml**：
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/modulith_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password: 123456
  
  jpa:
    hibernate:
      ddl-auto: update  # 自动更新表结构
  
  sql:
    init:
      mode: embedded  # 只在嵌入式数据库时执行初始化
```

### 关键参数说明

| 参数 | 说明 |
|------|------|
| `jdbc:h2:file:./data/modulith_db` | 数据库文件存储在 `./data/modulith_db.mv.db` |
| `DB_CLOSE_DELAY=-1` | JVM 退出时不立即关闭数据库 |
| `DB_CLOSE_ON_EXIT=FALSE` | 禁用自动关闭，确保数据安全 |
| `ddl-auto: update` | 自动更新表结构，保留数据 |
| `mode: embedded` | 只在嵌入式数据库时执行 SQL 初始化 |

### 数据存储位置

```
b43-modulith/
└── data/
    ├── modulith_db.mv.db      # H2 数据库文件
    └── modulith_db.trace.db   # H2 跟踪日志（可选）
```

### 使用步骤

1. **启动应用**
   ```bash
   cd b43-modulith-app
   mvn spring-boot:run
   ```

2. **验证数据持久化**
   - 创建一些测试数据
   - 停止应用（Ctrl+C）
   - 重新启动应用
   - 访问 H2 控制台查看数据是否保留

3. **访问 H2 控制台**
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:file:./data/modulith_db`
   - 用户名: `sa`
   - 密码: `123456`

### 备份和恢复

**备份**：
```bash
# 复制数据库文件
cp data/modulith_db.mv.db backup/modulith_db_20260511.mv.db
```

**恢复**：
```bash
# 停止应用
# 替换数据库文件
cp backup/modulith_db_20260511.mv.db data/modulith_db.mv.db
# 重新启动应用
```

---

## 🔧 方案 2：MySQL（生产推荐）

### 前置要求

1. 安装 MySQL 8.0+
2. 创建数据库和用户

### 配置步骤

#### 1. 创建数据库

```sql
CREATE DATABASE modulith_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'modulith'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON modulith_db.* TO 'modulith'@'localhost';
FLUSH PRIVILEGES;
```

#### 2. 修改 application.yml

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/modulith_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: modulith
    password: your_password
  
  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: validate  # 生产环境使用 validate
  
  sql:
    init:
      mode: never  # 生产环境不自动执行 SQL
```

#### 3. 添加 Maven 依赖

在 `b43-modulith-app/pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

#### 4. 手动执行 SQL 脚本

```bash
mysql -u modulith -p modulith_db < src/main/resources/data.sql
```

---

## 🔧 方案 3：Docker + MySQL（推荐开发环境）

### docker-compose.yml

已在项目根目录提供 `docker-compose.yml`，包含 MySQL 配置。

### 使用步骤

1. **启动 MySQL 容器**
   ```bash
   docker-compose up -d mysql
   ```

2. **等待 MySQL 就绪**
   ```bash
   docker-compose logs -f mysql
   # 看到 "ready for connections" 表示就绪
   ```

3. **修改 application.yml**（同方案 2）

4. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

---

## 🔄 服务重启策略

### 不同场景的配置

#### 开发环境
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 自动更新表结构
  sql:
    init:
      mode: always  # 每次启动都执行初始化
```

#### 测试环境
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop  # 每次重启重建
  sql:
    init:
      mode: always
```

#### 生产环境
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # 只验证，不修改
  sql:
    init:
      mode: never  # 不执行初始化
```

---

## 📊 数据迁移工具（Flyway）

### 为什么需要 Flyway？

- ✅ 版本控制数据库变更
- ✅ 自动化数据库迁移
- ✅ 团队协作更安全
- ✅ 回滚支持

### 集成步骤

#### 1. 添加依赖

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

#### 2. 创建迁移脚本

```
src/main/resources/db/migration/
├── V1__create_customer_table.sql
├── V2__create_order_table.sql
├── V3__create_inventory_table.sql
└── V4__insert_test_data.sql
```

#### 3. 示例迁移脚本

**V1__create_customer_table.sql**：
```sql
CREATE TABLE customer (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 4. 配置 Flyway

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

---

## 🎯 最佳实践建议

### 开发阶段
1. ✅ 使用 H2 文件数据库
2. ✅ `ddl-auto: update`
3. ✅ `mode: embedded`
4. ✅ 定期备份 `data/` 目录

### 测试阶段
1. ✅ 使用 MySQL Docker 容器
2. ✅ `ddl-auto: create-drop`
3. ✅ 每次测试前重置数据

### 生产阶段
1. ✅ 使用 MySQL/PostgreSQL
2. ✅ `ddl-auto: validate`
3. ✅ 使用 Flyway 管理迁移
4. ✅ 定期备份数据库
5. ✅ 配置主从复制

---

## 🐛 常见问题

### Q1: 重启后数据丢失？
**检查**：
- 确认使用的是文件数据库而非内存数据库
- 检查 `url` 是否为 `jdbc:h2:file:` 开头
- 确认 `ddl-auto` 不是 `create-drop`

### Q2: H2 控制台无法连接？
**解决**：
```yaml
# 使用正确的 JDBC URL
jdbc:h2:file:./data/modulith_db
```

### Q3: 表结构不一致？
**解决**：
- 删除 `data/` 目录
- 重启应用重新创建
- 或使用 Flyway 管理迁移

### Q4: 如何切换到 MySQL？
**步骤**：
1. 安装 MySQL
2. 创建数据库
3. 修改 `application.yml`
4. 添加 MySQL 驱动依赖
5. 执行 SQL 脚本

---

## 📝 总结

| 项目 | 当前配置 | 说明 |
|------|---------|------|
| **数据库类型** | H2 文件数据库 | ✅ 已配置持久化 |
| **存储位置** | `./data/modulith_db.mv.db` | 相对路径 |
| **DDL 策略** | `update` | 自动更新表结构 |
| **数据初始化** | `embedded` | 只在嵌入式数据库时执行 |
| **重启后数据** | ✅ 保留 | 数据持久化成功 |

**当前配置已经实现数据持久化，服务重启后数据不会丢失！** 🎉

---

## 🔗 相关文档

- [SPRING_MODULITH_GUIDE.md](SPRING_MODULITH_GUIDE.md) - 项目完整指南
- [API_TEST_GUIDE.md](API_TEST_GUIDE.md) - API 测试指南
- [CODE_REVIEW_REPORT.md](CODE_REVIEW_REPORT.md) - 代码审查报告

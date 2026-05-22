# 📖 应用运维脚本使用指南

> 📚 **返回主目录**: [README.md](../README.md) | ⚡ **快速配置**: [AUTOSTART_QUICK.md](../AUTOSTART_QUICK.md)

## 📋 目录

- [快速开始](#-快速开始)
- [配置方式](#-配置方式)
- [脚本功能](#-脚本功能)
- [使用示例](#-使用示例)
- [参数详解](#️-参数详解)
- [常见问题](#-常见问题)
- [维护建议](#-维护建议)

---

## 🚀 快速开始

### ⚡ 最快速的配置方式（推荐）

#### 1. 编辑 `.env` 配置文件

打开 `bin/.env` 文件，修改你需要的配置：

```bash
vim bin/.env
```

修改关键配置项：

```bash
# Java 版本 (8, 11, 17, 21 或留空自动检测)
JAVA_VERSION=17

# Nacos 服务器地址
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR=192.168.1.100:8848
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=192.168.1.100:8848

# 应用名称
APP_NAME=my-app
```

#### 2. 启动应用

```bash
cd bin/
chmod +x start.sh stop.sh  # 首次使用需要赋予执行权限
./start.sh
```

完成！🎉

---

## 🔧 配置方式

### 配置优先级

配置值的优先级从高到低：

1. **命令行参数** (最高优先级) - 临时覆盖
2. **.env 配置文件** (推荐) - 持久化配置
3. **脚本内默认值** (最低优先级) - 基础默认值

### 三种配置方式对比

| 方式 | 适用场景 | 优点 | 缺点 |
|------|---------|------|------|
| **.env 文件** | 日常使用、环境隔离 | 持久化、易管理、可版本控制(模板) | 修改后需重启 |
| **命令行参数** | 临时测试、紧急调整 | 即时生效、无需修改文件 | 每次都要输入 |
| **修改脚本** | 固定配置、统一部署 | 一次修改永久生效 | 不灵活、升级困难 |

### 方式一：使用 .env 配置文件（推荐）✨

#### 1. 创建配置文件

```bash
cd bin/
cp .env.example .env  # 从模板复制
```

#### 2. 编辑配置文件

```bash
vim .env
```

完整配置示例：

```bash
# ------------------- 基础配置 -------------------
JAVA_VERSION=17                          # Java 版本: 8, 11, 17, 21 或留空
APP_NAME=my-app                          # 应用名称
APP_PATH=/opt/application/my-app         # 应用路径
LOG_PATH=/var/logs/myapp                 # 日志路径
MODE=cluster                             # 运行模式: cluster 或 standalone

# ------------------- Nacos 配置中心 -------------------
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR=nacos-server:8848
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos-server:8848

# ------------------- Spring Boot 配置 -------------------
SERVER_MAX_HTTP_HEADER_SIZE=524288       # HTTP 头大小限制(字节)
SPRING_CONFIG_LOCATION=classpath:/,classpath:/config/,file:./,file:./config/

# ------------------- 其他自定义 JVM 参数 -------------------
JAVA_OPT_EXT="-Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8"
```

#### 3. 启动应用

```bash
./start.sh
```

脚本会自动加载 `.env` 文件中的配置。

### 方式二：直接修改脚本默认值

编辑 `start.sh` 文件，修改顶部的默认配置区域：

```bash
# ------------------- 默认基础配置 (可直接在此处修改) -------------------
APP_PATH="/opt/application/my-app"
APP_NAME="my-app"
LOG_PATH="/opt/logs/myapp"
MODE="cluster"
JAVA_VERSION="17"

# Spring Cloud Nacos 配置
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR="nacos-server:8848"
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR="nacos-server:8848"
```

### 方式三：使用命令行参数（临时覆盖）

```bash
# 指定 JDK 17 和 Nacos 地址
./start.sh -j 17 -a 192.168.1.100:8848

# 完整参数示例
./start.sh \
  -n my-app \
  -p /opt/application/my-app \
  -a nacos-server:8848 \
  -l /var/logs/myapp \
  -m standalone \
  -j 17
```

---

## 📂 脚本功能

本目录包含用于管理 Java Spring Boot 应用的启动与停止脚本。脚本已针对生产环境进行了优化，支持：

- ✅ Nacos 配置中心和服务发现
- ✅ 多 JDK 版本支持 (8, 11, 17, 21)
- ✅ 灵活的配置管理 (.env 文件/命令行参数/脚本默认值)
- ✅ 自定义路径和日志管理
- ✅ 优雅停机及超时强制终止
- ✅ 完善的日志记录和 GC 监控
- ✅ PID 文件管理和进程检测

| 文件名 | 功能描述 |
| ------ |------ |
| **start.sh** | 启动应用，支持指定 Nacos 地址、JDK 版本、运行模式及日志路径 |
| **stop.sh** | 停止应用，通过 PID 文件精准定位进程，支持超时强制终止 |
| **.env** | 当前环境的配置文件（不会被 Git 跟踪） |
| **.env.example** | 配置文件模板 |
| **.env.prod.example** | 生产环境配置示例 |

---

## 💻 使用示例

### start.sh 使用示例

#### 1. 默认启动（最常用）

直接运行脚本，使用 `.env` 文件或内置的默认配置。

```bash
./start.sh
```

#### 2. 指定 Nacos 服务器并切换为单机模式

适用于开发或测试环境，减少内存占用并连接指定的配置中心。

```bash
./start.sh -a 192.168.1.100:8848 -m standalone
```

#### 3. 指定 JDK 版本

当不同项目需要使用不同 JDK 版本时：

```bash
# 使用 JDK 8
./start.sh -j 8

# 使用 JDK 17
./start.sh -j 17

# 使用 JDK 21
./start.sh -j 21
```

#### 4. 自定义应用名称和日志路径

当一台服务器上部署多个实例，或需要将日志输出到特定磁盘分区时使用。

```bash
./start.sh -n order-service -l /data/logs/order-service
```

#### 5. 组合使用多个参数（全量自定义）

生产环境推荐方式，明确指定各项路径与参数，确保环境隔离。

```bash
./start.sh \
  -p /opt/apps/payment-service \
  -n payment-app \
  -a nacos.prod.local:8848 \
  -l /var/log/payment \
  -m cluster \
  -j 17
```

### stop.sh 使用示例

#### 1. 默认停止

如果启动时使用的是默认的 `demo-application` 名称，直接运行即可。脚本会通过 `.pid` 文件精准定位并优雅关闭进程。

```bash
./stop.sh
```

#### 2. 停止指定的应用

如果在启动时通过 `-n` 参数指定了其他的应用名称（例如上面的 `order-service`），停止时必须带上对应的名称。

```bash
./stop.sh -n order-service
```

---

## ⚙️ 参数详解

### start.sh 参数列表

| 参数 | 说明 | 默认值 | 示例 |
| ------ |------ |------ |------ |
| **-p** | 应用所在的绝对路径 (不含 jar 包名) | `/opt/application/demo-app` | `-p /opt/myapp` |
| **-n** | 应用名称 (需与 jar 包文件名一致) | `demo-application` | `-n my-app` |
| **-a** | Nacos 服务器地址 (IP:Port) | `localhost:8848` | `-a 192.168.1.100:8848` |
| **-l** | 日志输出根路径 | `/opt/logs/demo` | `-l /var/logs/myapp` |
| **-m** | 运行模式: `cluster` (集群) 或 `standalone` (单机) | `cluster` | `-m standalone` |
| **-j** | Java 版本: `8`, `11`, `17`, `21` 或留空自动检测 | 自动检测 | `-j 17` |

### stop.sh 参数列表

| 参数 | 说明 | 默认值 | 示例 |
| ------ |------ |------ |------ |
| **-n** | 需要停止的应用名称 (必须与启动时的 -n 保持一致) | `demo-application` | `-n my-app` |

---

## 📝 可用的配置项

### 基础配置

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `APP_PATH` | 应用部署路径 | `/opt/application/demo-app` |
| `APP_NAME` | 应用名称 | `demo-application` |
| `LOG_PATH` | 日志存储路径 | `/opt/logs/demo` |
| `MODE` | 运行模式 | `cluster` 或 `standalone` |
| `JAVA_VERSION` | Java 版本 | `8`, `11`, `17`, `21` 或留空 |

### Nacos 配置

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR` | Nacos 配置中心地址 | `localhost:8848` |
| `SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR` | Nacos 服务发现地址 | `localhost:8848` |

### Spring Boot 配置

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `SPRING_CONFIG_LOCATION` | 配置文件搜索路径 | `classpath:/,classpath:/config/,file:./,file:./config/` |
| `SERVER_MAX_HTTP_HEADER_SIZE` | HTTP 请求头大小限制 | `524288` (512KB) |

### 其他配置

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `JAVA_OPT_EXT` | 额外的 JVM 参数 | `-Duser.timezone=Asia/Shanghai` |

---

## 💡 使用场景示例

### 场景 1：不同环境使用不同配置

**开发环境 (.env.dev)**
```bash
JAVA_VERSION=""
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR="localhost:8848"
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR="localhost:8848"
```

**测试环境 (.env.test)**
```bash
JAVA_VERSION="17"
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR="nacos-test:8848"
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR="nacos-test:8848"
```

**生产环境 (.env.prod)**
```bash
JAVA_VERSION="17"
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR="nacos-prod.example.com:8848"
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR="nacos-prod.example.com:8848"
```

使用时：
```bash
# 复制对应环境的配置
cp .env.prod .env

# 启动应用
./start.sh
```

### 场景 2：同一服务器运行多个项目

**项目 A (JDK 8)**
```bash
# bin/.env.project-a
APP_NAME="project-a"
JAVA_VERSION="8"
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR="localhost:8848"
```

**项目 B (JDK 17)**
```bash
# bin/.env.project-b
APP_NAME="project-b"
JAVA_VERSION="17"
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR="localhost:8848"
```

启动时指定不同的配置文件：
```bash
# 启动项目 A
ENV_FILE=.env.project-a ./start.sh

# 启动项目 B
ENV_FILE=.env.project-b ./start.sh
```

### 场景 3：临时覆盖配置

即使配置了 `.env` 文件，仍然可以通过命令行参数临时覆盖：

```bash
# .env 中配置的是 localhost，但临时连接到测试环境
./start.sh -a nacos-test:8848

# 临时使用单机模式
./start.sh -m standalone

# 临时使用不同的 JDK 版本
./start.sh -j 8
```

---

## ❓ 常见问题与提示

### 赋予执行权限

首次上传脚本后，请执行以下命令赋予权限：

```bash
chmod +x start.sh stop.sh
```

### 查看实时日志

启动成功后，可以使用以下命令监控应用启动情况：

```bash
tail -f /opt/logs/demo/demo-application.log
```

### 传入额外 JVM 参数

如果需要开启远程调试或设置时区，可以在执行启动脚本前设置环境变量 `JAVA_OPT_EXT`：

```bash
# 方式一：在 .env 文件中配置
JAVA_OPT_EXT="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

# 方式二：命令行临时设置
export JAVA_OPT_EXT="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
./start.sh
```

### 验证配置是否生效

启动应用时，会显示当前使用的配置：

```
📄 发现配置文件 .env，正在加载自定义默认值...
🔍 正在根据配置查找 Java 17...
✅ 成功定位到 Java 17: /usr/lib/jvm/java-17-openjdk-amd64
☕ 当前使用的 Java 版本信息:
openjdk version "17.0.8" 2023-07-18 LTS

🚀 正在启动应用: my-custom-app ...
☕ Java 路径: /usr/lib/jvm/java-17-openjdk-amd64
⚙️ 运行模式: standalone
📄 日志文件: /opt/logs/demo/my-custom-app.log
🔗 Nacos 配置中心: 192.168.1.100:8848    ← 这里显示你的配置
🔗 Nacos 服务发现: 192.168.1.100:8848    ← 这里显示你的配置
----------------------------------------
✅ 应用 my-custom-app 启动成功！进程 PID: 12345
```

### Q: 修改 `.env` 后需要重启吗？
A: 是的，修改后需要停止应用并重新启动才能生效。

### Q: 可以临时覆盖 `.env` 的配置吗？
A: 可以，使用命令行参数：
```bash
./start.sh -a nacos-temp:8848  # 临时使用不同的 Nacos 地址
```

### Q: 如何查看当前使用的配置？
A: 启动应用时会显示所有配置信息，或者查看日志文件。

### Q: `.env` 文件会被 Git 跟踪吗？
A: 不应该被跟踪。确保 `.env` 已添加到 `.gitignore` 文件中。

### Q: 如何查找系统中已安装的 JDK 版本？
A: 使用以下命令：
```bash
# 查看所有已安装的 JDK
ls /usr/lib/jvm/

# 或使用 update-alternatives 查看
update-alternatives --list java
```

---

## ⚠️ 注意事项

1. **路径一致性**：启动和停止脚本必须在同一个目录下执行，以确保 PID 文件路径正确
2. **权限检查**：确保应用有权限读取配置文件和写入日志目录
3. **端口冲突**：检查应用所需端口是否已被占用
4. **环境变量**：确保 JAVA_HOME 环境变量已正确设置或脚本能够自动检测
5. **.env 文件安全**：不要在 `.env` 文件中硬编码密码等敏感信息
6. **配置生效时机**：修改 `.env` 后，下次启动时生效
7. **JDK 版本兼容性**：确保应用代码与指定的 JDK 版本兼容

---

## 🔄 维护建议

- **定期清理**：定期检查并清理旧的日志文件，避免磁盘空间耗尽
- **监控告警**：建议配置进程监控和日志告警，及时发现应用异常
- **备份配置**：重要的配置文件建议定期备份，防止意外丢失
- **版本管理**：使用 `.env.example` 作为模板，`.env` 文件加入 `.gitignore`
- **文档更新**：配置变更时及时更新相关文档

---

## 📚 相关文件

- `start.sh` - 应用启动脚本
- `stop.sh` - 应用停止脚本
- `.env` - 当前环境的配置文件（需手动创建，不会被 Git 跟踪）
- `.env.example` - 配置文件模板（包含所有可用配置项）
- `.env.prod.example` - 生产环境配置示例
- `demo-app.service` - systemd 服务配置模板（用于开机自启动）
- [AUTOSTART_GUIDE.md](AUTOSTART_GUIDE.md) - 开机自启动详细配置指南

# Java Spring Boot 应用运维脚本使用指南

## 📂 脚本文件说明

本目录包含用于管理 Java Spring Boot 应用的启动与停止脚本。脚本已针对生产环境进行了优化，支持 Nacos 配置中心、自定义路径、优雅停机及完善的日志记录。

| 文件名 | 功能描述 |
| ------ |------ |
| **start.sh** | 启动应用，支持指定 Nacos 地址、运行模式（集群/单机）及日志路径 |
| **stop.sh** | 停止应用，通过 PID 文件精准定位进程，支持超时强制终止 |

## 🚀 start.sh 使用示例

### 1. 默认启动（最常用）

直接运行脚本，使用内置的默认配置（应用路径 `/opt/application/demo-app`，Nacos 地址 `localhost:8848`，集群模式）。

```bash
./start.sh
```

### 2. 指定 Nacos 服务器并切换为单机模式

适用于开发或测试环境，减少内存占用并连接指定的配置中心。

```bash
./start.sh -a 192.168.1.100:8848 -m standalone
```

### 3. 自定义应用名称和日志路径

当一台服务器上部署多个实例，或需要将日志输出到特定磁盘分区时使用。

```bash
./start.sh -n order-service -l /data/logs/order-service
```

### 4. 组合使用多个参数（全量自定义）

生产环境推荐方式，明确指定各项路径与参数，确保环境隔离。

```bash
./start.sh \
  -p /opt/apps/payment-service \
  -n payment-app \
  -a nacos.prod.local:8848 \
  -l /var/log/payment \
  -m cluster
```

## 🛑 stop.sh 使用示例

### 1. 默认停止

如果启动时使用的是默认的 `demo-application` 名称，直接运行即可。脚本会通过 `.pid` 文件精准定位并优雅关闭进程。

```bash
./stop.sh
```

### 2. 停止指定的应用

如果在启动时通过 `-n` 参数指定了其他的应用名称（例如上面的 `order-service`），停止时必须带上对应的名称。

```bash
./stop.sh -n order-service
```

## ⚙️ 参数详解

### start.sh 参数列表

| 参数 | 说明 | 默认值 |
| ------ |------ |------ |
| **-p** | 应用所在的绝对路径 (不含 jar 包名) | `/opt/application/demo-app` |
| **-n** | 应用名称 (需与 jar 包文件名一致) | `demo-application` |
| **-a** | Nacos 服务器地址 (IP:Port) | `localhost:8848` |
| **-l** | 日志输出根路径 | `/opt/logs/demo` |
| **-m** | 运行模式: `cluster` (集群) 或 `standalone` (单机) | `cluster` |

### stop.sh 参数列表

| 参数 | 说明 | 默认值 |
| ------ |------ |------ |
| **-n** | 需要停止的应用名称 (必须与启动时的 -n 保持一致) | `demo-application` |

## 💡 常见问题与提示

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
# 示例：开启 5005 端口的远程调试
export JAVA_OPT_EXT="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
./start.sh
```

## 📝 注意事项

1. **路径一致性**：启动和停止脚本必须在同一个目录下执行，以确保 PID 文件路径正确
2. **权限检查**：确保应用有权限读取配置文件和写入日志目录
3. **端口冲突**：检查应用所需端口是否已被占用
4. **环境变量**：确保 JAVA_HOME 环境变量已正确设置或脚本能够自动检测

## 🔄 维护建议

- **定期清理**：定期检查并清理旧的日志文件，避免磁盘空间耗尽
- **监控告警**：建议配置进程监控和日志告警，及时发现应用异常
- **备份配置**：重要的配置文件建议定期备份，防止意外丢失

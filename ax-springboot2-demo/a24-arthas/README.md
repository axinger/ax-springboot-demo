# Arthas 演示项目

本项目是一个 Spring Boot + Arthas 的演示项目，用于展示如何使用 Arthas 进行 Java 应用诊断和性能分析。

## 什么是 Arthas？

Arthas 是 Alibaba 开源的 Java 诊断工具，可以帮助你：
- 在线排查问题，无需重启应用
- 监控 JVM 状态和性能指标
- 分析方法调用链路和耗时
- 查看类的结构和反编译代码
- 实时监控方法执行情况
- 诊断性能瓶颈和内存泄漏

## 项目结构

```
a24-arthas/
├── src/main/java/com/github/axinger/
│   ├── A24Application.java          # 主应用类
│   ├── controller/
│   │   └── DemoController.java      # REST API 控制器
│   ├── service/
│   │   └── DemoService.java         # 业务服务类
│   └── model/
│       └── User.java                # 数据模型
├── src/main/resources/
│   └── application.yml              # 应用配置
└── pom.xml                          # Maven 配置
```

## 快速开始

### 1. 启动应用

```bash
mvn spring-boot:run
```

或者在 IDE 中运行 `A24Application` 类。

### 2. 访问 Arthas

应用启动后，可以通过以下方式访问 Arthas：

**方式一：Web Console（推荐）**
```
浏览器访问: http://127.0.0.1:8563
```

**方式二：Telnet**
```bash
telnet 127.0.0.1 3658
```

### 3. 测试 API 接口

应用提供了多个 API 接口用于演示：

```bash
# 获取用户列表
curl http://localhost:15024/api/demo/users?count=10

# 执行计算
curl http://localhost:15024/api/demo/calculate?a=10&b=20

# 处理数据（POST）
curl -X POST http://localhost:15024/api/demo/process \
  -H "Content-Type: application/json" \
  -d '{"input": "hello world"}'

# 慢操作演示
curl http://localhost:15024/api/demo/slow

# Fibonacci 计算
curl http://localhost:15024/api/demo/fibonacci?n=15

# 获取系统信息
curl http://localhost:15024/api/demo/system-info
```

## Arthas 常用命令示例

### 1. dashboard - 实时数据面板

查看系统的实时数据，包括线程、内存、GC 等信息：

```bash
dashboard
```

按 `q` 退出。

### 2. thread - 线程信息

查看所有线程：
```bash
thread
```

查看最忙的前 N 个线程：
```bash
thread -n 3
```

查看指定线程的堆栈：
```bash
thread <thread-id>
```

### 3. jad - 反编译类

反编译指定类，查看源码：
```bash
jad com.github.axinger.service.DemoService
```

反编译指定方法：
```bash
jad com.github.axinger.service.DemoService getUsers
```

### 4. watch - 观察方法执行

观察方法的参数、返回值、异常等信息：

```bash
# 观察 getUsers 方法的参数和返回值
watch com.github.axinger.service.DemoService getUsers '{params, returnObj}'

# 观察方法执行的耗时
watch com.github.axinger.service.DemoService calculate '{params, returnObj}' '#cost > 100'

# 观察异常信息
watch com.github.axinger.service.DemoService processData '{params, throwExp}' -e
```

参数说明：
- `{params, returnObj}`: 输出参数和返回值
- `#cost > 100`: 只输出耗时超过 100ms 的调用
- `-e`: 只在抛出异常时输出

### 5. trace - 方法调用追踪

追踪方法调用路径和每个节点的耗时：

```bash
# 追踪 getUsers 方法的调用链
trace com.github.axinger.service.DemoService getUsers

# 设置最大深度
trace com.github.axinger.service.DemoService getUsers -n 5 --depth 3

# 只追踪耗时超过 100ms 的调用
trace com.github.axinger.service.DemoService getUsers '#cost > 100'
```

### 6. monitor - 方法监控

统计方法的调用情况（成功次数、失败次数、平均耗时等）：

```bash
# 每 5 秒统计一次
monitor -c 5 com.github.axinger.service.DemoService calculate

# 统计所有方法
monitor -c 5 com.github.axinger.service.DemoService *
```

按 `Ctrl+C` 停止监控。

### 7. stack - 查看调用栈

查看指定方法的调用栈：

```bash
stack com.github.axinger.service.DemoService processData
```

### 8. tt - 时空隧道

记录方法调用的详细信息，可以回放：

```bash
# 记录方法调用
tt -t com.github.axinger.service.DemoService getUsers

# 查看所有记录
tt -l

# 查看指定记录的详情
tt -i <index>

# 重放指定记录
tt -p <index>
```

### 9. sc - 搜索类

搜索 JVM 中已加载的类：

```bash
# 搜索包含 Demo 的类
sc *Demo*

# 查看类的详细信息
sc -d com.github.axinger.service.DemoService
```

### 10. sm - 搜索方法

搜索类的方法：

```bash
# 搜索 DemoService 的所有方法
sm com.github.axinger.service.DemoService

# 搜索特定方法
sm com.github.axinger.service.DemoService get*
```

### 11. vmtool - JVM 工具

从 JVM 中获取对象实例：

```bash
# 获取 DemoService 的实例
vmtool --action getInstances --className com.github.axinger.service.DemoService

# 调用实例方法
vmtool --action invoke --className com.github.axinger.service.DemoService --methodName getUsers --params '[10]'
```

### 12. logger - 查看和修改日志级别

```bash
# 查看所有 logger
logger

# 查看指定 logger
logger -n com.github.axinger

# 修改日志级别
logger -n com.github.axinger --level DEBUG
```

## 实战场景

### 场景 1：排查方法执行缓慢

1. 调用慢操作接口：
```bash
curl http://localhost:15024/api/demo/slow
```

2. 使用 trace 命令追踪：
```bash
trace com.github.axinger.service.DemoService slowOperation
```

3. 分析哪个步骤耗时最长

### 场景 2：定位异常原因

1. 调用可能抛出异常的接口：
```bash
curl -X POST http://localhost:15024/api/demo/process \
  -H "Content-Type: application/json" \
  -d '{"input": ""}'
```

2. 使用 watch 命令观察异常：
```bash
watch com.github.axinger.service.DemoService processData '{params, throwExp}' -e
```

3. 查看异常的详细信息和堆栈

### 场景 3：性能分析

1. 持续调用接口产生负载：
```bash
# 在另一个终端执行
while true; do
  curl http://localhost:15024/api/demo/users?count=100 > /dev/null
done
```

2. 使用 dashboard 查看系统状态：
```bash
dashboard
```

3. 使用 thread 查看最忙的线程：
```bash
thread -n 5
```

4. 使用 monitor 统计方法调用：
```bash
monitor -c 5 com.github.axinger.service.DemoService getUsers
```

### 场景 4：动态修改日志级别

无需重启应用，动态调整日志级别：

```bash
# 查看当前日志级别
logger -n com.github.axinger

# 修改为 DEBUG 级别
logger -n com.github.axinger --level DEBUG

# 验证修改效果（观察控制台输出）

# 恢复为 INFO 级别
logger -n com.github.axinger --level INFO
```

## 配置说明

在 `application.yml` 中可以配置 Arthas：

```yaml
arthas:
  # Telnet 端口
  telnet-port: 3658
  # HTTP 端口（Web Console）
  http-port: 8563
  # 监听地址（127.0.0.1 仅本地访问，0.0.0.0 允许远程访问）
  ip: 127.0.0.1
```

**安全提示**：生产环境建议将 `ip` 设置为 `127.0.0.1`，避免远程访问带来的安全风险。

## 注意事项

1. **生产环境谨慎使用**：Arthas 功能强大，但在生产环境使用时需谨慎，某些命令可能影响性能。

2. **权限控制**：确保只有授权人员可以访问 Arthas。

3. **资源消耗**：某些命令（如 trace、watch）会消耗一定资源，使用后及时停止。

4. **版本兼容**：本项目使用 Arthas 3.7.3，不同版本命令可能略有差异。

5. **Spring Boot 集成**：通过 `arthas-spring-boot-starter` 依赖自动集成，无需手动启动。

## 更多资源

- [Arthas 官方文档](https://arthas.aliyun.com/doc/)
- [Arthas GitHub](https://github.com/alibaba/arthas)
- [Arthas 在线教程](https://arthas.aliyun.com/doc/arthas-tutorials.html)

## 常见问题

**Q: 无法连接 Arthas？**
A: 检查应用是否正常启动，确认端口 3658 和 8563 未被占用。

**Q: Web Console 无法访问？**
A: 确认防火墙设置，检查 `application.yml` 中的 `ip` 配置。

**Q: 命令执行无响应？**
A: 可能是方法未被调用，先触发相应的 API 调用再执行观察命令。

**Q: 如何退出 Arthas？**
A: 在 Telnet 中输入 `stop` 或 `exit`，Web Console 直接关闭浏览器标签即可。

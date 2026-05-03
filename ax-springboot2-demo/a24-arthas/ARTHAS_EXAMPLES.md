# Arthas 实战场景示例

本文档提供了一些常见的 Arthas 使用场景和具体命令示例。

## 场景 1: 监控方法执行时间和参数

### 问题
某个 API 接口响应缓慢，需要定位是哪个方法耗时最长。

### 解决方案

1. **使用 trace 命令追踪调用链**

```bash
# 追踪 DemoController 的所有方法
trace com.github.axinger.controller.DemoController *

# 只追踪 getUsers 方法
trace com.github.axinger.controller.DemoController getUsers

# 设置最大深度为 5，避免输出过多
trace com.github.axinger.service.DemoService getUsers --depth 5

# 只输出耗时超过 100ms 的调用
trace com.github.axinger.service.DemoService getUsers '#cost > 100'
```

2. **使用 watch 观察方法参数和返回值**

```bash
# 观察 getUsers 方法的参数
watch com.github.axinger.service.DemoService getUsers params

# 观察返回值
watch com.github.axinger.service.DemoService getUsers returnObj

# 同时观察参数、返回值和耗时
watch com.github.axinger.service.DemoService getUsers '{params, returnObj, #cost}'

# 只观察耗时超过 200ms 的调用
watch com.github.axinger.service.DemoService getUsers '{params, returnObj, #cost}' '#cost > 200'
```

3. **使用 monitor 统计方法调用情况**

```bash
# 每 3 秒统计一次 getUsers 方法的调用情况
monitor -c 3 com.github.axinger.service.DemoService getUsers

# 统计所有方法
monitor -c 3 com.github.axinger.service.DemoService *
```

## 场景 2: 排查异常问题

### 问题
API 偶尔抛出异常，需要查看异常的详细信息和触发条件。

### 解决方案

1. **使用 watch 捕获异常**

```bash
# 观察 processData 方法的异常
watch com.github.axinger.service.DemoService processData throwExp -e

# 同时观察参数和异常
watch com.github.axinger.service.DemoService processData '{params, throwExp}' -e

# 观察异常的堆栈信息
watch com.github.axinger.service.DemoService processData '{params, throwExp.stackTrace}' -e
```

2. **使用 stack 查看调用栈**

```bash
# 当 processData 被调用时，打印调用栈
stack com.github.axinger.service.DemoService processData

# 限制只显示前 5 层
stack com.github.axinger.service.DemoService processData -n 5
```

3. **使用 tt 记录异常调用**

```bash
# 时空隧道记录方法调用
tt -t com.github.axinger.service.DemoService processData

# 查看所有记录
tt -l

# 查看某次异常调用的详情（假设 index 为 1000）
tt -i 1000

# 重放该次调用
tt -p 1000
```

## 场景 3: 分析 CPU 占用过高

### 问题
应用 CPU 占用率突然升高，需要找出是哪个线程或方法导致的。

### 解决方案

1. **使用 dashboard 查看整体情况**

```bash
# 查看实时数据面板
dashboard
```

关注以下指标：
- CPU 使用率
- 最忙的线程
- GC 情况
- 内存使用情况

2. **使用 thread 命令分析线程**

```bash
# 查看最忙的前 3 个线程
thread -n 3

# 查看指定线程的堆栈（假设线程 ID 为 42）
thread 42

# 查看处于 RUNNABLE 状态的线程
thread -b

# 查找是否有死锁
thread --deadlock
```

3. **使用 profiler 进行性能分析**

```bash
# 开始 CPU 性能分析
profiler start

# 运行一段时间后停止
profiler stop

# 生成火焰图
profiler start --event cpu
# ... 等待一段时间 ...
profiler stop --format html
```

## 场景 4: 内存泄漏排查

### 问题
应用运行一段时间后出现 OutOfMemoryError，需要排查内存泄漏。

### 解决方案

1. **使用 dashboard 监控内存**

```bash
dashboard
```

观察堆内存的使用趋势。

2. **使用 vmtool 查看对象实例**

```bash
# 获取某个类的所有实例
vmtool --action getInstances --className java.util.HashMap

# 限制返回数量
vmtool --action getInstances --className java.util.HashMap --limit 10

# 查看实例的字段
vmtool --action getInstances --className com.github.axinger.model.User --express 'instances[0]'
```

3. **使用 heapdump 导出堆转储**

```bash
# 导出堆转储文件
heapdump /tmp/heapdump.hprof

# 只导出存活对象
heapdump --live /tmp/heapdump.hprof
```

然后使用 MAT (Memory Analyzer Tool) 或 JVisualVM 分析堆转储文件。

## 场景 5: 动态修改代码行为

### 问题
需要在不重启应用的情况下，临时修改某个方法的返回值或行为。

### 解决方案

1. **使用 retransform 热更新类**

```bash
# 首先反编译当前类
jad com.github.axinger.service.DemoService > /tmp/DemoService.java

# 编辑文件，修改代码
# vi /tmp/DemoService.java

# 编译修改后的文件
mc -c <classloader-hash> /tmp/DemoService.java

# 加载新的类定义
retransform /tmp/com/github/axinger/service/DemoService.class
```

2. **使用 ognl 执行表达式**

```bash
# 查看 Spring Bean
ognl '@org.springframework.context.ApplicationContextProvider@getApplicationContext().getBean("demoService")'

# 调用方法
ognl '@com.github.axinger.A24Application@applicationContext.getBean("demoService").getUsers(5)'

# 修改静态字段
ognl '@com.github.axinger.service.DemoService@someStaticField="new value"'
```

## 场景 6: 查看和修改日志级别

### 问题
生产环境需要临时开启 DEBUG 日志来排查问题，但不想重启应用。

### 解决方案

```bash
# 查看所有 logger
logger

# 查看指定包下的 logger
logger -n com.github.axinger

# 修改日志级别为 DEBUG
logger -n com.github.axinger --level DEBUG

# 验证修改是否生效（观察控制台输出）

# 问题排查完成后，恢复为 INFO
logger -n com.github.axinger --level INFO
```

## 场景 7: 监控数据库查询

### 问题
需要监控所有的数据库查询操作，找出慢查询。

### 解决方案

假设使用 MyBatis：

```bash
# 追踪 Mapper 方法
trace com.github.axinger.mapper.* *

# 观察 SQL 执行参数
watch com.github.axinger.mapper.UserMapper selectList '{params, #cost}'

# 监控执行时间超过 1 秒的查询
watch com.github.axinger.mapper.* * '{params, #cost}' '#cost > 1000'
```

## 场景 8: 查看类的实际加载版本

### 问题
怀疑类路径中存在多个版本的同一个类，需要确认实际加载的是哪个版本。

### 解决方案

```bash
# 搜索类
sc -d com.github.axinger.service.DemoService

# 查看类的 CodeSource
sc -d com.github.axinger.service.DemoService | grep code-source

# 反编译类，查看实际代码
jad com.github.axinger.service.DemoService
```

## 常用命令速查表

| 命令 | 用途 | 示例 |
|------|------|------|
| `dashboard` | 实时数据面板 | `dashboard` |
| `thread` | 线程信息 | `thread -n 3` |
| `jad` | 反编译类 | `jad com.example.Service` |
| `watch` | 观察方法执行 | `watch com.example.Service method '{params, returnObj}'` |
| `trace` | 方法调用追踪 | `trace com.example.Service method` |
| `monitor` | 方法监控统计 | `monitor -c 5 com.example.Service method` |
| `stack` | 调用栈 | `stack com.example.Service method` |
| `tt` | 时空隧道 | `tt -t com.example.Service method` |
| `sc` | 搜索类 | `sc *Demo*` |
| `sm` | 搜索方法 | `sm com.example.Service *` |
| `vmtool` | JVM 工具 | `vmtool --action getInstances --className java.lang.String` |
| `logger` | 日志管理 | `logger -n com.example --level DEBUG` |
| `heapdump` | 堆转储 | `heapdump /tmp/dump.hprof` |
| `profiler` | 性能分析 | `profiler start` |
| `ognl` | 执行表达式 | `ognl '@java.lang.System@out.println("hello")'` |

## 实用技巧

### 1. 组合使用命令

```bash
# 先找到最忙的线程
thread -n 1

# 然后追踪该线程正在执行的方法
trace <class> <method>
```

### 2. 使用条件表达式过滤

```bash
# 只观察特定参数的调用
watch com.example.Service method '{params, returnObj}' 'params[0] > 100'

# 只观察返回值为 null 的调用
watch com.example.Service method '{params, returnObj}' 'returnObj == null'
```

### 3. 保存输出结果

在 Web Console 中，可以复制输出内容保存为文件进行分析。

### 4. 定期清理记录

使用 tt 命令后，记得清理记录：

```bash
# 删除所有记录
tt --delete-all

# 删除指定记录
tt --delete <index>
```

## 注意事项

1. **性能影响**：trace、watch 等命令会影响性能，生产环境谨慎使用
2. **及时停止**：使用完后及时按 Ctrl+C 停止监控
3. **权限控制**：确保只有授权人员可以访问 Arthas
4. **备份重要数据**：使用 retransform 前先备份原类
5. **测试环境验证**：新命令先在测试环境验证，再在生产环境使用

## 参考资料

- [Arthas 官方文档](https://arthas.aliyun.com/doc/)
- [Arthas GitHub](https://github.com/alibaba/arthas)
- [Arthas 用户问卷](https://survey.alibaba.com/apps/zhiliao/arthas)

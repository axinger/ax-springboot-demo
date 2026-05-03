# Arthas 快速参考卡片

## 启动和连接

```bash
# 启动应用
mvn spring-boot:run

# Web Console
浏览器访问: http://127.0.0.1:8563

# Telnet
telnet 127.0.0.1 3658
```

## 核心命令速查

### 系统监控

```bash
dashboard              # 实时数据面板
thread                 # 查看所有线程
thread -n 3           # 查看最忙的3个线程
thread --deadlock     # 检查死锁
vmoption              # 查看 JVM 参数
sysprop               # 查看系统属性
sysenv                # 查看环境变量
```

### 类和方法

```bash
sc *Demo*             # 搜索类
sm com.example.Service  # 搜索方法
jad com.example.Service  # 反编译类
classloader           # 查看类加载器
getstatic com.example.Class field  # 查看静态字段
```

### 方法追踪

```bash
# 观察方法执行
watch com.example.Service method '{params, returnObj}'
watch com.example.Service method '{params, throwExp}' -e  # 只观察异常

# 追踪调用链
trace com.example.Service method
trace com.example.Service method '#cost > 100'  # 只追踪慢调用

# 监控统计
monitor -c 5 com.example.Service method  # 每5秒统计一次

# 调用栈
stack com.example.Service method
```

### 时空隧道

```bash
tt -t com.example.Service method   # 记录方法调用
tt -l                              # 查看所有记录
tt -i 1000                         # 查看指定记录详情
tt -p 1000                         # 重放指定记录
tt --delete-all                    # 删除所有记录
```

### JVM 工具

```bash
# 获取对象实例
vmtool --action getInstances --className java.lang.String

# 调用方法
vmtool --action invoke --className com.example.Service --methodName method

# 堆转储
heapdump /tmp/dump.hprof
heapdump --live /tmp/dump.hprof  # 只导出存活对象

# 性能分析
profiler start
profiler stop
profiler stop --format html  # 生成火焰图
```

### 日志管理

```bash
logger                    # 查看所有 logger
logger -n com.example     # 查看指定 logger
logger -n com.example --level DEBUG  # 修改日志级别
```

### OGNL 表达式

```bash
# 执行 Java 代码
ognl '@java.lang.System@out.println("hello")'

# 调用 Spring Bean
ognl '@com.example.Application@context.getBean("service").method()'

# 查看字段
ognl '@com.example.Class@field'
```

### 热更新

```bash
# 反编译
jad com.example.Service > /tmp/Service.java

# 编译
mc -c <classloader-hash> /tmp/Service.java

# 加载
retransform /tmp/com/example/Service.class
```

## 常用条件表达式

```bash
# 耗时超过 100ms
'#cost > 100'

# 参数大于 10
'params[0] > 10'

# 返回值为 null
'returnObj == null'

# 抛出异常
'throwExp != null'

# 字符串匹配
'params[0].equals("test")'
```

## Watch 输出格式

```bash
# 基本格式
'{params, returnObj}'

# 包含耗时
'{params, returnObj, #cost}'

# 包含异常
'{params, throwExp}'

# 完整信息
'{params, returnObj, throwExp, #cost}'

# 格式化输出
'{params, returnObj, #cost}' -x 3  # 展开3层
```

## Trace 选项

```bash
# 设置深度
trace com.example.Service method --depth 3

# 限制次数
trace com.example.Service method -n 5

# 条件过滤
trace com.example.Service method '#cost > 100'

# 排除子类
trace -E com.example.Service method
```

## 实用组合

### 性能分析流程
```bash
# 1. 查看整体情况
dashboard

# 2. 找出最忙线程
thread -n 3

# 3. 追踪方法调用
trace com.example.Service slowMethod

# 4. 观察参数和耗时
watch com.example.Service slowMethod '{params, #cost}'
```

### 异常排查流程
```bash
# 1. 捕获异常
watch com.example.Service method throwExp -e

# 2. 查看调用栈
stack com.example.Service method

# 3. 记录调用
tt -t com.example.Service method

# 4. 重放问题
tt -p <index>
```

### 内存泄漏排查
```bash
# 1. 监控内存
dashboard

# 2. 查看对象实例
vmtool --action getInstances --className com.example.Class

# 3. 导出堆转储
heapdump --live /tmp/dump.hprof

# 4. 分析（使用 MAT）
```

## API 测试

```bash
# 获取用户列表
curl http://localhost:15024/api/demo/users?count=10

# 执行计算
curl http://localhost:15024/api/demo/calculate?a=10&b=20

# 处理数据
curl -X POST http://localhost:15024/api/demo/process \
  -H "Content-Type: application/json" \
  -d '{"input": "hello"}'

# 慢操作
curl http://localhost:15024/api/demo/slow

# Fibonacci
curl http://localhost:15024/api/demo/fibonacci?n=15

# 系统信息
curl http://localhost:15024/api/demo/system-info
```

## 快捷键

```bash
Ctrl + C    # 停止当前命令
q           # 退出 dashboard
Tab         # 自动补全
history     # 查看历史命令
help        # 帮助信息
stop        # 停止 Arthas
exit        # 退出 Arthas
```

## 注意事项

⚠️ **生产环境谨慎使用**
- trace/watch 会影响性能
- 使用后及时停止监控
- 不要长时间开启 profiler

⚠️ **安全第一**
- 限制访问 IP 为 127.0.0.1
- 不要暴露到公网
- 定期更新 Arthas 版本

✅ **最佳实践**
- 先在测试环境验证
- 记录重要的诊断结果
- 定期清理 tt 记录
- 结合日志一起分析

## 更多信息

- 📖 完整文档: README.md
- 💡 实战示例: ARTHAS_EXAMPLES.md
- 🌐 官方文档: https://arthas.aliyun.com/doc/
- 💻 GitHub: https://github.com/alibaba/arthas

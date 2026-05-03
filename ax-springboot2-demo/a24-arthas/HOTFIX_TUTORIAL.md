# Arthas 热更新代码实战教程

本教程演示如何使用 Arthas 在线修复生产环境中的 bug，无需重启应用。

## 📋 场景说明

我们在 `DemoService` 中故意写了一个有 bug 的方法 `calculateOrderTotal`：

### 🐛 Bug 描述

```java
public double calculateOrderTotal(List<Double> prices, double discount) {
    log.info("计算订单总价，商品数量: {}, 折扣: {}", prices.size(), discount);
    
    double total = 0;
    // Bug 1: 没有检查 prices 是否为 null，会抛出 NullPointerException
    for (Double price : prices) {
        total += price;
    }
    
    // Bug 2: 折扣计算错误，应该是乘以 (1 - discount)，而不是直接减去 discount
    total = total - discount;
    
    log.info("订单总价: {}", total);
    return total;
}
```

### ❌ 存在的问题

1. **空指针异常**：当 `prices` 为 `null` 时，`prices.size()` 会抛出 `NullPointerException`
2. **折扣计算错误**：应该使用 `total * (1 - discount)`，而不是 `total - discount`
   - 例如：总价 100 元，折扣 0.2（8折）
   - 错误计算：100 - 0.2 = 99.8 ❌
   - 正确计算：100 * (1 - 0.2) = 80 ✅

## 🚀 完整操作流程

### 第一步：启动应用并复现 Bug

#### 1.1 启动应用

```bash
cd D:\code\ax-springboot-demo\ax-springboot2-demo\a24-arthas
start.bat
```

或者：

```bash
mvn spring-boot:run
```

#### 1.2 测试有 Bug 的接口

**测试 1：正常调用（但计算结果错误）**

```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": [100, 200, 300], "discount": 0.2}'
```

预期返回（错误结果）：
```json
{
  "code": 200,
  "data": 599.8,  // 错误！应该是 480
  "message": "计算成功"
}
```

**测试 2：传入 null 值（触发空指针异常）**

```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": null, "discount": 0.2}'
```

预期返回（异常）：
```json
{
  "code": 500,
  "message": "计算失败: null",
  "errorType": "NullPointerException"
}
```

### 第二步：使用 Arthas 诊断问题

#### 2.1 连接 Arthas

打开浏览器访问：http://127.0.0.1:8563

或使用 Telnet：
```bash
telnet 127.0.0.1 3658
```

#### 2.2 查看方法代码（反编译）

```bash
jad com.github.axinger.service.DemoService calculateOrderTotal
```

输出示例：
```java
public double calculateOrderTotal(List<Double> prices, double discount) {
    log.info("计算订单总价，商品数量: {}, 折扣: {}", prices.size(), discount);
    double total = 0.0;
    Iterator var5 = prices.iterator();
    while(var5.hasNext()) {
        Double price = (Double)var5.next();
        total += price;
    }
    total = total - discount;  // ← 这里有问题！
    log.info("订单总价: {}", total);
    return total;
}
```

#### 2.3 观察异常信息

先调用接口触发异常，然后在 Arthas 中执行：

```bash
watch com.github.axinger.service.DemoService calculateOrderTotal '{params, throwExp}' -e
```

然后再次调用接口：
```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": null, "discount": 0.2}'
```

Arthas 会输出异常详情：
```
method=com.github.axinger.service.DemoService.calculateOrderTotal location=AtExceptionExit
ts=2026-05-03 10:30:45; [cost=0.523ms] result=@ArrayList[
    @Object[][
        @ArrayList[null],  // params[0] = null
        @Double[0.2],      // params[1] = 0.2
    ],
    @NullPointerException[java.lang.NullPointerException],  // 异常
]
```

#### 2.4 追踪方法调用栈

```bash
stack com.github.axinger.service.DemoService calculateOrderTotal
```

可以看到完整的调用链路：
```
ts=2026-05-03 10:30:45;thread_name=http-nio-15024-exec-1;id=1a;is_daemon=true;priority=5;
    @com.github.axinger.controller.DemoController.calculateOrderTotal()
    at com.github.axinger.service.DemoService.calculateOrderTotal(DemoService.java:110)
    at com.github.axinger.controller.DemoController.calculateOrderTotal(DemoController.java:172)
```

### 第三步：准备修复代码

#### 3.1 导出当前类文件

在 Arthas 中执行：

```bash
jad com.github.axinger.service.DemoService > /tmp/DemoService.java
```

Windows 系统可以保存到项目目录：

```bash
jad com.github.axinger.service.DemoService > D:\code\ax-springboot-demo\ax-springboot2-demo\a24-arthas\DemoService_temp.java
```

#### 3.2 编辑修复代码

打开导出的文件，找到 `calculateOrderTotal` 方法，替换为修复后的版本：

```java
/**
 * 【已修复】计算订单总价
 * 修复内容:
 * 1. 添加了空指针检查
 * 2. 修正了折扣计算公式
 * 3. 添加了参数验证
 */
public double calculateOrderTotal(List<Double> prices, double discount) {
    log.info("计算订单总价，商品数量: {}, 折扣: {}", 
            prices != null ? prices.size() : 0, discount);
    
    // 修复 1: 添加空指针检查
    if (prices == null || prices.isEmpty()) {
        log.warn("商品价格列表为空，返回 0");
        return 0.0;
    }
    
    // 修复 2: 验证折扣范围（0-1之间）
    if (discount < 0 || discount > 1) {
        log.warn("折扣范围无效: {}，应在 0-1 之间", discount);
        throw new IllegalArgumentException("折扣必须在 0-1 之间");
    }
    
    double total = 0;
    for (Double price : prices) {
        if (price != null && price > 0) {
            total += price;
        }
    }
    
    // 修复 3: 正确的折扣计算 - 乘以 (1 - discount)
    total = total * (1 - discount);
    
    // 确保价格不为负数
    total = Math.max(0, total);
    
    log.info("订单总价: {}", total);
    return total;
}
```

保存文件。

### 第四步：编译和加载新代码

#### 4.1 获取 ClassLoader Hash

在 Arthas 中执行：

```bash
sc -d com.github.axinger.service.DemoService | grep classLoaderHash
```

输出示例：
```
classLoaderHash    1a2b3c4d
```

记住这个 hash 值（例如：`1a2b3c4d`）。

#### 4.2 编译修改后的代码

在 Arthas 中执行（替换 `<hash>` 为实际的 hash 值）：

```bash
mc -c 1a2b3c4d /tmp/DemoService.java -d /tmp
```

Windows 系统：

```bash
mc -c 1a2b3c4d D:\code\ax-springboot-demo\ax-springboot2-demo\a24-arthas\DemoService_temp.java -d D:\code\ax-springboot-demo\ax-springboot2-demo\a24-arthas
```

如果编译成功，会输出：
```
Memory compiler output:
/tmp/com/github/axinger/service/DemoService.class
```

#### 4.3 加载新类定义

在 Arthas 中执行：

```bash
retransform /tmp/com/github/axinger/service/DemoService.class
```

Windows 系统：

```bash
retransform D:\code\ax-springboot-demo\ax-springboot2-demo\a24-arthas\com\github\axinger\service\DemoService.class
```

如果成功，会输出：
```
retransform success, size: 1
```

### 第五步：验证修复效果

#### 5.1 再次反编译验证

```bash
jad com.github.axinger.service.DemoService calculateOrderTotal
```

应该看到修复后的代码，包含空指针检查和正确的折扣计算。

#### 5.2 测试修复后的接口

**测试 1：正常调用（验证计算结果）**

```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": [100, 200, 300], "discount": 0.2}'
```

预期返回（正确结果）：
```json
{
  "code": 200,
  "data": 480.0,  // ✅ 正确！(100+200+300) * (1-0.2) = 480
  "message": "计算成功"
}
```

**测试 2：传入 null 值（验证空指针处理）**

```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": null, "discount": 0.2}'
```

预期返回（不再抛出异常）：
```json
{
  "code": 200,
  "data": 0.0,  // ✅ 正确处理，返回 0
  "message": "计算成功"
}
```

**测试 3：传入空列表**

```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": [], "discount": 0.2}'
```

预期返回：
```json
{
  "code": 200,
  "data": 0.0,
  "message": "计算成功"
}
```

**测试 4：验证折扣范围检查**

```bash
curl -X POST http://localhost:15024/api/demo/order-total \
  -H "Content-Type: application/json" \
  -d '{"prices": [100, 200], "discount": 1.5}'
```

预期返回（折扣超出范围）：
```json
{
  "code": 500,
  "message": "计算失败: 折扣必须在 0-1 之间",
  "errorType": "IllegalArgumentException"
}
```

### 第六步：观察日志变化

在应用控制台，应该能看到修复后的日志输出：

```
2026-05-03 10:35:20 INFO  - 计算订单总价，商品数量: 3, 折扣: 0.2
2026-05-03 10:35:20 INFO  - 订单总价: 480.0

2026-05-03 10:35:25 WARN  - 商品价格列表为空，返回 0
```

## 🎯 关键命令总结

```bash
# 1. 反编译查看当前代码
jad com.github.axinger.service.DemoService calculateOrderTotal

# 2. 导出完整类文件
jad com.github.axinger.service.DemoService > /tmp/DemoService.java

# 3. 获取 ClassLoader Hash
sc -d com.github.axinger.service.DemoService | grep classLoaderHash

# 4. 编译修改后的代码
mc -c <hash> /tmp/DemoService.java -d /tmp

# 5. 加载新类
retransform /tmp/com/github/axinger/service/DemoService.class

# 6. 验证修复
jad com.github.axinger.service.DemoService calculateOrderTotal
```

## ⚠️ 注意事项

### 1. 兼容性限制

`retransform` 不能修改以下内容：
- ❌ 类的字段（添加、删除、修改）
- ❌ 类的方法签名（添加、删除方法）
- ❌ 类的继承关系
- ✅ 可以修改方法内部的实现逻辑

### 2. 临时性

- `retransform` 的修改是**临时的**
- 重启应用后会恢复原始代码
- 需要同时修改源代码并重新部署

### 3. 安全性

- 在生产环境使用前，务必在测试环境验证
- 备份原始代码
- 记录所有修改操作

### 4. 性能影响

- 编译和加载过程会短暂影响性能
- 建议在低峰期操作
- 避免频繁热更新

## 🔧 常见问题

### Q1: 编译失败怎么办？

**原因**：代码语法错误或依赖缺失

**解决**：
```bash
# 查看详细错误信息
mc -c <hash> /tmp/DemoService.java -d /tmp

# 确保导入语句完整
# 检查是否有未定义的变量或方法
```

### Q2: retransform 失败怎么办？

**原因**：修改不兼容（如修改了方法签名）

**解决**：
- 只修改方法内部逻辑
- 不要添加或删除方法
- 不要修改字段

### Q3: 如何撤销修改？

**方法 1**：重启应用（会恢复原始代码）

**方法 2**：再次 retransform 原始版本的 class 文件

### Q4: Windows 系统路径问题？

**解决**：使用正斜杠或双反斜杠
```bash
# 方式 1：正斜杠
jad com.example.Class > C:/temp/Class.java

# 方式 2：双反斜杠
jad com.example.Class > C:\\temp\\Class.java
```

## 📝 实战练习

尝试修复以下 bug：

1. **练习 1**：修复 `processData` 方法，移除随机异常
2. **练习 2**：优化 `fibonacci` 方法，添加缓存避免重复计算
3. **练习 3**：在 `getUsers` 方法中添加分页参数验证

## 🎓 学习要点

通过本教程，你应该掌握：

1. ✅ 如何使用 `jad` 反编译查看代码
2. ✅ 如何使用 `watch` 观察方法执行和异常
3. ✅ 如何使用 `stack` 查看调用栈
4. ✅ 如何使用 `mc` 编译 Java 代码
5. ✅ 如何使用 `retransform` 热更新类
6. ✅ 热更新的限制和注意事项

## 📚 相关资源

- [Arthas 官方文档 - retransform](https://arthas.aliyun.com/doc/retransform.html)
- [Arthas 官方文档 - mc](https://arthas.aliyun.com/doc/mc.html)
- [Arthas 官方文档 - jad](https://arthas.aliyun.com/doc/jad.html)

---

**提示**：本教程中的所有代码和文件都已包含在项目中，可以直接实践操作！

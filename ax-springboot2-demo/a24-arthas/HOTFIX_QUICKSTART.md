# Arthas 热更新 - 快速操作指南

## 🎯 目标

在线修复 `DemoService.calculateOrderTotal` 方法的 bug，无需重启应用。

## 📝 Bug 说明

**原始代码问题**：
1. 空指针异常：`prices.size()` 在 prices 为 null 时崩溃
2. 折扣计算错误：使用 `total - discount` 而非 `total * (1 - discount)`

## ⚡ 5 分钟快速修复流程

### 步骤 1：启动应用（如果还未启动）

```bash
cd D:\code\ax-springboot-demo\ax-springboot2-demo\a24-arthas
start.bat
```

### 步骤 2：复现 Bug

在 IDE 中打开 [demo.http](file:///D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/demo.http)，运行请求 #12：

```http
POST http://localhost:15024/api/demo/order-total
Content-Type: application/json

{
  "prices": null,
  "discount": 0.2
}
```

会收到 500 错误：`NullPointerException`

### 步骤 3：连接 Arthas

浏览器访问：http://127.0.0.1:8563

### 步骤 4：查看当前代码

在 Arthas Console 中执行：

```bash
jad com.github.axinger.service.DemoService calculateOrderTotal
```

可以看到有 bug 的代码。

### 步骤 5：导出类文件

```bash
jad com.github.axinger.service.DemoService > D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/DemoService_temp.java
```

### 步骤 6：获取 ClassLoader Hash

```bash
sc -d com.github.axinger.service.DemoService | grep classLoaderHash
```

记住输出的 hash 值，例如：`1a2b3c4d`

### 步骤 7：编辑修复代码

打开 `DemoService_temp.java`，找到 `calculateOrderTotal` 方法，替换为：

```java
public double calculateOrderTotal(List<Double> prices, double discount) {
    log.info("计算订单总价，商品数量: {}, 折扣: {}", 
            prices != null ? prices.size() : 0, discount);
    
    if (prices == null || prices.isEmpty()) {
        log.warn("商品价格列表为空，返回 0");
        return 0.0;
    }
    
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
    
    total = total * (1 - discount);
    total = Math.max(0, total);
    
    log.info("订单总价: {}", total);
    return total;
}
```

保存文件。

### 步骤 8：编译新代码

在 Arthas 中执行（替换 `<hash>` 为实际的 hash 值）：

```bash
mc -c <hash> D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/DemoService_temp.java -d D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas
```

### 步骤 9：加载新类

```bash
retransform D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/com/github/axinger/service/DemoService.class
```

看到 `retransform success` 表示成功！

### 步骤 10：验证修复

再次运行 demo.http 中的请求 #12，现在应该返回：

```json
{
  "code": 200,
  "data": 0.0,
  "message": "计算成功"
}
```

不再抛出异常！✅

再测试请求 #11，验证计算结果：

```json
{
  "code": 200,
  "data": 480.0,  // 正确！(100+200+300) * 0.8 = 480
  "message": "计算成功"
}
```

## 🎉 完成！

你已经成功使用 Arthas 在线修复了生产环境的 bug，无需重启应用！

## 📌 关键命令速查

```bash
# 1. 反编译
jad com.github.axinger.service.DemoService calculateOrderTotal

# 2. 导出
jad com.github.axinger.service.DemoService > /path/to/DemoService.java

# 3. 获取 hash
sc -d com.github.axinger.service.DemoService | grep classLoaderHash

# 4. 编译
mc -c <hash> /path/to/DemoService.java -d /path/to/output

# 5. 加载
retransform /path/to/DemoService.class
```

## 🔗 相关文档

- 📖 详细教程：[HOTFIX_TUTORIAL.md](file:///D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/HOTFIX_TUTORIAL.md)
- 📝 修复代码参考：[fix-calculateOrderTotal.txt](file:///D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/fix-calculateOrderTotal.txt)
- ✅ 完整示例：[DemoServiceFixed.java](file:///D:/code/ax-springboot-demo/ax-springboot2-demo/a24-arthas/DemoServiceFixed.java)

## ⚠️ 重要提醒

1. **临时性**：retransform 的修改在重启后会丢失，记得同时修改源代码
2. **兼容性**：只能修改方法内部逻辑，不能添加/删除方法或字段
3. **安全性**：生产环境使用前务必在测试环境验证
4. **备份**：操作前备份原始代码

---

**提示**：如需撤销修改，只需重启应用即可恢复原始代码。

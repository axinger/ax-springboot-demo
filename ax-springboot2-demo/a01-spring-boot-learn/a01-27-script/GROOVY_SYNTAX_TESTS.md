# Groovy 常用语法测试总结

本文档总结了在 `GroovyTests.java` 中测试的各种 Groovy 常用语法特性。

## 测试概览

共包含 15 个测试用例，涵盖了 Groovy 的核心语法和功能：

### 1. 基本变量和字符串操作 (`testBasicVariablesAndStrings`)
- 变量声明（def 关键字）
- 字符串插值（`${variable}`）
- 多行字符串（三引号 `'''` 或 `"""`）
- 字符串_trim()_方法

### 2. List 集合操作 (`testListOperations`)
- 创建列表：`[1, 2, 3, 4, 5]`
- 过滤：`findAll { it % 2 == 0 }`
- 映射：`collect { it * 2 }`
- 查找：`find { it > 5 }`
- 排序：`sort { a, b -> b <=> a }`
- 分组：`groupBy { it % 2 == 0 ? 'even' : 'odd' }`

### 3. Map 集合操作 (`testMapOperations`)
- 创建 Map：`[name: 'Alice', age: 30]`
- 属性访问：`person.name` 或 `person['age']`
- 添加属性：`person.email = 'alice@example.com'`
- 遍历：`each { key, value -> ... }`
- 过滤：`findAll { key, value -> ... }`

### 4. 闭包和高阶函数 (`testClosuresAndHigherOrderFunctions`)
- 定义闭包：`{ a, b -> a * b }`
- 调用闭包：`multiply(5, 6)`
- inject/reduce：`inject(0) { acc, val -> acc + val }`
- 链式调用：`.findAll{}.collect{}.sum()`

### 5. 条件语句和循环 (`testConditionalsAndLoops`)
- if-else 语句
- switch-case 语句
- for 循环：`for (int i = 1; i <= 5; i++)`
- while 循环
- 阶乘计算示例

### 6. 异常处理 (`testExceptionHandling`)
- try-catch-finally 块
- IndexOutOfBoundsException 捕获
- 安全导航操作符：`obj?.property`
- Elvis 运算符：`?: '默认值'`

### 7. 正则表达式 (`testRegularExpressions`)
- 模式匹配：`~/\\b\\w{4}\\b/`
- 查找所有匹配：`findAll(pattern)`
- 替换：`replaceAll(/fox|dog/, 'animal')`
- 查找第一个匹配：`find(/\\b\\w{5}\\b/)`
- 分割字符串：`split(/\\s+/)`

### 8. 日期和时间处理 (`testDateAndTime`)
- LocalDateTime 和 LocalDate
- 日期格式化：`DateTimeFormatter`
- 日期计算：`plusDays()`, `plusWeeks()`, `minusMonths()`
- 日期比较：`isAfter()`

### 9. 文件操作模拟 (`testFileOperationsSimulation`)
- 多行文本处理
- readLines() 方法
- 查找包含特定内容的行
- 文本转换（大写）
- eachWithIndex 带索引遍历

### 10. JSON 处理 (`testJsonProcessing`)
- JsonSlurper 解析 JSON
- JsonOutput 生成 JSON
- 访问 JSON 属性
- prettyPrint 格式化输出

### 11. 类和对象 (`testClassesAndObjects`)
- 定义类：`class Person { ... }`
- 属性和方法
- 创建对象：`new Person(name: 'Charlie', ...)`
- 方法调用
- 动态属性访问：`person[propName]`

### 12. 范围(Range)操作 (`testRangeOperations`)
- 数字范围：`1..10`（包含）, `1..<10`（不包含右端）
- 递减范围：`10..1`
- 字符范围：`'a'..'z'`
- 范围操作：sum(), findAll(), collect()
- 范围迭代：`(1..5).each { }`

### 13. Elvis 运算符和安全导航 (`testElvisAndSafeNavigation`)
- Elvis 运算符：`name ?: 'Anonymous'`
- 安全导航：`user?.address?.city`
- 组合使用处理 null 值
- 嵌套对象的安全访问

### 14. GroovyShell 精度计算 (`calculateWithShell11`)
- 直接使用 GroovyShell 执行脚本
- 返回 Map 结果
- 展示 Groovy 的数值计算

### 15. GroovyUtils 精度计算 (`calculateWithShell12`)
- 使用封装的 GroovyUtils 执行
- 对比 double 和 BigDecimal 的精度差异
- double: 0.1 + 0.2 = 0.30000000000000004 ❌
- BigDecimal: 0.1 + 0.2 = 0.3 ✅

## Groovy 核心特性总结

### 1. 简洁的语法
- 可选的分号
- def 关键字声明变量
- 自动类型推断

### 2. 强大的集合操作
- List 和 Map 的字面量语法
- 丰富的闭包方法（findAll, collect, find, each, etc.）
- 链式调用支持

### 3. 闭包（Closures）
- 一等公民
- 可以作为参数传递
- 支持高阶函数

### 4. 空安全
- 安全导航操作符 `?.`
- Elvis 运算符 `?:`
- 避免 NullPointerException

### 5. 字符串插值
- GString 支持 `${variable}`
- 多行字符串支持

### 6. 动态特性
- 运行时添加属性和方法
- 动态属性访问

### 7. 运算符重载
- `<=>` 比较运算符
- `<<` 追加运算符
- 范围运算符 `..`

## 运行测试

```bash
cd D:\code\ax-springboot-demo\ax-springboot2-demo\a01-spring-boot-learn\a01-27-script
mvn test -Dtest=GroovyTests
```

## 测试结果

✅ 所有 15 个测试用例全部通过

```
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 注意事项

1. **精度问题**：使用 BigDecimal 而非 double 进行精确计算
2. **方法名称**：Groovy 中使用 `eachWithIndex` 而非 `collectWithIndex`
3. **线程安全**：GroovyUtils 使用了缓存机制，Binding 每次新建保证线程安全
4. **性能优化**：编译后的 Script 被缓存，避免重复解析

## 适用场景

- 动态规则引擎
- 业务逻辑脚本化
- 配置表达式
- 数据转换和处理
- 快速原型开发

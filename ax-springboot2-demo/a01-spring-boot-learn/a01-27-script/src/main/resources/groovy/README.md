# Groovy 脚本示例集合

本目录包含了各种 Groovy 语法和功能的示例脚本，每个脚本都展示了特定的 Groovy 特性。

## 📁 文件列表

### 基础语法
- **01-basic-variables.groovy** - 基本变量和字符串操作
  - 变量声明（def关键字）
  - 字符串插值
  - 多行字符串
  - 字符串方法

- **02-list-operations.groovy** - List集合操作
  - 创建和访问列表
  - 过滤、映射、查找
  - 排序、分组、去重
  - 链式调用
  - inject/reduce操作

- **03-map-operations.groovy** - Map集合操作
  - 创建和访问Map
  - 添加、修改、删除属性
  - 遍历和过滤
  - Map合并
  - 嵌套Map

### 高级特性
- **04-closures.groovy** - 闭包和高阶函数
  - 闭包定义和调用
  - 闭包作为参数
  - 链式调用
  - 柯里化（Curry）
  - 闭包委托

- **05-conditionals-loops.groovy** - 条件语句和循环
  - if-else语句
  - switch-case语句
  - for/while/do-while循环
  - each遍历
  - times和step循环
  - break和continue

- **06-exception-handling.groovy** - 异常处理和安全操作符
  - try-catch-finally
  - 自定义异常
  - 安全导航操作符（?.）
  - Elvis运算符（?:）
  - 展开操作符（*.）

### 实用功能
- **07-regex.groovy** - 正则表达式
  - 基本匹配和查找
  - 邮箱、手机号、身份证验证
  - URL和日期格式验证
  - 分组和捕获
  - 字符串分割和替换

- **08-date-time.groovy** - 日期和时间处理
  - LocalDate、LocalDateTime、LocalTime
  - 日期格式化
  - 日期加减计算
  - 日期比较
  - 时区处理
  - 年龄计算、工作日判断

- **09-json-processing.groovy** - JSON处理
  - JsonSlurper解析JSON
  - JsonOutput生成JSON
  - 嵌套JSON处理
  - JSON数组操作
  - 数据过滤和转换
  - 统计和分组

- **10-classes-objects.groovy** - 类和对象
  - 基本类定义
  - 属性和访问控制
  - 继承和多态
  - 接口和特质（Trait）
  - 抽象类
  - 枚举
  - 动态特性

## 🚀 如何运行

### 方法1：在IDE中运行
1. 在 IntelliJ IDEA 或其他支持 Groovy 的 IDE 中打开任意 `.groovy` 文件
2. 右键点击文件，选择 "Run" 或按快捷键运行
3. IDE 会提供语法高亮、代码补全和错误检查

### 方法2：使用命令行
```bash
# 确保已安装 Groovy
groovy --version

# 运行脚本
groovy src/main/resources/groovy/01-basic-variables.groovy
```

### 方法3：在Java代码中使用
```java
import com.github.axinger.groovy.GroovyUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

// 读取脚本文件
String script = new String(Files.readAllBytes(
    Paths.get("src/main/resources/groovy/01-basic-variables.groovy")
));

// 执行脚本
Object result = GroovyUtils.execute(script, new HashMap<>());
System.out.println(result);
```

## 📝 脚本特点

### ✅ 完整注释
每个脚本都包含详细的中文注释，解释每段代码的功能。

### ✅ 实际示例
所有示例都是实际开发中常见的场景，可以直接应用到项目中。

### ✅ 渐进式学习
从基础到高级，循序渐进地展示 Groovy 的各种特性。

### ✅ 可独立运行
每个脚本都可以独立运行，不依赖其他脚本。

## 🎯 学习建议

1. **初学者**：从 `01-basic-variables.groovy` 开始，逐步学习
2. **有经验的开发者**：直接跳转到感兴趣的主题
3. **实践练习**：修改脚本中的示例，尝试不同的场景
4. **参考文档**：结合 [GroovyTests.java](../../../../../test/java/com/github/axinger/GroovyTests.java) 中的单元测试一起学习

## 🔗 相关资源

- [Groovy 官方文档](https://groovy-lang.org/documentation.html)
- [Groovy API 参考](https://docs.groovy-lang.org/latest/html/api/)
- [项目测试文件](../../../../../test/java/com/github/axinger/GroovyTests.java)

## 💡 提示

- 这些脚本文件会被 IDE 识别为 Groovy 文件，提供完整的语法提示
- 可以随时修改和实验这些脚本
- 建议将常用的代码片段保存为自己的代码库

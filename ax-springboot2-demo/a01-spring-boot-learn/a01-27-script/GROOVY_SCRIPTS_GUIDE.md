# Groovy 脚本文件使用指南

## 📂 目录结构

```
src/main/resources/
├── groovy/                          # Groovy 脚本目录
│   ├── 01-basic-variables.groovy   # 基本变量和字符串
│   ├── 02-list-operations.groovy   # List 集合操作
│   ├── 03-map-operations.groovy    # Map 集合操作
│   ├── 04-closures.groovy          # 闭包和高阶函数
│   ├── 05-conditionals-loops.groovy # 条件语句和循环
│   ├── 06-exception-handling.groovy # 异常处理和安全操作符
│   ├── 07-regex.groovy             # 正则表达式
│   ├── 08-date-time.groovy         # 日期和时间处理
│   ├── 09-json-processing.groovy   # JSON 处理
│   ├── 10-classes-objects.groovy   # 类和对象
│   └── README.md                   # 详细说明文档
├── js/                             # JavaScript 脚本目录
├── py/                             # Python 脚本目录
└── examples/                       # 其他示例
```

## ✅ IDE 语法提示支持

### IntelliJ IDEA
1. **自动识别**：`.groovy` 文件会被 IDEA 自动识别为 Groovy 文件
2. **语法高亮**：完整的语法高亮支持
3. **代码补全**：智能代码补全和提示
4. **错误检查**：实时错误检查和警告
5. **运行配置**：可以直接右键运行脚本

### VS Code
1. 安装 **Groovy** 扩展
2. 打开 `.groovy` 文件即可获得语法支持
3. 支持代码补全、语法检查等功能

### Eclipse
1. 安装 **Groovy Development Tools**
2. 项目会自动识别 Groovy 文件
3. 提供完整的 IDE 支持

## 🚀 使用方法

### 方法 1：在 IDE 中直接运行（推荐）

1. 在项目中找到 `src/main/resources/groovy/` 目录
2. 双击任意 `.groovy` 文件打开
3. 右键点击编辑器，选择 "Run 'xxx.groovy'"
4. 查看控制台输出结果

**优点**：
- ✅ 完整的语法提示和代码补全
- ✅ 实时错误检查
- ✅ 可以设置断点调试
- ✅ 支持重构和查找引用

### 方法 2：通过 Java 代码加载执行

```java
import com.github.axinger.groovy.GroovyUtils;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GroovyScriptRunner {
    public static void main(String[] args) throws Exception {
        // 读取脚本文件
        String script = new String(Files.readAllBytes(
            Paths.get("src/main/resources/groovy/01-basic-variables.groovy")
        ));
        
        // 准备上下文数据（可选）
        Map<String, Object> context = new HashMap<>();
        context.put("param1", "value1");
        context.put("param2", 123);
        
        // 执行脚本
        Object result = GroovyUtils.execute(script, context);
        System.out.println("执行结果: " + result);
    }
}
```

### 方法 3：命令行运行

```bash
# 确保已安装 Groovy
groovy --version

# 运行脚本
cd D:\code\ax-springboot-demo\ax-springboot2-demo\a01-spring-boot-learn\a01-27-script
groovy src/main/resources/groovy/01-basic-variables.groovy
```

### 方法 4：单元测试

参考 [GroovyFileTests.java](../../test/java/com/github/axinger/GroovyFileTests.java)：

```java
@Test
@DisplayName("测试加载基本变量脚本")
public void testBasicVariablesScript() throws Exception {
    String script = loadScript("01-basic-variables.groovy");
    Object result = GroovyUtils.execute(script, new HashMap<>());
    System.out.println("基本变量脚本执行完成");
}
```

## 📝 脚本特点

### 1. 完整注释
每个脚本都包含详细的中文注释：
```groovy
// 1. 变量声明
def name = "Groovy"
def version = 4.0

// 2. 字符串插值
def greeting = "Hello, ${name}! Version: ${version}"
println greeting
```

### 2. 实际示例
所有示例都是真实场景：
- 邮箱验证
- 手机号验证
- 日期计算
- JSON 数据处理
- 集合操作等

### 3. 独立运行
每个脚本都可以独立运行，不依赖其他文件。

### 4. 渐进式学习
从基础到高级，循序渐进：
1. 基本语法 → 2. 集合操作 → 3. 高级特性 → 4. 实际应用

## 🎯 学习路径建议

### 初学者
1. **第1天**：`01-basic-variables.groovy` - 掌握变量和字符串
2. **第2天**：`02-list-operations.groovy` - 学习 List 操作
3. **第3天**：`03-map-operations.groovy` - 学习 Map 操作
4. **第4天**：`04-closures.groovy` - 理解闭包概念
5. **第5天**：`05-conditionals-loops.groovy` - 条件和循环

### 进阶开发者
1. `06-exception-handling.groovy` - 异常处理最佳实践
2. `07-regex.groovy` - 正则表达式应用
3. `08-date-time.groovy` - 日期时间处理
4. `09-json-processing.groovy` - JSON 数据操作
5. `10-classes-objects.groovy` - 面向对象编程

### 实战应用
- 将这些脚本作为模板
- 根据实际需求修改
- 集成到你的项目中

## 💡 最佳实践

### 1. 代码组织
```groovy
// ✅ 好的做法：清晰的分组和注释
// ==================== 基本操作 ====================
def numbers = [1, 2, 3, 4, 5]

// ==================== 高级操作 ====================
def result = numbers.findAll { it % 2 == 0 }
```

### 2. 命名规范
```groovy
// ✅ 使用有意义的变量名
def userEmail = "user@example.com"
def isValidEmail = validateEmail(userEmail)

// ❌ 避免无意义的名称
def x = "user@example.com"
def y = validateEmail(x)
```

### 3. 利用 Groovy 特性
```groovy
// ✅ 简洁的 Groovy 风格
def evens = numbers.findAll { it % 2 == 0 }

// ❌ 过于 Java 风格
List<Integer> evens = new ArrayList<>();
for (Integer num : numbers) {
    if (num % 2 == 0) {
        evens.add(num);
    }
}
```

## 🔧 常见问题

### Q1: IDE 没有语法提示？
**A**: 
- IntelliJ IDEA: 确保安装了 Groovy 插件（默认已安装）
- VS Code: 安装 Groovy 扩展
- Eclipse: 安装 Groovy Development Tools

### Q2: 如何调试 Groovy 脚本？
**A**:
1. 在 IDE 中打开脚本文件
2. 设置断点
3. 右键选择 "Debug"
4. 像调试 Java 代码一样调试

### Q3: 脚本执行出错怎么办？
**A**:
1. 检查语法错误（IDE 会标红）
2. 查看控制台错误信息
3. 参考对应的测试用例
4. 查阅 Groovy 官方文档

### Q4: 如何在脚本中使用外部依赖？
**A**:
```groovy
// 在脚本顶部导入需要的类
import java.time.LocalDate
import groovy.json.JsonSlurper

// 然后正常使用
def today = LocalDate.now()
```

### Q5: 脚本文件放在哪里最合适？
**A**:
- 当前项目：`src/main/resources/groovy/`
- 这样可以被 IDE 识别并获得语法提示
- 也可以通过 ClassPath 加载

## 📚 相关资源

- [Groovy 官方文档](https://groovy-lang.org/documentation.html)
- [Groovy API 参考](https://docs.groovy-lang.org/latest/html/api/)
- [项目测试文件](../../test/java/com/github/axinger/GroovyTests.java)
- [脚本详细说明](README.md)

## 🎉 总结

现在你已经拥有了：
- ✅ 10 个完整的 Groovy 示例脚本
- ✅ 覆盖 Groovy 核心语法和功能
- ✅ 详细的中文注释和说明
- ✅ IDE 语法提示支持
- ✅ 可直接运行的测试用例
- ✅ 实用的学习路径建议

开始你的 Groovy 学习之旅吧！🚀

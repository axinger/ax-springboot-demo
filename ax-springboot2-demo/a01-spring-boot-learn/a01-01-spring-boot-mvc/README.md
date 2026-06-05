```text
外部加载application.yml,
和xx.jar目录下创建application.yml

和xx.jar目录下创建config/application.yml
```

---

## YAML 块标量（Block Scalars）语法 Demo

本模块包含 YAML 块标量语法的完整演示，展示如何在 Spring Boot 配置文件中使用 `>` 和 `|` 系列语法处理多行文本。

### 核心概念

块标量由**样式指示符**和**缩进指示符**组合而成：

| 样式指示符 | 含义 | 适用场景 |
|-----------|------|---------|
| `>` | 折叠样式（Folded） | 文中换行变空格，适合长段落、JSON |
| `\|` | 字面样式（Literal） | 保留文中换行符，适合 SQL、Markdown |

| 缩进指示符 | 含义 | 效果 |
|-----------|------|------|
| 无后缀（Clip） | 默认 | 文末保留 1 个换行符 |
| `-`（Strip） | 去除 | 文末去除所有换行符 |
| `+`（Keep） | 保留 | 文末保留所有尾随空行 |

### 涉及文件

| 文件 | 说明 |
|------|------|
| [BlockScalarProperties.java](src/main/java/com/github/axinger/model/properties/BlockScalarProperties.java) | 配置属性类，绑定 `block.scalar` 前缀 |
| [BlockScalarController.java](src/main/java/com/github/axinger/controller/BlockScalarController.java) | 测试接口，`/block-scalar/all` 和 `/block-scalar/analyze` |
| `application-dev.yml` | 包含完整的块标量配置示例 |

### 测试接口

- `GET /block-scalar/all` — 返回所有块标量配置的原始值
- `GET /block-scalar/analyze` — 分析字符串特征（长度、换行符数量等）

### 最佳实践

- **JSON 字符串配置**：推荐使用 `>-`，在 YAML 中格式化书写，Spring Boot 读到的是紧凑单行 JSON
- **SQL 脚本配置**：使用 `\|`，保留换行符以维持 SQL 可读性
- **Markdown 文本**：使用 `\|`，保留原始格式

---

## 配置属性绑定 Demo（JSON 字符串转对象）

本模块演示如何通过自定义 `Converter` 将 YAML 中的 JSON 字符串自动转换为 Java 对象。

### 场景说明

在 YAML 中配置如下 JSON 字符串：
```yaml
virtually:
  call:
    intervalConfig: "{\"0_720\":\"0\",\"720_1020\":\"1\"}"
```

Spring Boot 启动时自动将其转换为 `List<PressureInterval>` 对象列表。

### 涉及文件

| 文件 | 说明 |
|------|------|
| [VirtuallyCallProperties.java](src/main/java/com/github/axinger/model/properties/VirtuallyCallProperties.java) | 配置属性类，绑定 `virtually.call` 前缀 |
| [PressureInterval.java](src/main/java/com/github/axinger/model/properties/PressureInterval.java) | 压力值区间数据对象 |
| [PressureIntervalConverter.java](src/main/java/com/github/axinger/model/properties/PressureIntervalConverter.java) | 自定义转换器，JSON → `List<PressureInterval>` |

### 核心实现

**PressureIntervalConverter** 使用 `@ConfigurationPropertiesBinding` 注解，Spring Boot 在绑定配置时自动发现并调用：

1. 将 JSON 字符串解析为 `Map<String, String>`
2. 按 `_` 切割 Key 得到上下界（如 `0_720` → lowerBound=0, upperBound=720）
3. 构建 `PressureInterval` 对象列表并按 lowerBound 排序

### 使用方式

在 `VirtuallyCallProperties` 中直接声明：
```java
private List<PressureInterval> intervalConfig;
private Map<String, List<PressureInterval>> intervalConfigByTakeout;
```

Spring 会自动调用 `PressureIntervalConverter` 完成类型转换，包括 Map 中的 Value。

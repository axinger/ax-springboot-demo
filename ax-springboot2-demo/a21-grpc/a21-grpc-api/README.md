优雅性对比：src/ vs target/ 存放生成代码
在 Maven 项目中，target/generated-sources 是更优雅、更符合 Maven 规范的做法，而手动生成到 src/ 目录会带来一些问题。以下是详细分析：

1. 生成到 target/generated-sources（推荐 ✅）
   优点
   ✅ 符合 Maven 标准

target/ 目录是 Maven 的构建输出目录，专门用于存放生成的代码（如 Protobuf、JAXB、Annotation Processors 生成的代码）。

执行 mvn clean 时会自动清理，避免残留旧代码影响新构建。

✅ IDE 自动支持

IntelliJ IDEA、Eclipse 等 IDE 自动识别 target/generated-sources 并加入 classpath，无需手动配置。

在 IntelliJ 中，右键 target/generated-sources → "Mark Directory as Generated Sources Root"（通常自动完成）。

✅ 避免版本控制污染

生成的代码不应该提交到 Git（在 .gitignore 里忽略 target/）。

如果放在 src/，容易误提交，导致代码库臃肿或冲突。

✅ 增量编译优化

Maven 会缓存生成的代码，仅当 .proto 文件变更时重新生成，提高构建速度。

缺点
⚠️ 每次 mvn clean 后需要重新生成

但这不是问题，因为 mvn compile 会自动重新生成。

2. 生成到 src/main/java（不推荐 ❌）
   缺点
   ❌ 违反 Maven 规范

src/ 目录应仅存放手写代码，生成的代码属于构建产物，不应混入源码目录。

类似 .class 文件不应该放在 src/ 里。

❌ 版本控制问题

生成的代码可能被误提交到 Git，导致：

仓库臃肿（生成的代码可能很大）。

合并冲突（如果多人修改 .proto 文件并重新生成）。

❌ IDE 可能不会自动刷新

某些 IDE 可能不会自动检测 src/ 下的生成代码，需要手动刷新项目。

❌ mvn clean 不会清理

如果生成到 src/，执行 mvn clean 后残留的旧代码可能导致编译错误。

可能的适用场景
🔹 遗留项目：某些旧项目强制要求生成代码在 src/ 目录（但应逐步迁移到 target/）。
🔹 特殊构建需求：如生成的代码需要被其他工具处理（但仍建议先生成到 target/，再复制到 src/）。

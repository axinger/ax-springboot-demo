# Apache Commons 常用工具示例

本项目演示 Apache Commons 系列常用工具的使用方法，每个组件都有独立的测试类展示核心 API。

## 模块说明

| 测试类 | 组件 | 说明 |
|--------|------|------|
| [CommonsTextTest](src/test/java/com/github/axinger/CommonsTextTest.java) | commons-text | 字符串模板、编辑距离、HTML/CSV 转义、随机字符串 |
| [CommonsLang3Test](src/test/java/com/github/axinger/CommonsLang3Test.java) | commons-lang3 | StringUtils、ArrayUtils、DateUtils、StopWatch、SystemUtils |
| [CommonsIOTest](src/test/java/com/github/axinger/CommonsIOTest.java) | commons-io | FileUtils、FilenameUtils、IOUtils、文件过滤 |
| [CommonsCollections4Test](src/test/java/com/github/axinger/CommonsCollections4Test.java) | commons-collections4 | CollectionUtils、MapUtils、Bag、MultiMap、LRUMap |
| [CommonsCodecTest](src/test/java/com/github/axinger/CommonsCodecTest.java) | commons-codec | Base64、MD5/SHA、Hex、URLCodec、HMAC |
| [CommonsBeanUtilsTest](src/test/java/com/github/axinger/CommonsBeanUtilsTest.java) | commons-beanutils | 属性复制、Map 转 Bean、嵌套属性 |
| [CommonsCsvTest](src/test/java/com/github/axinger/CommonsCsvTest.java) | commons-csv | CSV 读写、格式配置 |
| [CommonsCompressTest](src/test/java/com/github/axinger/CommonsCompressTest.java) | commons-compress | ZIP 压缩解压 |
| [CommonsEmailTest](src/test/java/com/github/axinger/CommonsEmailTest.java) | commons-email | 邮件构建（SMTP 配置需自行替换） |
| [CommonsPool2Test](src/test/java/com/github/axinger/CommonsPool2Test.java) | commons-pool2 | 对象池配置与使用 |
| [CommonsConfiguration2Test](src/test/java/com/github/axinger/CommonsConfiguration2Test.java) | commons-configuration2 | Properties/XML 配置读取 |
| [CommonsExecTest](src/test/java/com/github/axinger/CommonsExecTest.java) | commons-exec | 外部命令执行、超时控制、异步执行 |
| [CommonsMath3Test](src/test/java/com/github/axinger/CommonsMath3Test.java) | commons-math3 | 描述统计、回归分析、组合计算 |
| [CommonsFileUploadTest](src/test/java/com/github/axinger/CommonsFileUploadTest.java) | commons-fileupload | 文件上传解析（含 Spring 对比说明） |

## 运行测试

```bash
mvn test
```

或运行单个测试类：

```bash
mvn test -Dtest=CommonsLang3Test
```

## 依赖说明

### 已包含的依赖

| 依赖 | 版本 | 状态 |
|------|------|------|
| commons-text | 由 parent 管理 | 活跃维护 |
| commons-lang3 | 3.14.0 | 活跃维护 |
| commons-collections4 | 4.4 | 活跃维护 |
| commons-beanutils | 1.9.4 | 维护中 |
| commons-io | 2.15.1 | 活跃维护 |
| commons-fileupload | 1.5 | 维护中，Spring 项目建议用内置 MultipartFile |
| commons-compress | 1.26.0 | 活跃维护 |
| commons-csv | 1.10.0 | 活跃维护 |
| commons-email | 1.5 | 维护中 |
| commons-pool2 | 2.12.1 | 活跃维护 |
| commons-configuration2 | 2.1.1 | 维护中 |
| commons-codec | 1.16.1 | 活跃维护 |
| commons-exec | 1.4.0 | 维护中 |
| commons-math3 | 3.6.1 | 维护中 |

### 已移除的依赖（停止维护或长期未更新）

| 依赖 | 最后版本 | 移除原因 |
|------|----------|----------|
| commons-digester | 2.1 (2011) | **停止维护** |
| commons-jxpath | 1.3 (2008) | **停止维护** |
| commons-dbutils | 1.7 (2017) | **停止维护** |
| commons-dbcp2 | 2.9.0 | 较旧，Spring Boot 已内置连接池 |
| commons-validator | 1.7 (2020) | 长期未更新 |
| commons-cli | 1.6.0 | 命令行解析，使用场景有限 |
| commons-rng | 1.5 | 较冷门 |
| httpclient / httpclient5 | 4.5.14 / 5.3.1 | 非 Commons 项目，独立维护 |

## 选型建议

- **Spring Boot 项目**：优先使用 Spring 内置功能（如 `MultipartFile` 替代 `commons-fileupload`，HikariCP 替代 `commons-dbcp2`）
- **通用工具**：`commons-lang3`、`commons-io`、`commons-collections4` 是必备基础库
- **编解码/校验**：`commons-codec` 提供标准的 Base64、MD5、SHA 等实现
- **配置读取**：`commons-configuration2` 支持多格式配置文件统一管理

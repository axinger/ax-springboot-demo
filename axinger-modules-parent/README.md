# Axinger Modules - Independent Maven Projects

本目录包含从 ax-springboot-demo 项目中提取的独立 Maven 模块，可以单独构建和发布到 Maven 仓库。

## 模块结构

### axinger-common

通用工具类 Spring Boot Starters，包含：

-   `util-spring-boot-starter` - 通用工具类
-   `redis-spring-boot-starter` - Redis 集成
-   `quartz-spring-boot-starter` - 任务调度
-   `mongodb-spring-boot-starter` - MongoDB 支持
-   `minio-spring-boot-starter` - MinIO 对象存储
-   `excel-spring-boot-starter` - Excel 处理
-   `oos-spring-boot-starter` - 对象存储服务
-   `axinger-tool-core` - 核心工具类

### axinger-config

自动配置 Spring Boot Starters，包含：

-   `result-config-spring-boot-starter` - 统一响应包装
-   `doc-config-spring-boot-starter` - 文档配置
-   `base-config-spring-boot-starter` - 基础配置
-   `mybatis-plus-config-spring-boot-starter` - MyBatis Plus 配置
-   `advice-config-spring-boot-starter` - 全局异常处理
-   `jackson-config-spring-boot-starter` - JSON 序列化
-   `http-config-spring-boot-starter` - HTTP 客户端配置
-   `logback-config-spring-boot-starter` - 日志配置
-   `executor-config-spring-boot-starter` - 异步执行器配置
-   `cors-config-spring-boot-starter` - 跨域配置
-   `request-spring-boot-starter` - 请求处理

### axinger-cloud

云基础设施 Spring Boot Starters，包含：

-   `cloud-fetch-gateway-starter` - 网关过滤器 starter

### BOM 模块

-   `axinger-bom` - **总 BOM，包含所有模块**（推荐使用）
-   `axinger-common-bom` - 通用依赖管理（继承自总 BOM）
-   `axinger-config-bom` - 配置 starters 依赖管理（继承自总 BOM）  
-   `axinger-cloud-bom` - 云依赖管理（继承自总 BOM）

## 构建和发布

### 前提条件

-   JDK 21
-   Maven 3.6+

### 构建命令

```bash
# 构建所有模块
cd axinger-modules
mvn clean install

# 构建特定模块
cd axinger-common
mvn clean install

# 跳过测试
mvn clean install -DskipTests

# 发布到本地 Maven 仓库
mvn clean deploy
```

### 发布到 Maven 仓库

1.  配置 `~/.m2/settings.xml` 添加仓库认证信息
2.  在每个模块的 pom.xml 中配置仓库地址
3.  执行发布命令：

```bash
mvn clean deploy
```

## 在项目中使用

### 方法1：使用总 BOM（最推荐）

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-all</artifactId>
            <version>2026.06.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 方法2：使用分类 BOM（按需引入）

```xml
<dependencyManagement>
    <dependencies>
        <!-- 引入通用工具类 -->
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-common-bom</artifactId>
            <version>2026.06.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        
        <!-- 引入配置类 -->
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-config-bom</artifactId>
            <version>2026.06.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        
        <!-- 引入云相关 -->
        <dependency>
            <groupId>com.github.axinger</groupId>
            <artifactId>axinger-cloud-bom</artifactId>
            <version>2026.06.01-2.7</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- 使用 starter，版本由 BOM 管理 -->
    <dependency>
        <groupId>com.github.axinger</groupId>
        <artifactId>redis-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>com.github.axinger</groupId>
        <artifactId>result-config-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

### 方法2：直接指定版本

```xml
<dependencies>
    <dependency>
        <groupId>com.github.axinger</groupId>
        <artifactId>redis-spring-boot-starter</artifactId>
        <version>2026.06.01-2.7</version>
    </dependency>
    <dependency>
        <groupId>com.github.axinger</groupId>
        <artifactId>result-config-spring-boot-starter</artifactId>
        <version>2026.06.01-2.7</version>
    </dependency>
</dependencies>
```

## 版本说明

当前版本：`2026.06.01-2.7`

-   `2026.01.01` - 年月版本号
-   `2.7` - 对应的 Spring Boot 版本

## 注意事项

1.  所有模块都配置了 `maven-source-plugin`，会自动生成源码包
2.  模块依赖于 Spring Boot 2.7.18
3.  使用前请确保已安装到本地 Maven 仓库或配置了远程仓库
4.  建议优先使用 BOM 方式管理版本，确保版本一致性

## 开发指南

如需修改或扩展这些模块：

1.  在对应的模块目录中进行修改
2.  运行测试确保功能正常
3.  更新版本号（使用 `mvn versions:set -DnewVersion=xxx`）
4.  构建并发布到仓库

更多详细信息请参考各个模块目录中的 README 文件。
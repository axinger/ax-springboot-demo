# ax-enterprise-demo 企业级 Maven 多模块架构示例

## 项目结构

```
ax-enterprise-demo/                          # 根聚合 POM（只聚合，不继承）
├── ax-enterprise-parent/                    # 父 POM（统一定义依赖版本、插件、构建配置）
│   └── pom.xml
├── ax-enterprise-api/                       # API 模块聚合 POM
│   ├── api-user/                            # 用户服务 API（DTO、Feign接口、枚举）
│   │   ├── src/main/java/com/github/axinger/api/user/
│   │   │   ├── dto/UserDTO.java             # 用户数据传输对象
│   │   │   ├── dto/UserRequest.java         # 用户查询请求
│   │   │   ├── feign/UserFeignApi.java      # 用户服务 Feign 接口
│   │   │   └── enums/UserStatusEnum.java    # 用户状态枚举
│   │   └── pom.xml
│   └── api-order/                           # 订单服务 API（DTO、Feign接口、枚举）
│       ├── src/main/java/com/github/axinger/api/order/
│       │   ├── dto/OrderDTO.java            # 订单数据传输对象
│       │   ├── dto/OrderRequest.java        # 订单查询请求
│       │   ├── feign/OrderFeignApi.java     # 订单服务 Feign 接口
│       │   └── enums/OrderStatusEnum.java   # 订单状态枚举
│       └── pom.xml
├── ax-enterprise-common/                    # 公共工具模块聚合 POM
│   └── common-core/                         # 核心工具类
│       ├── src/main/java/com/github/axinger/common/result/
│       │   └── Result.java                  # 统一响应结果
│       └── pom.xml
├── ax-enterprise-project/                   # 业务项目聚合 POM
│   ├── project-a/                           # 项目A - 用户服务（可独立部署）
│   │   ├── src/main/java/com/github/axinger/projecta/
│   │   │   ├── ProjectAApplication.java     # 启动类
│   │   │   └── controller/UserController.java # 用户业务 Controller
│   │   ├── src/main/resources/application.yml
│   │   └── pom.xml
│   ├── project-b/                           # 项目B - 订单服务（可独立部署）
│   │   ├── src/main/java/com/github/axinger/projectb/
│   │   │   ├── ProjectBApplication.java     # 启动类
│   │   │   └── controller/OrderController.java # 订单业务 Controller
│   │   ├── src/main/resources/application.yml
│   │   └── pom.xml
│   └── project-c/                           # 项目C - All-in-One 聚合服务
│       ├── src/main/java/com/github/axinger/projectc/
│       │   ├── ProjectCApplication.java     # 启动类（聚合入口）
│       │   └── controller/HealthController.java # 健康检查
│       ├── src/main/resources/application.yml
│       └── pom.xml
└── pom.xml
```

## POM 层级关系

```
ax-enterprise-demo (根聚合)
  ├── ax-enterprise-parent (父POM，被所有子模块继承)
  │     └── 继承 spring-boot-starter-parent (Spring Boot 3.2.12)
  ├── ax-enterprise-api (聚合POM，继承parent)
  │   ├── api-user (继承parent，lightweight jar，skip spring-boot打包)
  │   └── api-order (继承parent，lightweight jar，skip spring-boot打包)
  ├── ax-enterprise-common (聚合POM，继承parent)
  │   └── common-core (继承parent，lightweight jar，skip spring-boot打包)
  └── ax-enterprise-project (聚合POM，继承parent)
      ├── project-a (继承parent，可执行jar，端口8081)
      ├── project-b (继承parent，可执行jar，端口8082)
      └── project-c (继承parent，可执行jar，端口8080，显式指定mainClass)
```

## 依赖关系

| 模块 | 依赖 |
|------|------|
| api-user | 无（仅 spring-web provided） |
| api-order | 无（仅 spring-web provided） |
| common-core | 无（仅 spring-web provided） |
| project-a | api-user + common-core + spring-boot-starter-web |
| project-b | api-order + common-core + spring-boot-starter-web |
| project-c | project-a + project-b + spring-boot-starter-web |

## 关键设计要点

### 1. 根 POM 只做聚合

根 `pom.xml` 仅通过 `<modules>` 聚合所有子模块，不定义任何依赖或构建配置。

### 2. 父 POM 统一定义

`ax-enterprise-parent` 统一定义：
- `<properties>`：JDK 版本、各依赖版本号
- `<dependencyManagement>`：所有子模块的依赖版本
- `<pluginManagement>`：插件版本

### 3. API 模块轻量级

API 模块（`api-user`、`api-order`）只包含接口、DTO、枚举：
- 依赖 `spring-web` 且 scope 为 `provided`
- `spring-boot-maven-plugin` 配置 `<skip>true</skip>`，不生成可执行 jar
- 无业务逻辑，无数据库访问

### 4. 项目可独立部署

`project-a` 和 `project-b` 各自有独立的 `@SpringBootApplication` 启动类：
- 分别运行在端口 8081 和 8082
- 各自生成可执行的 fat-jar
- 独立打包时互不包含对方代码

### 5. All-in-One 聚合（project-c）

`project-c` 同时依赖 `project-a` 和 `project-b`：
- 通过 `<mainClass>` 显式指定启动类（解决多主类冲突）
- 通过 `scanBasePackages` 扩大组件扫描范围
- 运行在端口 8080
- fat-jar 包含 project-a 和 project-b 的所有业务代码

## 打包与运行

### 全量构建

```bash
cd ax-enterprise-demo
mvn clean install
```

### 独立部署 project-a

```bash
cd ax-enterprise-demo/ax-enterprise-project/project-a
mvn spring-boot:run
# 或
java -jar target/project-a-1.0.0.jar
```

访问：http://localhost:8081/api/user/list

### 独立部署 project-b

```bash
cd ax-enterprise-demo/ax-enterprise-project/project-b
mvn spring-boot:run
# 或
java -jar target/project-b-1.0.0.jar
```

访问：http://localhost:8082/api/order/list

### All-in-One 部署 project-c

```bash
cd ax-enterprise-demo/ax-enterprise-project/project-c
mvn spring-boot:run
# 或
java -jar target/project-c-1.0.0.jar
```

访问：
- http://localhost:8080/api/user/list（用户服务）
- http://localhost:8080/api/order/list（订单服务）
- http://localhost:8080/health（健康检查）

## 打包隔离验证

```bash
# 检查 project-a 的 jar 不包含 project-b
cd ax-enterprise-demo/ax-enterprise-project/project-a
mvn clean package
jar tf target/project-a-1.0.0.jar | grep "projectb"  # 无输出 = 隔离成功

# 检查 project-c 的 jar 同时包含 project-a 和 project-b
cd ../project-c
mvn clean package
jar tf target/project-c-1.0.0.jar | grep "projecta"   # 有输出
jar tf target/project-c-1.0.0.jar | grep "projectb"   # 有输出
```

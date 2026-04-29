# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此仓库中工作时提供指导。

## 构建命令

```bash
# 构建所有模块
mvn clean install

# 构建指定模块
mvn clean install -pl a21-springboot3-oauth2-server

# 运行应用（从模块目录）
mvn spring-boot:run -pl a21-springboot3-oauth2-server
```

## 项目结构

基于 Spring Boot 3 和 Spring Authorization Server 的 OAuth2 多模块 Maven 演示项目。

| 模块 | 端口 | 角色 |
|------|------|------|
| a21-springboot3-oauth2-server | 默认 (8080) | 授权服务器 |
| a21-springboot3-oauth2-resource | 可配置 | 资源服务器 |
| a21-springboot3-oauth2-client | 可配置 | OAuth2 客户端 |

## 架构说明

**OAuth2 授权服务器** (`a21-springboot3-oauth2-server`):
- 使用 `spring-boot-starter-oauth2-authorization-server`
- JWT 令牌使用 RSA 密钥签名（启动时生成）
- 基于 JDBC 存储客户端、授权信息和授权确认
- 支持授权码模式、客户端凭证模式、设备码模式、刷新令牌模式
- 自定义授权确认页面 `/oauth2/consent`

**已注册客户端** (配置于 `AuthorizationConfig.java`):
- `messaging-client` / 密钥: `123456` - 标准客户端
- `device-message-client` - 用于设备码模式的公共客户端

**默认用户** (内存存储):
- 用户名: `admin` / 密码: `123456`
- 角色: `admin`, `normal`, `unAuthentication`

**数据库要求**:
- MySQL 数据库名 `ax_oauth2` (配置于 `application.yml`)
- 表结构由 Spring Authorization Server 自动创建
- 连接地址: `jdbc:mysql://localhost:3306/ax_oauth2`

**关键配置文件**:
- `AuthorizationConfig.java` - OAuth2 服务器核心配置（安全过滤器链、JWT、已注册客户端）

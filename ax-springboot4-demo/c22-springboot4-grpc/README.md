# Spring Boot 3 + Spring gRPC Demo

本项目演示了如何在 Spring Boot 3 中使用 Spring 官方的 gRPC 支持。

## 项目结构

```
c22-springboot4-grpc/
├── c22-grpc-api/          # gRPC API 模块（proto 定义）
├── c22-grpc-server/       # gRPC 服务端
└── c22-grpc-client/       # gRPC 客户端
```

## 技术栈

- **Spring Boot**: 3.4.x / 3.5.x
- **Spring gRPC**: 0.8.0 (官方支持)
- **gRPC**: 1.68.0
- **Protobuf**: 4.28.2
- **Java**: 17+

## 快速开始

### 1. 编译项目

首先编译整个项目以生成 Protobuf 代码：

```bash
cd c22-springboot4-grpc
mvn clean install
```

### 2. 启动 gRPC Server

```bash
cd c22-grpc-server
mvn spring-boot:run
```

服务器将在以下端口启动：
- HTTP 端口: 8080
- gRPC 端口: 9090

### 3. 启动 gRPC Client

在另一个终端中：

```bash
cd c22-grpc-client
mvn spring-boot:run
```

客户端将在端口 8081 启动。

## 测试接口

### 一元调用测试

访问客户端的一元调用接口：

```bash
curl "http://localhost:8081/grpc/say-hello?name=张三"
```

预期响应：
```json
{
  "success": true,
  "message": "Hello ==> 张三"
}
```

### 流式调用测试

访问客户端的流式调用接口：

```bash
curl "http://localhost:8081/grpc/stream-hello?name=李四"
```

预期响应：
```json
{
  "success": true,
  "messages": [
    "Hello(0) ==> 李四",
    "Hello(1) ==> 李四",
    "Hello(2) ==> 李四",
    "Hello(3) ==> 李四",
    "Hello(4) ==> 李四"
  ],
  "count": 5
}
```

## 使用 grpcurl 直接测试 gRPC Server

你也可以直接使用 `grpcurl` 工具测试 gRPC 服务：

### 安装 grpcurl

```bash
# macOS
brew install grpcurl

# Windows (使用 scoop)
scoop install grpcurl
```

### 测试一元调用

```bash
grpcurl -plaintext -d '{"name":"World"}' localhost:9090 Simple.SayHello
```

### 测试流式调用

```bash
grpcurl -plaintext -d '{"name":"Stream"}' localhost:9090 Simple.StreamHello
```

## 核心代码说明

### 1. Proto 定义 (c22-grpc-api)

在 `src/main/proto/hello.proto` 中定义了 gRPC 服务：

```protobuf
service Simple {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
  rpc StreamHello (HelloRequest) returns (stream HelloReply) {}
}
```

### 2. 服务端实现 (c22-grpc-server)

使用 `@GrpcService` 注解标记服务实现类：

```java
@GrpcService
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        // 实现业务逻辑
    }
}
```

### 3. 客户端配置 (c22-grpc-client)

创建 gRPC stub Bean：

```java
@Bean
public SimpleGrpc.SimpleBlockingStub simpleBlockingStub(GrpcChannelFactory channels) {
    return SimpleGrpc.newBlockingStub(channels.createChannel("default"));
}
```

## 配置文件

### Server 配置 (application.yml)

```yaml
spring:
  grpc:
    server:
      port: 9090
```

### Client 配置 (application.yml)

```yaml
spring:
  grpc:
    client:
      default-channel:
        address: localhost:9090
        negotiation-type: plaintext
```

## 特性展示

1. **一元调用 (Unary RPC)**: 标准的请求-响应模式
2. **服务器端流 (Server Streaming)**: 服务器返回多个响应
3. **自动配置**: Spring gRPC 提供的自动配置简化了开发
4. **依赖注入**: 完全集成到 Spring 容器中

## 注意事项

1. 确保先编译 `c22-grpc-api` 模块生成 Protobuf 代码
2. Server 必须先于 Client 启动
3. 默认使用明文传输 (plaintext)，生产环境建议使用 TLS
4. Spring gRPC 目前还在快速发展中，API 可能会有变化

## 与第三方库对比

| 特性 | Spring gRPC (官方) | grpc-spring-boot-starter (第三方) |
|------|-------------------|----------------------------------|
| 维护方 | Spring Team | 社区 (devh) |
| 成熟度 | 发展中 (0.8.0) | 成熟 (2.15.0) |
| Spring Boot 3 支持 | ✅ | ✅ |
| 自动配置 | ✅ | ✅ |
| 服务发现 | 待完善 | 完善 |
| 文档 | 基础 | 丰富 |

## 参考资料

- [Spring gRPC 官方文档](https://spring.io/projects/spring-grpc)
- [gRPC 官方文档](https://grpc.io/docs/)
- [Protobuf 文档](https://developers.google.com/protocol-buffers)

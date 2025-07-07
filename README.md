# RPC Demo 项目

基于 SpringBoot3 实现的 RPC 框架演示项目，使用 Netty 作为网络通信框架，支持 JSON 序列化，采用动态代理技术实现透明的远程方法调用。

## 项目架构

```
rpc-demo/
├── rpc-common/          # 公共模块
│   ├── annotation/      # RPC注解
│   ├── dto/            # 数据传输对象
│   └── service/        # 服务接口
├── rpc-core/           # 核心模块
│   ├── client/         # 客户端实现
│   ├── server/         # 服务端实现
│   ├── protocol/       # 协议编解码
│   ├── proxy/          # 动态代理
│   └── serialization/  # 序列化
├── rpc-server/         # 服务提供者
└── rpc-client/         # 服务消费者
```

## 功能特性

- 🚀 **基于 SpringBoot3**: 使用最新的 SpringBoot3 框架
- 🔥 **Netty 网络通信**: 高性能的异步网络通信框架
- 📦 **JSON 序列化**: 使用 Jackson 进行 JSON 序列化/反序列化
- 🎯 **动态代理**: 基于 JDK 动态代理实现透明的远程调用
- 🔧 **注解驱动**: 通过注解标识服务提供者和消费者
- 💾 **连接复用**: 客户端与服务端保持长连接
- 🔄 **异步处理**: 支持异步请求处理

## 技术栈

- **Java**: 17
- **SpringBoot**: 3.2.0
- **Netty**: 4.1.100.Final
- **Jackson**: 2.15.2
- **Maven**: 3.8+

## 快速开始

### 1. 构建项目

```bash
mvn clean install
```

### 2. 启动服务端

```bash
cd rpc-server
mvn spring-boot:run
```

服务端将在以下端口启动：

- **HTTP 端口**: 8080
- **RPC 端口**: 9999

### 3. 启动客户端

```bash
cd rpc-client
mvn spring-boot:run
```

客户端将在 8081 端口启动。

### 4. 测试 RPC 调用

#### 获取用户列表

```bash
curl http://localhost:8081/users
```

#### 根据 ID 获取用户

```bash
curl http://localhost:8081/users/1
```

#### 创建用户

```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{"username":"测试用户","email":"test@example.com","age":25}'
```

#### 更新用户

```bash
curl -X PUT http://localhost:8081/users/1 \
  -H "Content-Type: application/json" \
  -d '{"username":"更新用户","email":"updated@example.com","age":30}'
```

#### 删除用户

```bash
curl -X DELETE http://localhost:8081/users/1
```

#### 根据用户名获取用户

```bash
curl http://localhost:8081/users/username/张三
```

## 核心组件说明

### 1. RPC 注解

#### @RpcService

用于标识服务提供者：

```java
@RpcService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    // 实现方法
}
```

#### @RpcClient

用于标识服务消费者：

```java
@RpcClient(interfaceClass = UserService.class, serverAddress = "localhost:9999")
private UserService userService;
```

### 2. 核心类

- **RpcServer**: RPC 服务器，负责接收和处理 RPC 请求
- **RpcClient**: RPC 客户端，负责发送 RPC 请求
- **RpcProxy**: 动态代理，将本地方法调用转换为 RPC 调用
- **ServiceRegistry**: 服务注册器，管理服务实例
- **JsonSerializer**: JSON 序列化器，处理对象序列化

### 3. 协议设计

```
+--------+--------+--------+
| Length | Header | Body   |
+--------+--------+--------+
| 4 bytes| 可变长  | 可变长  |
+--------+--------+--------+
```

- **Length**: 数据包总长度
- **Header**: 协议头信息
- **Body**: 序列化后的请求/响应数据

## 配置说明

### 服务端配置 (application.yml)

```yaml
server:
  port: 8080 # HTTP端口

spring:
  application:
    name: rpc-server

logging:
  level:
    com.rpc.demo: DEBUG
```

### 客户端配置 (application.yml)

```yaml
server:
  port: 8081 # HTTP端口

spring:
  application:
    name: rpc-client

rpc:
  server:
    host: localhost # RPC服务器地址
    port: 9999 # RPC服务器端口
```

## 扩展功能

### 1. 负载均衡

可以扩展`LoadBalancer`接口实现不同的负载均衡策略：

- 轮询 (Round Robin)
- 随机 (Random)
- 一致性哈希 (Consistent Hash)

### 2. 服务发现

可以集成注册中心（如 ZooKeeper、Nacos）实现服务发现：

```java
public interface ServiceDiscovery {
    List<String> discover(String serviceName);
}
```

### 3. 熔断器

可以集成熔断器（如 Hystrix）提高系统稳定性：

```java
@HystrixCommand(fallbackMethod = "fallback")
public User getUserById(Long id) {
    return userService.getUserById(id);
}
```

## 项目亮点

1. **模块化设计**: 清晰的模块划分，便于扩展和维护
2. **高性能**: 基于 Netty 的异步网络通信
3. **易用性**: 注解驱动，使用简单
4. **可扩展**: 支持自定义序列化器、负载均衡器等
5. **完整示例**: 提供完整的服务端和客户端示例

## 注意事项

1. 确保服务端先启动，客户端后启动
2. 防火墙需要开放相应端口
3. 建议在生产环境中使用连接池
4. 可以添加心跳机制保证连接可用性

## 许可证

MIT License

## 参考资料

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Netty 官方文档](https://netty.io/4.1/api/index.html)
- [Jackson 官方文档](https://github.com/FasterXML/jackson)

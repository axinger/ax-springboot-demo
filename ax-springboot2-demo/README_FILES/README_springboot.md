```text
常见的starter会包括下面四个方面的内容 
自动配置文件，根据classpath是否存在指定的类来决定是否要执行该功能的自动配置。 
spring.factories，非常重要，指导Spring Boot找到指定的自动配置文件。 
endpoint：可以理解为一个admin，包含对服务的描述、界面、交互(业务信息的查询)。 
health indicator:该starter提供的服务的健康指标。
两个需要注意的点： 
1. @ConditionalOnMissingBean的作用是：只有对应的bean在系统中都没有被创建，它修饰的初始化代码块才会执行，【用户自己手动创建的bean优先】。
 2. Spring Boot Starter找到自动配置文件(xxxxAutoConfiguration之类的文件)的方式有两种：spring.factories:由Spring Boot触发探测classpath目录下的类，进行自动配置； 
@EnableXxxxx:有时需要由starter的用户触发*查找自动配置文件的过程
```

```text
Spring Boot Starter的工作原理如下： 
1. Spring Boot 在启动时扫描项目所依赖的JAR包，寻找包含spring.factories文件的JAR 
2. 根据spring.factories配置加载AutoConfigure类 
3. 根据 @Conditional注解的条件，进行自动配置并将Bean注入Spring Context 
```

```text
a.b.c 和 a.b.d 包格式,相同类名会冲突
a.b.c 和 a.d.c 包格式,相同类名不会冲突
```

```text
/project-root/
├── config/                         # 外部 config 目录（优先级最高）
│   ├── application.yml             # 高优先级配置文件
│   ├── application-prod.yml        # 针对 prod 环境的配置文件
│   └── custom.properties           # 自定义扩展配置文件
├── application.properties          # 外部根目录配置文件（优先级次高）
├── target/                         # 编译与打包输出目录
│   ├── server.jar                  # 打包的 Spring Boot 可执行文件
│   └── classes/                    # 编译的 class 文件目录
│       ├── application.yml         # JAR 内部 config/application.yml
│       ├── application-prod.yml    # JAR 内部生产环境配置
│       └── logback-spring.xml      # 日志配置文件
├── logs/                           # 可选，日志文件存放目录
│   ├── app.log
├── README.md                       # 可选，项目说明文档
└── start.sh                        # 可选，启动脚本
```

```text
bootstrap.yml 的配置会被 application.yml 中的相同配置项覆盖。
但是，如果某些配置被绑定到 bootstrap 配置上下文（如 Spring Cloud Config 中的远程配置），则 application.yml 无法覆盖。
```

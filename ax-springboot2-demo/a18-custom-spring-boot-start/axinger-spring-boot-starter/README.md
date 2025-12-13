```text
2.6之前
META-INF/spring.factories

org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.axing.starter.auto.HelloServiceAutoConfiguration
```

```text
2.6之后
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
com.axing.starter.auto.HelloServiceAutoConfiguration

```

```text
可以同时存在,达到兼容效果
```

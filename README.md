# springboot笔记

```text
springboot及spring cloud的demo
```

[AOP](./README_FILES/README_AOP.md)

[docker](./README_FILES/README_docker.md)

[ES](./README_FILES/README_es.md)

[Java](./README_FILES/README_java.md)

[JUC](./README_FILES/README_juc.md)

[jvm调优](./README_FILES/README_jvm调优.md)

[Linux](./README_FILES/README_Linux.md)

[lock](./README_FILES/README_lock.md)

[maven](./README_FILES/README_maven.md)

[mysql](README_FILES/README_mysql.md)

[mybatis](./README_FILES/README_mybatis.md)

[nacos](./README_FILES/README_nacos.md)

[Nginx](./README_FILES/README_Nginx.md)

[rabbitMQ](./README_FILES/README_rabbitMQ.md)

[redis](./README_FILES/README_redis.md)

[docker](./README_FILES/README_docker.md)

[session](./README_FILES/README_session.md)

[spring5](./README_FILES/README_spring5.md)

[spring-cloud-alibaba](./README_FILES/README_spring-cloud-alibaba.md)

[springboot](./README_FILES/README_springboot.md)

[springmvc](./README_FILES/README_springmvc.md)

[vagrant](./README_FILES/README_vagrant.md)

[事务](./README_FILES/README_事务.md)

[内存溢出](./README_FILES/README_内存溢出.md)

[性能监控](./README_FILES/README_性能监控.md)

[警告](./README_FILES/README_警告.md)

[进程](./README_FILES/README_进程.md)

[面试2](./README_FILES/README_面试2.md)

# 高并发

```
高并发系统保护的三把利器 : 缓存、降级和限流
```

```text
限流方式
    限制总并发数(比如数据库连接池、线程池)
    限制瞬时并发数(如nginx的limit_conn模块，用来限制瞬时并发连接数)
    限制时间窗口内的平均速率(如Guava的RateLimiter、nginx的limit_req模块，限制每秒的平均速率)
    限制远程接口调用速率
    限制MQ的消费速率。
    可以根据网络连接数、网络流量、CPU或内存负载等来限流

常见的限流算法有：漏桶(Leaky Bucket)算法、令牌桶算法(Token Bucket)，计数器也可以进行粗暴限流实现
```

```text
# 设置全局用户名和邮箱
git config --global user.name "你的新名字"
git config --global user.email "你的新邮箱"

# 只修改当前仓库
git config user.name "你的新名字"
git config user.email "你的新邮箱"

# 查看
git config user.name
git config user.email
```

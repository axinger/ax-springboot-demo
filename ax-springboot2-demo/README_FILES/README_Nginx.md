# Nginx

```text
安装
1、添加源

默认情况Centos7中无Nginx的源，最近发现Nginx官网提供了Centos的源地址。因`此可以如下执行命令添加源：

sudo rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm

2、安装Nginx

通过yum search nginx看看是否已经添加源成功。如果成功则执行下列命令安装Nginx。

sudo yum install -y nginx

3、启动Nginx并设置开机自动运行

sudo systemctl start nginx.service
sudo systemctl enable nginx.service

systemctl stop nginx.service
systemctl restart nginx.service

4、浏览查看效果
————————————————
版权声明：本文为CSDN博主「s416676943s」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/s416676943s/article/details/113247903

```

```text
1.正向代理:
2.反向代理:
3.负载均衡:将请求分发到多个服务器上
4.动静分离:动态页面和静态页面不同服务器解析,css,js 是静态资源,jsp的动态资源
```

```text
配置文件 三部分
1.全局块:
    worker_processes 值越大,支持并发数越多
2.events块
  worker_connections  服务器与用户链接数,最大1024,
    配置最频繁的部分,
3.http块
```

## 高可用

````text
nginx可能宕机,主备服务器
keepalived:路由,建立虚拟ip,绑定到主备服务器
安装keepalived
````

# 幂等性,redis原子性

```text
使用redis执行 setnx 命令,天热具有幂等性
```

# 优先队列 0~255

```text
从小到大,越大越消费,设置参数,生产中用0~10
```

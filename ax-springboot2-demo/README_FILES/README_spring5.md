# bean作用域

```text
默认是单例
scope="singleton",加载配置文件时候,就创建了实例
scope="prototype",在调用.getBean()方法时候,创建多实例

scope="session"
scope="request"
```

# bean生命周期

```text
1.通过构造器创建bean实例;
2.为bean的属性设置值和对其他bean引用(调用set方法);
3.把bean实例传给bean前置处理器的方法
4.调用bean的初始化方法(需要进行配置初始化的方法);
5.把bean实例传给bean后置处理器的方法
6.bean可以使用了(对象获取到了);
7.当容器关闭时候,调用bean的销毁方法(需要进行配置销毁的方法);
```

# 注解

```text
1.@Component
2.@Service
3.@Controller
4.@Repository
功能一样,无差异,只是业务要求
```

# 注入属性

```text
@Resource:属性,类型都可以
@Autowired:根据属性类型进行自动装配
@Qualifier:根据属性名称,和Autowired 一起使用
@Value: 注入普通类型属性,如String
```

# 完全注解开发,springbootstart 方式

```text
1.config
2.测试类
```

# AOP

```text
1.动态代理有接口,JDK动态代理: 创建接口实现类代理对象,增强内增方法
2.没有接口情况,使用CGLIB代理: 创建当前类子类的代理对象,完成增强

```

## 静态代理

```text
代理模式可以在不修改被代理对象的基础上，通过扩展代理类，进行一些功能的附加与增强。值得注意的是，代理类和被代理类应该共同实现一个接口，或者是共同继承某个类。

上面介绍的是静态代理的内容，为什么叫做静态呢？因为它的类型是事先预定好的，比如上面代码中的 Cinema 这个类。下面要介绍的内容就是动态代理。
```

## aop术语

```text
1.连接点:可以被增强的方法;
2.切入点:实际被真正增强的方法;
3.通知(增强):
(1).实际增强的逻辑部分,
(2).通知类型,前置通知,后置通知,环绕通知,异常通知,最终通知
4.切面:把通知应用到切入点的过程;
```

```text
spring框架基于AspectJ实现AOP操作

```

## 切入点表达式

```text
/// 语法结构
execution(权限修饰符,返回值类型,类全路径,方法名称,参数列表)

*修饰符统配
返回值类型可以省略,有个空格,有个空格,有个空格
(..)所有参数
execution(* dao.aop.com.github.axinger.UserDaoImpl.add(..))

// 所有方法
execution(* dao.aop.com.github.axinger.UserDaoImpl.*(..))

// 所有类,所有方法
execution(* com.ax.aop.dao.*.*(..))
```

# 事务

```text
两种方式
1.编程式事务管理
2.声明式事务管理(常用),注解和xml方式

spring事务管理api
1.提供一个接口,代表事务管理器,针对不同的框架提供不同的实现类
```

# spring5 新功能

```text
基于java8,
自带通用日志功能,可以整合第三方日志4j
```

# @Nullable 注解

```text
可以使用再方法上,属性,参数,表示方法返回值可以空,属性值可以空,参数可以空
```

# 响应式编程

# SpringWebflux 基于注解编程模型,异步非阻塞,核心基于Reactor(响应式编程)的相关api实现,Netty容器

```text
springmvc 命令式编程
SpringWebflux 响应式编程,Reactor封装java9的api,
1.Reactor是满足Reactive规范框架
2.核心类: Mono和Flux,实现看接口
Flux对象实现发布者,返回N个元素
Mono发布者,返回0或者1个元素
3.都是数据流发布者,可以发出三种数据信号(元素值,错误信号,完成信号,后两者代表终止信号,
终止信号用于告诉订阅者数据流结束了,错误信号终止,传递给订阅者)
```

```text
错误信号和完成信号都是终止信号,不能共存
```

```text
BIO 阻塞式
NIO 非阻塞
```

### SpringWebflux 基于注解编程模型

```text
和之前springmvc使用相似,只需要把相关依赖配置到项目中,
springboot自动配置相关运行容器,默认Netty容器
```

### SpringWebflux 基于函数式编程模型

```text
1.需要自己初始化服务器;
2.有两个核心接口RouteFunction,HandlerFunction,实现接口,并启动需要的服务器;
3.请求和响应,不是在ServletRequest(3.1),而是ServerRequest

```

### 依赖循环

```text
这是一个经典的循环依赖，但是它能正常运行，得益于spring的内部机制，让我们根本无法感知它有问题，因为spring默默帮我们解决了。
spring内部有三级缓存：
singletonObjects 一级缓存，用于保存实例化、注入、初始化完成的bean实例
earlySingletonObjects 二级缓存，用于保存实例化完成的bean实例
singletonFactories 三级缓存，用于保存bean创建工厂，以便于后面扩展有机会创建代理对象。


```

### 统计时间

```java
@SpringBootTest
public class TotalTimeTests {

    @Test
    public void timeLong(){
        // StopWatch 用来计时的
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        System.out.println("计算时长 = "+watch.getTotalTimeSeconds());
    }
}
```

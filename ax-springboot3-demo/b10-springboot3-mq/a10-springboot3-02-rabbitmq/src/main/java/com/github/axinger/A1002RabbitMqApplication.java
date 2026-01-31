package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * https://blog.csdn.net/weixin_41882200/article/details/117128590
 * https://blog.csdn.net/Yearingforthefuture/article/details/119611721
 * <p>
 * 6.工作模式总结
 * <p>
 * ​ 这五种工作模式，可以归为三类：
 * <p>
 * 生产者，消息队列，一个消费者；
 * 生产者，消息队列，多个消费者；
 * 生产者，交换机，多个消息队列，多个消费者；
 * <p>
 * 7、四种交换器
 * <p>
 * 1、direct 如果路由键完全匹配的话，消息才会被投放到相应的队列。
 * <p>
 * 2、fanout 当发送一条消息到fanout交换器上时，它会把消息投放到所有附加在此交换器上的队列。
 * <p>
 * 3、topic　设置模糊的绑定方式，“*”操作符将“.”视为分隔符，匹配单个字符；“#”操作符没有分块的概念，它将任意“.”均视为关键字的匹配部分，能够匹配多个字符。
 * <p>
 * ​ 4、header headers 交换器允许匹配 AMQP 消息的 header 而非路由键，除此之外，header 交换器和 direct 交换器完全一致，但是性能却差很多，因此基本上不会用到该交换器
 */
@SpringBootApplication
public class A1002RabbitMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(A1002RabbitMqApplication.class, args);
    }

}



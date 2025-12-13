package com.github.axinger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {
    // 定义Sink实现消息的发布及订阅，所有订阅者都可以通过该Sink获取实时最新的消息。
    /*
    解释：

Sinks.Many: 这是一个能够处理多个 Message 类型元素的接收器（Sink）
replay().limit: 确保新订阅者在连接时能够获取到已发布的最后一项数据。这对于那些希望立即获取最新数据的新订阅者来说非常有意义。
     */
    @Bean
    public Sinks.Many<Message> sink() {
        return Sinks.many().replay().limit(1);
    }
}

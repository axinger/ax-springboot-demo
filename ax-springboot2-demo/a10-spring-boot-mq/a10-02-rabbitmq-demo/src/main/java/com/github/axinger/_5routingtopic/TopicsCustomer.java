package com.github.axinger._5routingtopic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 5、主题模式
 * 上面的路由模式是根据路由key进行完整的匹配（完全相等才发送消息），
 * 这里的通配符模式通俗的来讲就是模糊匹配。
 * <p>
 * 符号“#”表示匹配一个或多个词，符号“*”表示匹配一个词。
 * 　　与路由模式相似，但是，主题模式是一种模糊的匹配方式。
 * Routing 之订阅模型-Topic
 */
@Component
public class TopicsCustomer {

    /**
     * 符号“*”表示匹配一个词
     *
     * @param message
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    key = {"user.*"},
                    exchange = @Exchange(name = "topics", type = ExchangeTypes.TOPIC)
            )
    })
    public void receive1(String message) {
        System.out.println("message1 = " + message);
    }


    /**
     * 符号“#”表示匹配一个或多个词
     *
     * @param message
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    key = {"user.#"},
                    exchange = @Exchange(name = "topics", type = ExchangeTypes.TOPIC)
            )
    })
    public void receive2(String message) {
        System.out.println("message2 = " + message);
    }
}

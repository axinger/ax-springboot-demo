package com.github.axinger.config;

import com.github.axinger.util.MqNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyProducer {

    private final RabbitTemplate rabbitTemplate;


    /**
     * 发送延迟消息
     *
     * @param flag    topic 标识，如 "one", "two"
     * @param message 消息内容
     * @param delayMs 延迟毫秒数（TTL）
     */
    public void sendDelayedMessage(String flag, Object message, long delayMs) {
        String sendQueue = MqNames.sendQueue(flag);

        rabbitTemplate.convertAndSend("", sendQueue, message, msg -> {
            msg.getMessageProperties().setExpiration(String.valueOf(delayMs)); // 设置 TTL
            return msg;
        });
        log.info("✅✅✅已发送延迟消息到 [{}]，延迟 {}ms", sendQueue, delayMs);
    }
}

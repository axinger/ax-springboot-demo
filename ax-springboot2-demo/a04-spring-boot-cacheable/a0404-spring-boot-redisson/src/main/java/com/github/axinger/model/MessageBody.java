package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息体 - 用于Redis消息传递
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBody implements Serializable {

    private String id;
    private String content;
    private String sender;
    private LocalDateTime sendTime;
    /**
     * 目标投递时间 — Stream 延迟消息用，消费者只处理 deliverTime <= now 的消息
     */
    private LocalDateTime deliverTime;

    public static MessageBody of(String content) {
        return MessageBody.builder()
                .id(java.util.UUID.randomUUID().toString().substring(0, 8))
                .content(content)
                .sender("demo")
                .sendTime(LocalDateTime.now())
                .build();
    }

    /**
     * 创建带投递时间的消息（Stream 延迟消息用）
     */
    public static MessageBody ofDelayed(String content, long delaySeconds) {
        return MessageBody.builder()
                .id(java.util.UUID.randomUUID().toString().substring(0, 8))
                .content(content)
                .sender("delay-" + delaySeconds + "s")
                .sendTime(LocalDateTime.now())
                .deliverTime(LocalDateTime.now().plusSeconds(delaySeconds))
                .build();
    }

    /**
     * 判断消息是否已到期
     */
    @JsonIgnore
    public boolean isDeliverable() {
        return deliverTime == null || !deliverTime.isAfter(LocalDateTime.now());
    }
}

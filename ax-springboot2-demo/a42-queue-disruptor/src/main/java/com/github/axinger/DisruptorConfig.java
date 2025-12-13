package com.github.axinger;

import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class DisruptorConfig {

    @Bean
    public Disruptor<OrderEvent> disruptor() {
        OrderEventFactory factory = new OrderEventFactory();
        int bufferSize = 1024; // RingBuffer 大小

        Disruptor<OrderEvent> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory());

        // 绑定事件处理器
        disruptor.handleEventsWith(new OrderEventHandler());
        disruptor.start();
        return disruptor;
    }

    @Bean
    public OrderEventProducer orderEventProducer(Disruptor<OrderEvent> disruptor) {
        return new OrderEventProducer(disruptor.getRingBuffer());
    }
}

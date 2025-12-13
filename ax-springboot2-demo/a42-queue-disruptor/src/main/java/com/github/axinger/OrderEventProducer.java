package com.github.axinger;

import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class OrderEventProducer {
    private final RingBuffer<OrderEvent> ringBuffer;

//    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
//        this.ringBuffer = ringBuffer;
//    }

    public void onData(String userId, double price) {
        long sequence = ringBuffer.next();  // 获取下一个序列号
        try {
            OrderEvent orderEvent = ringBuffer.get(sequence); // 获取事件对象
            orderEvent.setOrderId(UUID.randomUUID().toString());
            orderEvent.setUserId(userId);
            orderEvent.setPrice(price);
            orderEvent.setStatus("未支付");
        } finally {
            ringBuffer.publish(sequence);  // 发布事件
        }
    }
}

package com.github.axinger;

import com.lmax.disruptor.EventHandler;

public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        // 处理订单的具体业务逻辑
        processOrder(event);
    }

    private void processOrder(OrderEvent orderEvent) {
        // 模拟订单支付逻辑
        System.out.println("处理订单: " + orderEvent.getOrderId() + ", 用户: " + orderEvent.getUserId() + ", 金额: " + orderEvent.getPrice());

        // 假设订单处理通过后更新订单状态
        orderEvent.setStatus("已支付");

        // 模拟库存扣减逻辑
        reduceInventory(orderEvent);

        System.out.println("订单处理完成: " + orderEvent.getOrderId() + " 状态: " + orderEvent.getStatus());
    }

    private void reduceInventory(OrderEvent orderEvent) {
        // 模拟库存扣减逻辑
        System.out.println("扣减库存: 订单 " + orderEvent.getOrderId());
        // 假设库存扣减成功
    }
}

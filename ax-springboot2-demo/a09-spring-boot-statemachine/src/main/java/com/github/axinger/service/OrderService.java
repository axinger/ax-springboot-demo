package com.github.axinger.service;

import com.github.axinger.model.OrderEvent;
import com.github.axinger.model.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // 自动注入配置好的状态机
    @Autowired
    private StateMachine<OrderState, OrderEvent> stateMachine;

    /**
     * 处理订单支付事件，触发状态机的PAY事件
     */
    public void payOrder() {
        stateMachine.sendEvent(OrderEvent.PAY);
    }

    /**
     * 处理订单发货事件，触发状态机的SHIP事件
     */
    public void shipOrder() {
        stateMachine.sendEvent(OrderEvent.SHIP);
    }

    /**
     * 处理订单送达事件，触发状态机的DELIVER事件
     */
    public void deliverOrder() {
        stateMachine.sendEvent(OrderEvent.DELIVER);
    }

    /**
     * 处理订单取消事件，触发状态机的CANCEL事件
     */
    public void cancelOrder() {
        stateMachine.sendEvent(OrderEvent.CANCEL);
    }
}

package com.github.axinger.service;

import com.github.axinger.model.OrderEvent;
import com.github.axinger.model.OrderState;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

// 定义一个状态机监听器组件
@Component
public class OrderStateMachineListener extends StateMachineListenerAdapter<OrderState, OrderEvent> {

    /**
     * 当状态机的状态发生变化时，此方法会被调用
     *
     * @param from 变化前的状态
     * @param to   变化后的状态
     */
    @Override
    public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
        if (from != null) {
            // 打印状态变化信息
            System.out.println("Order state changed from " + from.getId() + " to " + to.getId());
        } else {
            // 如果是初始状态，打印初始化信息
            System.out.println("Order state initialized to " + to.getId());
        }
    }
}

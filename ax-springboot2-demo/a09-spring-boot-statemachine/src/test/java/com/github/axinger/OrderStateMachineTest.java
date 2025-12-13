package com.github.axinger;

import com.github.axinger.model.OrderEvent;
import com.github.axinger.model.OrderState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 使用Spring Boot测试注解，加载Spring上下文
@SpringBootTest
public class OrderStateMachineTest {

    // 自动注入状态机
    @Autowired
    private StateMachine<OrderState, OrderEvent> stateMachine;

    /**
     * 测试订单支付后的状态转换
     */
    @Test
    public void testOrderPayment() {
        // 启动状态机
        stateMachine.start();
//        stateMachine.startReactively();
        // 发送PAY事件
        stateMachine.sendEvent(OrderEvent.PAY);
        // 验证状态机的当前状态是否为PAID
        assertEquals(OrderState.PAID, stateMachine.getState().getId());
    }
}

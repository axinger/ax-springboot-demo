package com.github.axinger.service;

import com.github.axinger.model.OrderEvent;
import com.github.axinger.model.OrderState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// 定义一个状态机持久化组件
@Component
public class OrderStateMachinePersister implements StateMachinePersist<OrderState, OrderEvent, String> {

    // 模拟一个简单的内存存储，用于存储状态机的上下文信息
    private static final Map<String, StateMachineContext<OrderState, OrderEvent>> STATE_MACHINE_CONTEXT_MAP = new HashMap<>();

    /**
     * 将状态机的上下文信息写入持久化存储
     *
     * @param context    状态机的上下文信息
     * @param contextObj 上下文对象标识
     * @throws Exception 写入过程中可能出现的异常
     */
    @Override
    public void write(StateMachineContext<OrderState, OrderEvent> context, String contextObj) throws Exception {
        STATE_MACHINE_CONTEXT_MAP.put(contextObj, context);
    }

    /**
     * 从持久化存储中读取状态机的上下文信息
     *
     * @param contextObj 上下文对象标识
     * @return 状态机的上下文信息
     * @throws Exception 读取过程中可能出现的异常
     */
    @Override
    public StateMachineContext<OrderState, OrderEvent> read(String contextObj) throws Exception {
        return STATE_MACHINE_CONTEXT_MAP.get(contextObj);
    }
}

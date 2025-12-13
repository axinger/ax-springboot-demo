package com.github.axinger.config;

import com.github.axinger.model.OrderEvent;
import com.github.axinger.model.OrderState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
// 启用状态机功能
@EnableStateMachine
// 继承EnumStateMachineConfigurerAdapter，方便配置基于枚举的状态机
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    /**
     * 配置状态机的基本属性
     *
     * @param config 状态机配置构建器
     * @throws Exception 配置过程中可能出现的异常
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        config
                .withConfiguration()
                // 配置状态机在应用启动时自动启动
                .autoStartup(true);
    }

    /**
     * 配置状态机的状态
     *
     * @param states 状态机状态配置构建器
     * @throws Exception 配置过程中可能出现的异常
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states
                .withStates()
                // 指定状态机的初始状态为CREATED
                .initial(OrderState.CREATED)
                // 配置状态机包含OrderState枚举中定义的所有状态
                .states(EnumSet.allOf(OrderState.class));
    }

    /**
     * 配置状态机的状态转换规则
     *
     * @param transitions 状态机转换配置构建器
     * @throws Exception 配置过程中可能出现的异常
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                // 定义从CREATED状态到PAID状态的转换，触发事件为PAY
                .source(OrderState.CREATED).target(OrderState.PAID).event(OrderEvent.PAY)
                .and()
                .withExternal()
                // 定义从PAID状态到SHIPPED状态的转换，触发事件为SHIP
                .source(OrderState.PAID).target(OrderState.SHIPPED).event(OrderEvent.SHIP)
                .and()
                .withExternal()
                // 定义从SHIPPED状态到DELIVERED状态的转换，触发事件为DELIVER
                .source(OrderState.SHIPPED).target(OrderState.DELIVERED).event(OrderEvent.DELIVER)
                .and()
                .withExternal()
                // 定义从CREATED状态到CANCELLED状态的转换，触发事件为CANCEL
                .source(OrderState.CREATED).target(OrderState.CANCELLED).event(OrderEvent.CANCEL)
                .and()
                .withExternal()
                // 定义从PAID状态到CANCELLED状态的转换，触发事件为CANCEL
                .source(OrderState.PAID).target(OrderState.CANCELLED).event(OrderEvent.CANCEL);
    }
}

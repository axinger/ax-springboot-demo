//package com.github.axinger.service;
//
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.listener.StateMachineErrorListener;
//import org.springframework.stereotype.Component;
//
/// / 定义一个状态机错误监听器组件
//@Component
//public class OrderStateMachineErrorListener implements StateMachineErrorListener {
//
//    /**
//     * 当状态机发生错误时，此方法会被调用
//     * @param stateMachine 发生错误的状态机
//     * @param exception 错误异常信息
//     */
//    @Override
//    public void stateMachineError(StateMachine<?, ?> stateMachine, Exception exception) {
//        // 打印错误信息
//        System.err.println("State machine error: " + exception.getMessage());
//    }
//}

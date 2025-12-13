//package com.github.axinger.config;
//
//import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
//import org.springframework.messaging.Message;
//
//@RocketMQTransactionListener
//public class TransactionListener implements RocketMQLocalTransactionListener {
//    @Override
//    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
//        System.out.println("运行executeLocalTransaction");
//        return RocketMQLocalTransactionState.COMMIT;
//        //  return RocketMQLocalTransactionState.ROLLBACK;
//    }
//
//    @Override
//    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
//        System.out.println("checkLocalTransaction");
//        return RocketMQLocalTransactionState.COMMIT;
//    }
//}

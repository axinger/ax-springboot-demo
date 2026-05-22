//package com.github.axinger.config;
//
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
//import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyTransactionListener implements RocketMQLocalTransactionListener {
//
//    private final RocketMQTemplate rocketMQTemplate;
//
//    public MyTransactionListener(RocketMQTemplate rocketMQTemplate) {
//        this.rocketMQTemplate = rocketMQTemplate;
//    }
//
//
//    private boolean doBusinessLogic() {
//        // 实际的业务逻辑代码
//        // 返回 true 表示成功，false 表示失败
//        return true; // or false based on your business logic
//    }
//
//    private boolean checkBusinessStatus(String messageId) {
//        // 查询本地事务状态的方法
//        // 应该基于实际的业务逻辑实现
//        return true; // or false based on your business logic
//    }
//
//    @Override
//    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        try {
//            // 执行本地事务逻辑，例如更新数据库记录
//            boolean success = doBusinessLogic();
//
//            if (success) {
//                // 如果本地事务成功，返回提交状态
//                return RocketMQLocalTransactionState.COMMIT;
//            } else {
//                // 如果本地事务失败，返回回滚状态
//                return RocketMQLocalTransactionState.ROLLBACK;
//            }
//        } catch (Exception e) {
//            // 如果出现异常，暂时不确定状态，稍后由 RocketMQ 进行回查
//            return RocketMQLocalTransactionState.UNKNOWN;
//        }
//    }
//
//    @Override
//    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
//        // 根据消息 ID 或其他标识查询本地事务状态
//        // 注意：这里应该有幂等性的考虑
//
//        // 假设这里是你的业务逻辑代码
//        boolean isCommitted = checkBusinessStatus("committed");
//
//        if (isCommitted) {
//            return RocketMQLocalTransactionState.COMMIT;
//        } else {
//            return RocketMQLocalTransactionState.ROLLBACK;
//        }
//    }
//
//
//}

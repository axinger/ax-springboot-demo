//package com.github.axinger.config;
//
/// **
// * @author huangjialin
// * @date 2023/1/12
// * @description:
// */
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.producer.LocalTransactionState;
//import org.apache.rocketmq.client.producer.TransactionListener;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
/// **
// * rocketmq 事务消息回调类
// */
//@Slf4j
//@Component
//public class OrderTransactionListener implements TransactionListener {
//
//
//    /**
//     * half消息发送成功后回调此方法，执行本地事务
//     *
//     * @param message 回传的消息，利用transactionId即可获取到该消息的唯一Id
//     * @param arg     调用send方法时传递的参数，当send时候若有额外的参数可以传递到send方法中，这里能获取到
//     * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：回调
//     */
//    @Override
//    @Transactional
//    public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
//        long startTime = System.currentTimeMillis();
//        log.info("开始执行本地事务：订单信息：" + new String(message.getBody()));
//        String msgKey = new String(message.getBody());
//
//        int saveResult;
//        LocalTransactionState state;
//        try {
//            //修改为true时，模拟本地事务异常
/// /            boolean imitateException = true;
/// /            if(imitateException) {
/// /                throw new RuntimeException("更新本地事务时抛出异常");
/// /            }
//
//
//            state = 1 == 1 ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
//
//            // 更新本地事务并将事务号持久化，为后续的幂等做准备
//            // TransactionDao.add(transactionId)
//        } catch (Exception e) {
//            log.error("本地事务执行异常，异常信息：", e);
//            state = LocalTransactionState.ROLLBACK_MESSAGE;
//        }
//
//        //修改为true时，模拟本地事务超时，对于超时的消息，rocketmq会调用 checkLocalTransaction 方法回查本地事务执行状况
/// /        boolean imitateTimeout = true;
/// /        if(imitateTimeout){
/// /            state = LocalTransactionState.UNKNOW;
/// /        }
//        log.info("本地事务执行结果：msgKey=" + msgKey + ",execute state:" + state);
//        return state;
//    }
//
//
//    /**
//     * 回查本地事务接口
//     * 当LocalTransactionState.UNKNOW 会进行回查本地事务接口
//     *
//     * @param messageExt 通过获取 transactionId 来判断这条消息的本地事务执行状态
//     * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：回调
//     */
//    @Override
//    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
//        log.info("调用回查本地事务接口：msgKey=" + new String(messageExt.getBody()));
//
//        String msgKey = new String(messageExt.getBody());
//
//
//        LocalTransactionState state = 1 > 0 ? LocalTransactionState.COMMIT_MESSAGE : LocalTransactionState.ROLLBACK_MESSAGE;
//        log.info("调用回查本地事务接口的执行结果：" + state);
//        return state;
//    }
//}

package com.github.axinger.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

// 注解中可以指定线程池参数
// 似乎表明了项目中只能有一个@RocketMQTransactionListener, 不能出现多个。
@RocketMQTransactionListener(corePoolSize = 2, maximumPoolSize = 5)
public class MyTransactionListener implements RocketMQLocalTransactionListener {


    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            System.out.println("RocketMQLocalTransactionListener#executeLocalTransaction arg=" + arg);

            // userService.createAccount();
            //
            // // 更新事务状态为成功
            // MQTxLog mqTxLog = new MQTxLog();
            // mqTxLog.setStatus(1);
            // mqTxLogMapper.update(mqTxLog, Wrappers.<MQTxLog>lambdaUpdate().eq(MQTxLog::getTxNo, arg));
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            // 更新事务状态为失败
            // MQTxLog mqTxLog = new MQTxLog();
            // mqTxLog.setStatus(2);
            // mqTxLogMapper.update(mqTxLog, Wrappers.<MQTxLog>lambdaUpdate().eq(MQTxLog::getTxNo, arg));
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        System.out.println("消息回查");
        // 在数据库中查询事务的处理结果
        // String txNo = msg.getHeaders().get("txNo").toString();
        // MQTxLog mqTxLog = mqTxLogMapper.selectOne(Wrappers.<MQTxLog>lambdaQuery().eq(MQTxLog::getTxNo, txNo));
        // if (mqTxLog == null) {
        //     return RocketMQLocalTransactionState.UNKNOWN;
        // } else {
        //     Integer status = mqTxLog.getStatus();
        //     if (status == 1) {
        //         return RocketMQLocalTransactionState.COMMIT;
        //     } else if (status == 2) {
        //         return RocketMQLocalTransactionState.ROLLBACK;
        //     }
        // }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}

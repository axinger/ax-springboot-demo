package com.github.axinger;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootTest
class A10Rocket2ApplicationTest {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    void contextLoads() {

        String topic = "testTopic";
        Message message = MessageBuilder.withPayload("").build();

//        rocketMQTemplate.sendMessageInTransaction(
//                topic,
//                message,
//                new LocalTransactionExecutor() {
//                    @Override
//                    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//                        // 执行本地事务逻辑
//                        boolean localTransactionSuccess = executeLocalBusinessLogic(msg, arg);
//                        return localTransactionSuccess ? LocalTransactionState.COMMIT : LocalTransactionState.ROLLBACK;
//                    }
//
//                    @Override
//                    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//                        // 检查本地事务状态（可选）
//                        // 根据业务逻辑返回 COMMIT 或 ROLLBACK
//                        return LocalTransactionState.COMMIT; // 假设检查通过，返回 COMMIT
//                    }
//                },
//                null // 附加参数，可以为 null
//        );

    }
}

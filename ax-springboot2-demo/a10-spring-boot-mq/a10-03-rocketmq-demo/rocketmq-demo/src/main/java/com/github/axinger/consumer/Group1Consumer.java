package com.github.axinger.consumer;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Group1Consumer {

    @Component
    @RocketMQMessageListener(
            consumerGroup = Topic.GROUP_1,
            topic = Topic.TOPIC_1,
            selectorExpression = Topic.Tag_1,
//            consumeThreadNumber = 4,
            replyTimeout = 1000,
            consumeTimeout = 1L,                       // 超时默认失败
            selectorType = SelectorType.TAG,          // 过滤类型为标签
            consumeMode = ConsumeMode.CONCURRENTLY,   // 并发消费模式
            messageModel = MessageModel.CLUSTERING,   // 集群模式（默认）
            consumeThreadMax = 4,                    // 最大消费线程数
            maxReconsumeTimes = 5                     // 最大重试次数
    )
    public static class Consumer2 implements RocketMQListener<User> {
        // 监听到消息就会执行此方法

        /// 抛出异常会导致消息重试（相当于NACK），不抛出异常则自动ACK：
        /// 这里只要不抛出异常，就会认为是成功返回CONSUME_SUCCESS，否则返回RECONSUME_LATER，boker会重试，
        /// 从RocketMQ Spring 2.2.0开始，RocketMQ Srping支持Pull模式消费
        @SneakyThrows
        @Override
        public void onMessage(User user) {
            int randomInt = RandomUtil.randomInt(4, 8);
            log.info("收到消息GROUP_1:当前线程池={},user={},randomInt={}", Thread.currentThread().getName(), user.getId(), randomInt);
            if (ObjUtil.equal(user.getId(), 5)) {
                throw new RuntimeException("id5,就任务异常,不处理,重试");
            }
            TimeUnit.SECONDS.sleep(randomInt);
        }
    }


//
//    @Component
//    @RocketMQMessageListener(
//            consumerGroup = "my-group",
//            topic = "my-topic",
//            selectorType = SelectorType.TAG,
//            selectorExpression = "*",
//            consumeMode = ConsumeMode.ORDERLY // 顺序消费才能确保手动ACK有效
//    )
//    public static class ManualAckConsumer implements RocketMQListener<MessageExt>, RocketMQLocalTransactionListener {
//
//        @Override
//        public void onMessage(MessageExt message) {
//            // 处理消息但不自动ACK
//            System.out.println("Received message: " + new String(message.getBody()));
//            // 需要自己实现存储消息和状态，后续在checkLocalTransaction中决定是否ACK
//        }
//
//        @Override
//        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//            // 执行本地事务
//            try {
//                // 业务处理
//                return RocketMQLocalTransactionState.COMMIT; // 提交事务，相当于ACK
//            } catch (Exception e) {
//                return RocketMQLocalTransactionState.ROLLBACK; // 回滚，相当于NACK
//            }
//        }
//
//        @Override
//        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
//            // 检查本地事务状态
//            return RocketMQLocalTransactionState.COMMIT; // 根据实际业务状态返回
//        }
//    }
}

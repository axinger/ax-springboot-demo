package com.github.axinger.consumer;

import com.github.axinger.config.Topic;
import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 问题复现
 * <p>
 * 启动消费者1，消费组为group1，订阅topicA的消息，tag设置为tag1 || tag2
 * 启动消费者2，消费组也为group1，也订阅topicA的消息，但是tag设置为tag3
 * 启动生产者，生产者发送含有tag1,tag2,tag3的消息各10条
 * 消费者1没有收到任何消息，消费者2收到部分消息
 * <p>
 * 先上结论
 * <p>
 * 同一个消费组中，设置不同tag时，后启动的消费者会覆盖先启动的消费者设置的tag
 * tag决定了消息过滤的条件，经过服务端和客户端两层过滤，最后只有后启动的消费者才能收到部分消息
 * <p>
 * 作者：kinnylee
 * 链接：https://juejin.cn/post/6844903865318391821
 * 来源：稀土掘金
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * <p>
 * RocketMQ一个消费组内订阅同一个主题不同的TAG为什么会丢消息
 */

@Slf4j
@Component
public class Group2Consumer {


//    @Component
//    @RocketMQMessageListener(
//            consumerGroup = Topic.GROUP_2,
//            topic = Topic.TOPIC_2,
//            consumeThreadNumber = 4
//    )
//    public static class ConsumerGROUP_2 implements RocketMQListener<User> {
//        @Override
//        public void onMessage(User user) {
//            log.info("收到消息GROUP_2:Tag_2当前线程池={},user={}", Thread.currentThread().getName(), user.getId());
//        }
//    }

    /**
     * 如果多个消费者使用相同的 consumerGroup，但订阅了不同的 tag，可能会出现无法正常接收消息的情况。
     * 这是因为 RocketMQ 的消费组（Consumer Group）和订阅关系的设计机制导致的。
     */
    @Component
    @RocketMQMessageListener(
            consumerGroup = Topic.GROUP_2,
            topic = Topic.TOPIC_2,
            selectorType = SelectorType.TAG,
            selectorExpression = Topic.Tag_2
//            consumeThreadNumber = 4
    )
    public static class ConsumerGROUP_2Tag_2 implements RocketMQListener<User> {
        @Override
        public void onMessage(User user) {
            log.info("收到消息GROUP_2:Tag_2当前线程池={},user={}", Thread.currentThread().getName(), user);
        }
    }

    @Component
    @RocketMQMessageListener(
            consumerGroup = Topic.GROUP_2 + "2",
            topic = Topic.TOPIC_2,
            selectorType = SelectorType.TAG,
//            consumeThreadNumber = 4,
            selectorExpression = Topic.Tag_3
    )
    public static class ConsumerGROUP_2Tag_3 implements RocketMQListener<User> {
        @Override
        public void onMessage(User user) {
            log.info("收到消息GROUP_2:Tag_3当前线程池={},user={}", Thread.currentThread().getName(), user.getId());
        }
    }

    @Component
    @RocketMQMessageListener(
            consumerGroup = Topic.GROUP_2 + "3",
            topic = Topic.TOPIC_2,
            selectorType = SelectorType.TAG,
            selectorExpression = Topic.Tag_2
    )
    public static class ConsumerGROUP_4 implements RocketMQListener<MessageExt> {
        @Override
        public void onMessage(MessageExt messageExt) {
            byte[] body = messageExt.getBody();
            String msg = new String(body);
            System.out.println("msg = " + msg);

            String tags = messageExt.getTags();
            System.out.println("tags = " + tags);

            log.info("收到消息GROUP_4当前线程池={}", Thread.currentThread().getName());
        }
    }

//    @Component
//    @RocketMQMessageListener(
//            consumerGroup = Topic.GROUP_2,
//            topic = Topic.TOPIC_2,
//            selectorType = SelectorType.TAG,
//            selectorExpression = Topic.Tag_3,
//            consumeThreadNumber = 4
//    )
//    public static class ConsumerGROUP_2Tag_3 implements RocketMQListener<User> {
//        @Override
//        public void onMessage(User user) {
//            log.info("收到消息GROUP_2:Tag_3当前线程池={},user={}", Thread.currentThread().getName(), user.getId());
//        }
//    }
}

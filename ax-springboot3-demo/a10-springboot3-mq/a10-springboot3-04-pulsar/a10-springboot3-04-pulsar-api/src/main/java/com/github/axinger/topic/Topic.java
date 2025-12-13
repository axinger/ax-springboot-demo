package com.github.axinger.topic;

public interface Topic {

    String sp = "001";
    String EXCLUSIVE_TOPIC = "ExclusiveTopic" + sp;
    String FAILOVER_TOPIC = "FailoverTopic" + sp;
    String SHARED_TOPIC = "SharedTopic" + sp;
    String KEY_SHARED_TOPIC = "Key_SharedTopic" + sp;


    /**
     * 延迟  subscriptionType = SubscriptionType.Shared, 延迟消息必须是共享模式
     * 会有1秒延迟
     */
    String DELIVER_AFTER_TOPIC = "deliverAfterTopic" + sp;

    /**
     * 定时,转换成时间戳,精确到毫秒
     */
    String DELIVER_AT_TOPIC = "deliverAtTopic" + sp;


    String testTop2 = "user_behavior" + sp;

}

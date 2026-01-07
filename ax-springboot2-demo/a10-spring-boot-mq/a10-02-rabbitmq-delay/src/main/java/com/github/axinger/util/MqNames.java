package com.github.axinger.util;

/**
 * RabbitMQ 资源命名规范工具类
 * 所有名称基于统一模板：business.topic.{flag}.xxx
 */
public final class MqNames {

    private static final String PREFIX = "business.topic.";

    // 私有构造，防止实例化
    private MqNames() {
        throw new UnsupportedOperationException("Utility class");
    }

    // 队列
    public static String sendQueue(String flag) {
        return PREFIX + flag + ".dlq";
    }

    public static String processQueue(String flag) {
        return PREFIX + flag + ".process.queue";
    }

    // 交换机
    public static String dlx(String flag) {
        return PREFIX + flag + ".dlx";
    }

    // 路由键
    public static String routingKey(String flag) {
        return PREFIX + flag + ".routing.key";
    }

}

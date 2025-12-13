package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.topic.Topic;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class A10PulsarApplicationTest {

    static String SERVICE_URL = "";
    static String serviceHttpUrl = "";
    @Autowired
    private PulsarTemplate<String> template;

    public static void send() throws PulsarClientException {
        PulsarClient client = PulsarClient.builder().serviceUrl(SERVICE_URL).build();
        /**
         * 发送方式
         * 默认是字节
         */
        Producer<byte[]> producer = client.newProducer().topic("my-tenant/my-namespace/my-topic").create();
        /**
         * 可以设置成字符串简化
         */
        Producer<String> stringProducer = client.newProducer(Schema.STRING).topic("my-tenant/my-namespace/my-topic").create();

        producer.send("这是一条测试的字节消息".getBytes());
        stringProducer.send("这是一条测试的字符串消息");
        //  producer.close();
        //  stringProducer.close();
        producer.closeAsync().thenRun(() -> System.out.println("Producer 关闭")).exceptionally((ex) -> {
            System.err.println("关闭Producer失败: " + ex);
            return null;
        });
        stringProducer.closeAsync().thenRun(() -> System.out.println("Producer 关闭")).exceptionally((ex) -> {
            System.err.println("关闭stringProducer失败: " + ex);
            return null;
        });
    }

    public static void customer() throws PulsarClientException {
        PulsarClient client = PulsarClient.builder().serviceUrl(SERVICE_URL).build();
        Consumer consumer = client.newConsumer().topic("my-tenant/my-namespace/my-topic").subscriptionName("my-subscription").subscribe();
        while (true) {
            // Wait for a message
            Message msg = consumer.receive();
            try {
                // Do something with the message
                System.out.printf("接受到消息: %s", new String(msg.getData()));
                System.out.println();
                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }
    }

    // API的方式操作新增/删除租户
    public static void addTant() throws PulsarAdminException, PulsarClientException {


        // 1.创建pulsar的Admin管理对象
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().serviceHttpUrl(serviceHttpUrl).build();
        // 2.2 查看当前有那些租户
        List<String> tenants = pulsarAdmin.tenants().getTenants();
        tenants.forEach(System.out::println);
        // 2.基于pulsar的Admin对象进行相关的操作

        // 2.1 创建租户
        Set<String> allowedClusters = new HashSet<>();
        allowedClusters.add("standalone");
        TenantInfo tenantInfo = TenantInfo.builder().allowedClusters(allowedClusters).build();
        pulsarAdmin.tenants().createTenant("my-tenant", tenantInfo);

        // 2.2 查看当前有那些租户
        // List<String> tenants = pulsarAdmin.tenants().getTenants();
        tenants.forEach(System.out::println);

        // 2.3 删除租户操作
        pulsarAdmin.tenants().deleteTenant("my-tenant");

        // 3.关闭管理对象
        pulsarAdmin.close();
    }

    //API方式操作新增/输出Namespace
    public static void addNameSpace() throws PulsarAdminException, PulsarClientException {

        // 1.创建pulsar的Admin管理对象
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().serviceHttpUrl(serviceHttpUrl).build();

        // 2.基于pulsar的Admin对象进行相关的操作

        // 2.1 创建名称空间
        String tenant = "my-tenant";
        String namespace = tenant + "/my-namespace";
        pulsarAdmin.namespaces().createNamespace(namespace);

        // 2.2 获取租户下的名称空间列表
        List<String> namespaces = pulsarAdmin.namespaces().getNamespaces(tenant);
        namespaces.forEach(System.out::println);

        // 2.3 删除名称空间
        pulsarAdmin.namespaces().deleteNamespace(namespace);

        // 3.关闭管理对象
        pulsarAdmin.close();
    }

    public static void topicTest() throws PulsarAdminException, PulsarClientException {

        // 1.创建pulsar的Admin管理对象
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().serviceHttpUrl(serviceHttpUrl).build();

        // 2.基于pulsar的Admin对象进行相关的操作

        // 2.1 创建topic相关操作: 有分区和没有分区, 以及持久化和非持久化
        String tenant = "my-tenant";
        String namespace = tenant + "/my-namespace";
        String nonPartitionedTopicName = "non-persistent://my-tenant/my-namespace/my-non-partitioned-topic";
        String partitionedTopicName = "persistent://my-tenant/my-namespace/my-partitioned-topic";

        // 2.2 创建无分区的topic
        pulsarAdmin.topics().createNonPartitionedTopic(nonPartitionedTopicName);

        // 2.3 创建有分区的topic
        pulsarAdmin.topics().createPartitionedTopic(partitionedTopicName, 3);

        // 2.4 修改有分区的Topic的分区数量
        pulsarAdmin.topics().updatePartitionedTopic(partitionedTopicName, 6);

        // 2.5 查询当前有那些topic。如果一个topic有3个分区，则返回3个带-partition-N后缀的topic
        List<String> topics = pulsarAdmin.topics().getList(namespace);
        System.out.println("topic列表");
        topics.forEach(System.out::println);

        // 2.6 查询当前有分区的topic列表
        List<String> partitionedTopicList = pulsarAdmin.topics().getPartitionedTopicList(namespace);
        System.out.print("有分区的topic");
        partitionedTopicList.forEach(System.out::println);

        // 2.7 查询有分区的Topic，有多少个分区
        int partitions = pulsarAdmin.topics().getPartitionedTopicMetadata(partitionedTopicName).partitions;
        System.out.println(partitions);

        // 2.8 删除无分区的Topic
        pulsarAdmin.topics().delete(nonPartitionedTopicName);

        // 2.9 删除有分区的Topic
        pulsarAdmin.topics().deletePartitionedTopic(partitionedTopicName);


        // 3.关闭管理对象
        pulsarAdmin.close();
    }

    //API方式操作TOPIC

    @Test
    public void test1() throws PulsarClientException {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", 123);
        map.put("item_id", 4444);
        map.put("behavior", "aaa");

        String jsonString = JSON.toJSONString(map);
        MessageId send = template.send(Topic.testTop2, jsonString);
        System.out.println("send = " + send);
    }
}

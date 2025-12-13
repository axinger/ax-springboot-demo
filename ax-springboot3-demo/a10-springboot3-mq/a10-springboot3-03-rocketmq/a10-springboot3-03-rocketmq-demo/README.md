```text
RocketMQ主要有三种发送模式：同步发送、异步发送和单向发送。
同步发送会等待Broker的确认，确保消息发送成功；
异步发送则通过回调处理结果；
而单向发送（sendOneWay）不等待响应，也不保证消息到达Broker。

单向发送：不等待 Broker 的响应（无 ACK 确认）
无阻塞：立即返回，不关心发送结果
最高吞吐：相比同步/异步发送，性能最佳
```

![img.png](img.png)

```text
如果多个消费者使用相同的 consumerGroup，但订阅了不同的 tag，可能会出现无法正常接收消息的情况。这是因为 RocketMQ 的消费组（Consumer Group）和订阅关系的设计机制导致的。
```

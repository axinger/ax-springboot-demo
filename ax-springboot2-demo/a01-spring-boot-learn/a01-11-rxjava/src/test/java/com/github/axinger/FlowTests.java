package com.github.axinger;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

//Java Flow 是官方标准 API，而 Reactor 或 RxJava 是第三方库，提供更丰富的操作符和扩展功能。
//
//应用场景‌
//
//Flow API 适合轻量级异步流处理，如 HTTP 客户端响应解析。
//
//复杂数据流处理（如过滤、转换链）更适合使用 Reactor 或 RxJava。
public class FlowTests {
    public static void main(String[] args) {
        // 创建同步发布者
        Flow.Publisher<Integer> publisher = subscriber -> {
            for (int i = 0; i < 5; i++) {
                subscriber.onNext(i);  // 发送数据
            }
            subscriber.onComplete();   // 标记完成
        };

        // 创建订阅者
        Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);  // 请求无限数据（简化示例）
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("Received: " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Processing completed");
            }
        };

        publisher.subscribe(subscriber);  // 绑定关系
    }

    @SneakyThrows
    @Test
    public void test2() {
        // 创建一个发布者
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        // 创建一个订阅者
        Subscriber<String> subscriber = new Subscriber<>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                // 请求一个数据项
                subscription.request(1);
            }

            @Override
            public void onNext(String item) {
                System.out.println("接收到: " + item);
                // 处理完当前数据项后再次请求下一个
                this.subscription.request(1);

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("完成");
            }
        };

        // 注册订阅者
        publisher.subscribe(subscriber);

        // 发布一些数据项
        publisher.submit("Hello");
        publisher.submit("World");

        // 关闭发布者
        publisher.close();

        // 等待一段时间确保所有消息都被处理
        Thread.sleep(1000);
    }
}

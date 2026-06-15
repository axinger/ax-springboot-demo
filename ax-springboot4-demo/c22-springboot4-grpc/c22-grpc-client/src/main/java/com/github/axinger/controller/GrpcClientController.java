package com.github.axinger.controller;

import com.github.axinger.grpc.proto.HelloReply;
import com.github.axinger.grpc.proto.HelloRequest;
import com.github.axinger.grpc.proto.SimpleGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * gRPC Client 控制器
 * 通过构造函数注入 gRPC stub
 */
@Slf4j
@RestController
@RequestMapping("/grpc")
@RequiredArgsConstructor
public class GrpcClientController {

    private final SimpleGrpc.SimpleBlockingStub simpleBlockingStub;

    /**
     * 一元调用测试
     */
    @GetMapping("/say-hello")
    public Map<String, Object> sayHello(@RequestParam(defaultValue = "World") String name) {
        log.info("发起 gRPC 调用: name={}", name);
        
        try {
            HelloRequest request = HelloRequest.newBuilder()
                    .setName(name)
                    .build();
            
            HelloReply response = simpleBlockingStub.sayHello(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", response.getMessage());
            
            log.info("收到响应: {}", response.getMessage());
            return result;
        } catch (Exception e) {
            log.error("gRPC 调用失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    /**
     * 流式调用测试
     */
    @GetMapping("/stream-hello")
    public Map<String, Object> streamHello(@RequestParam(defaultValue = "World") String name) {
        log.info("发起流式 gRPC 调用: name={}", name);
        
        try {
            HelloRequest request = HelloRequest.newBuilder()
                    .setName(name)
                    .build();
            
            List<String> messages = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(1);
            
            // 使用异步 stub 进行流式调用
            SimpleGrpc.SimpleStub asyncStub = SimpleGrpc.newStub(simpleBlockingStub.getChannel());
            
            asyncStub.streamHello(request, new StreamObserver<HelloReply>() {
                @Override
                public void onNext(HelloReply value) {
                    messages.add(value.getMessage());
                    log.info("收到流消息: {}", value.getMessage());
                }

                @Override
                public void onError(Throwable t) {
                    log.error("流式调用错误", t);
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    log.info("流式调用完成");
                    latch.countDown();
                }
            });
            
            // 等待完成（最多5秒）
            latch.await(5, TimeUnit.SECONDS);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("messages", messages);
            result.put("count", messages.size());
            
            return result;
        } catch (Exception e) {
            log.error("流式 gRPC 调用失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
}

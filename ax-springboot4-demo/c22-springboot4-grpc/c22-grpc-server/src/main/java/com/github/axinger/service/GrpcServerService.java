package com.github.axinger.service;

import com.github.axinger.grpc.proto.HelloReply;
import com.github.axinger.grpc.proto.HelloRequest;
import com.github.axinger.grpc.proto.SimpleGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

/**
 * gRPC 服务实现
 * 使用 @GrpcService 注解标识为 gRPC 服务
 */
@Slf4j
@GrpcService
@Service
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        log.info("收到请求: name={}", request.getName());
        
        // 模拟业务处理
        if (request.getName().startsWith("error")) {
            throw new IllegalArgumentException("无效的名称: " + request.getName());
        }
        
        String message = "Hello ==> " + request.getName();
        HelloReply reply = HelloReply.newBuilder()
                .setMessage(message)
                .build();
        
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
        
        log.info("响应发送完成: {}", message);
    }

    @Override
    public void streamHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        log.info("开始流式响应: name={}", request.getName());
        
        try {
            for (int i = 0; i < 5; i++) {
                String message = "Hello(" + i + ") ==> " + request.getName();
                HelloReply reply = HelloReply.newBuilder()
                        .setMessage(message)
                        .build();
                
                responseObserver.onNext(reply);
                log.info("发送流消息: {}", message);
                
                // 模拟延迟
                Thread.sleep(1000);
            }
            
            responseObserver.onCompleted();
            log.info("流式响应完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            responseObserver.onError(e);
            log.error("流式响应中断", e);
        }
    }
}

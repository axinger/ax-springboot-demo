package com.github.axinger.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.github.axinger.api.*;
import io.grpc.stub.StreamObserver;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Setter
@Slf4j
@Component
@RestController
public class ChatService {

    private final AtomicBoolean reconnecting = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    @GrpcClient("a21-grpc-server")
    private ChatServiceGrpc.ChatServiceStub chatServiceStub;
    @GrpcClient("a21-grpc-server")
    private ChatServiceGrpc.ChatServiceBlockingStub chatServiceBlockingStub;
    private volatile StreamObserver<ChatRequest> requestObserver;
    private volatile boolean connected = false;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        connection();
        // 添加心跳机制
        scheduler.scheduleAtFixedRate(this::sendHeartbeat, 5, 5, TimeUnit.SECONDS);
    }

    private void connection() {
        if (reconnecting.getAndSet(true)) {
            return;
        }

        log.info("尝试建立新连接...");

        try {
            StreamObserver<ChatResponse> responseObserver = new StreamObserver<>() {
                @Override
                public void onNext(ChatResponse response) {
                    if (response.hasHeartbeat()) {
                        log.info("收到心跳响应");
                    } else {
                        log.info("收到服务器消息: {}", response.getMessage());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    log.error("服务器连接错误={}", ExceptionUtil.getRootCauseMessage(t), t);
                    connected = false;
                    reconnect();
                }

                @Override
                public void onCompleted() {
                    log.info("服务器关闭了连接");
                    connected = false;
                    reconnect();
                }
            };

            requestObserver = chatServiceStub.connection(responseObserver);
            ChatRequest request = ChatRequest.newBuilder()
                    .setUserId("jim")
                    .setMessage("我要注册...")
                    .build();
            requestObserver.onNext(request);
            connected = true;
            log.info("连接建立成功");
        } catch (Exception e) {
            log.error("建立连接失败", e);
            reconnect();
        } finally {
            reconnecting.set(false);
        }
    }

    private void reconnect() {
        if (reconnecting.compareAndSet(false, true)) {
            scheduler.schedule(() -> {
                log.info("尝试重新连接...");
                connection();
            }, 5, TimeUnit.SECONDS);
        }
    }

    private void sendHeartbeat() {
        if (!connected) return;

        try {
            ChatRequest heartbeat = ChatRequest.newBuilder()
                    .setHeartbeat(ChatHeartbeat.newBuilder()
                            .setTimestamp(System.currentTimeMillis())
                            .build())
                    .build();
            synchronized (this) {
                if (requestObserver != null) {
                    requestObserver.onNext(heartbeat);
                }
            }
        } catch (Exception e) {
            log.error("发送心跳失败", e);
            connected = false;
            reconnect();
        }
    }

    public boolean sendMessage(String message) {
        ChatRequest chatRequest = ChatRequest.newBuilder()
                .setUserId("jim")
                .setToUserId("jim")
                .setMessage(message).build();

//        try {
//            ChatMessageResponse response = chatServiceBlockingStub
//                    .withDeadlineAfter(5, TimeUnit.SECONDS)
//                    .sendMessage(chatRequest);
//            SendCode sendCode = response.getSendCode();
//            System.out.println("sendCode = " + sendCode);
//            return response.getSendCode() == SendCode.SUCCESS;
//        } catch (StatusRuntimeException e) {
//            log.error("RPC失败: {}", e.getStatus());
//            return false;
//        }


        CompletableFuture<Boolean> future = new CompletableFuture<>();

        chatServiceStub.sendMessage(chatRequest, new StreamObserver<>() {
            @Override
            public void onNext(ChatMessageResponse response) {

                SendCode sendCode1 = response.getSendCode();
                System.out.println("发送消息结果 = " + sendCode1);
                future.complete(response.getSendCode() == SendCode.SUCCESS);
            }

            @Override
            public void onError(Throwable t) {
                log.error("发送消息失败={}", ExceptionUtil.getRootCauseMessage(t), t);
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                log.info("发送消息完成=============");
                // 如果onNext未被调用则完成
                if (!future.isDone()) {
                    future.complete(false);
                }
            }
        });

//        return future.join();
        try {
            return future.get(5, TimeUnit.SECONDS); // 同步等待结果
        } catch (Exception e) {
            log.error("等待结果失败", e);
            return false;
        }
//
//        if (!connected || requestObserver == null) {
//            return false;
//        }
//        try {
//            ChatRequest request = ChatRequest.newBuilder()
//                    .setUserId("jim")
//                    .setMessage(message)
//                    .build();
//            synchronized (this) {
//                if (requestObserver != null) {
//                    requestObserver.onNext(request);
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            log.error("发送消息失败", e);
//            connected = false;
//            reconnect();
//        }
//        return false;
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
        if (requestObserver != null) {
            requestObserver.onCompleted();
        }
    }
}

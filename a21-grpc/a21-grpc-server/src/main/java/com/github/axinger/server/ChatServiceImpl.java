package com.github.axinger.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import com.github.axinger.api.*;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@GrpcService
@Slf4j
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private static final Map<String, ClientSession> activeClients = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();


    @PreDestroy
    public void shutdown() {
        cleanupScheduler.shutdown();
    }

    @Override
    public StreamObserver<ChatRequest> connection(StreamObserver<ChatResponse> responseObserver) {
        return new StreamObserver<>() {
            private String userId;
            private volatile long lastActivityTime = System.currentTimeMillis();

            @Override
            public void onNext(ChatRequest request) {
                lastActivityTime = System.currentTimeMillis();

                if (request.hasHeartbeat()) {
                    handleHeartbeat(responseObserver, request);
                    return;
                }

                userId = request.getUserId();
                handleClientRegistration(userId, responseObserver);

//                ChatResponse chatResponse = ChatResponse.newBuilder()
//                        .setUserId(userId)
//                        .setMessage("服务端收到消息: " + request.getMessage())
//                        .build();
//
//                responseObserver.onNext(chatResponse);
            }

            @Override
            public void onError(Throwable t) {
                log.error("客户端 {} 连接错误: {}", userId, t.getMessage());
                cleanup();
            }

            @Override
            public void onCompleted() {
                log.info("客户端 {} 连接正常关闭", userId);
                cleanup();
            }

            private void cleanup() {
                if (userId != null) {
                    ClientSession session = activeClients.get(userId);
                    if (session != null && session.getObserver() == responseObserver) {
                        activeClients.remove(userId);
                        log.info("移除客户端 {}", userId);
                    }
                }
            }

            private void handleHeartbeat(StreamObserver<ChatResponse> responseObserver, ChatRequest request) {
                log.debug("收到心跳: {}", request.getHeartbeat().getTimestamp());
                responseObserver.onNext(ChatResponse.newBuilder()
                        .setHeartbeat(ChatHeartbeat.newBuilder()
                                .setTimestamp(System.currentTimeMillis())
                                .build())
                        .build());
            }

            private void handleClientRegistration(String userId, StreamObserver<ChatResponse> responseObserver) {
                ClientSession newSession = new ClientSession(responseObserver, lastActivityTime);
                ClientSession oldSession = activeClients.put(userId, newSession);

                if (oldSession != null) {
                    log.info("检测到客户端 {} 的重复连接，关闭旧连接", userId);
                    try {
                        oldSession.getObserver().onError(
                                Status.ALREADY_EXISTS
                                        .withDescription("新连接已建立，关闭旧连接")
                                        .asRuntimeException());
                    } catch (Exception e) {
                        log.warn("关闭旧连接失败", e);
                    }
                }
                log.info("客户端 {} 注册成功", userId);
            }
        };
    }

    @Override
    public void sendMessage(ChatRequest request, StreamObserver<com.github.axinger.api.ChatMessageResponse> responseObserver) {

        String userId = request.getUserId();
        String toUserId = request.getToUserId();
        String message = request.getMessage();

        boolean b = pushToClient(toUserId, message);

        responseObserver.onNext(com.github.axinger.api.ChatMessageResponse.newBuilder().setUserId(userId).setToUserId(toUserId).setSendCode(b ? com.github.axinger.api.SendCode.SUCCESS : SendCode.FAILURE).build());
        responseObserver.onCompleted();
    }

    public boolean pushToClient(String toUserId, String message) {
        ClientSession session = activeClients.get(toUserId);
        if (session == null) {
            log.warn("客户端 {} 不存在或已断开", toUserId);
            return false;
        }

        synchronized (session.getObserver()) {
            try {
                ChatResponse response = ChatResponse.newBuilder()
                        .setToUserId(toUserId)
                        .setMessage(message)
                        .build();
                session.getObserver().onNext(response);
                session.updateLastActivity();
                return true;
            } catch (Exception e) {
                log.error("推送消息到客户端 {} 失败，移除连接", toUserId, e);
                activeClients.remove(toUserId);
                return false;
            }
        }
    }

    private void cleanupInactiveSessions() {
        long now = System.currentTimeMillis();
        long inactiveTimeout = TimeUnit.MINUTES.toMillis(5);

        activeClients.entrySet().removeIf(entry -> {
            if (now - entry.getValue().getLastActivityTime() > inactiveTimeout) {
                try {
                    entry.getValue().getObserver().onError(
                            Status.DEADLINE_EXCEEDED
                                    .withDescription("连接因不活跃被关闭")
                                    .asRuntimeException());
                } catch (Exception e) {
                    log.warn("关闭不活跃连接失败", e);
                }
                log.info("移除不活跃客户端 {}", entry.getKey());
                return true;
            }
            return false;
        });
    }

    @Getter
    @AllArgsConstructor
    private static class ClientSession {
        private final StreamObserver<ChatResponse> observer;
        private volatile long lastActivityTime;

        public void updateLastActivity() {
            this.lastActivityTime = System.currentTimeMillis();
        }
    }
}

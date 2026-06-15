package com.github.axinger.config;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.service.GrpcService;

/**
 * gRPC 全局异常处理器
 * 提供统一的异常处理机制
 */
@Slf4j
@Configuration
public class GrpcExceptionHandler {

    /**
     * 全局 gRPC 异常处理器
     * 将业务异常转换为对应的 gRPC 状态码
     */
    @Bean
    @GlobalServerInterceptor
    public io.grpc.ServerInterceptor exceptionHandlingInterceptor() {
        return new io.grpc.ServerInterceptor() {
            @Override
            public <ReqT, RespT> io.grpc.ServerCall.Listener<ReqT> interceptCall(
                    io.grpc.ServerCall<ReqT, RespT> call,
                    Metadata headers,
                    io.grpc.ServerCallHandler<ReqT, RespT> next) {
                io.grpc.ServerCall.Listener<ReqT> listener = next.startCall(call, headers);
                return new io.grpc.ServerCall.Listener<ReqT>() {
                    @Override
                    public void onMessage(ReqT message) {
                        try {
                            listener.onMessage(message);
                        } catch (Exception e) {
                            handleException(call, e);
                        }
                    }

                    @Override
                    public void onHalfClose() {
                        try {
                            listener.onHalfClose();
                        } catch (Exception e) {
                            handleException(call, e);
                        }
                    }

                    @Override
                    public void onCancel() {
                        listener.onCancel();
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }

                    @Override
                    public void onReady() {
                        listener.onReady();
                    }

                    private void handleException(io.grpc.ServerCall<ReqT, RespT> call, Exception e) {
                        log.error("gRPC 调用异常: method={}, error={}",
                                call.getMethodDescriptor().getFullMethodName(), e.getMessage(), e);

                        Status status;
                        if (e instanceof IllegalArgumentException) {
                            status = Status.INVALID_ARGUMENT.withDescription(e.getMessage());
                        } else if (e instanceof IllegalStateException) {
                            status = Status.FAILED_PRECONDITION.withDescription(e.getMessage());
                        } else if (e instanceof SecurityException) {
                            status = Status.PERMISSION_DENIED.withDescription(e.getMessage());
                        } else if (e instanceof UnsupportedOperationException) {
                            status = Status.UNIMPLEMENTED.withDescription(e.getMessage());
                        } else {
                            status = Status.INTERNAL.withDescription("Internal server error");
                        }

                        call.close(status, new Metadata());
                    }
                };
            }
        };
    }

    /**
     * 日志拦截器
     * 记录所有 gRPC 调用
     */
    @Bean
    @GlobalServerInterceptor
    public io.grpc.ServerInterceptor loggingInterceptor() {
        return new io.grpc.ServerInterceptor() {
            @Override
            public <ReqT, RespT> io.grpc.ServerCall.Listener<ReqT> interceptCall(
                    io.grpc.ServerCall<ReqT, RespT> call,
                    Metadata headers,
                    io.grpc.ServerCallHandler<ReqT, RespT> next) {
                log.info("收到 gRPC 调用: method={}, headers={}",
                        call.getMethodDescriptor().getFullMethodName(), headers);
                return next.startCall(call, headers);
            }
        };
    }
}

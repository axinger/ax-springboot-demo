package com.github.axinger.config;

import com.github.axinger.grpc.proto.SimpleGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

/**
 * gRPC Client 配置
 * 创建 gRPC stub Bean
 */
@Configuration
public class GrpcClientConfig {

    /**
     * 创建 gRPC stub Bean
     * 使用 GrpcChannelFactory 创建通道并绑定到 stub
     */
    @Bean
    public SimpleGrpc.SimpleBlockingStub simpleBlockingStub(GrpcChannelFactory channels) {
        // 使用默认通道配置
        return SimpleGrpc.newBlockingStub(channels.createChannel("default-channel"));
    }
}

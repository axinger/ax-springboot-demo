package com.github.axinger;

import com.github.axinger.grpc.proto.SimpleGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

/**
 * gRPC Client 配置
 */
@Configuration
public class GrpcClientConfig {

    /**
     * 创建 gRPC stub Bean
     */
    @Bean
    public SimpleGrpc.SimpleBlockingStub simpleBlockingStub(GrpcChannelFactory channels) {
        // 使用默认通道配置
        return SimpleGrpc.newBlockingStub(channels.createChannel("default"));
    }
}

package com.github.axinger.server;

import cn.hutool.core.util.StrUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;


@Component
public class NettyServer2 {

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Value("${netty.port:8081}")
    private int port;

    //    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加 HTTP 编解码器，HTTP 协议解析，用于握手阶段
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 10)); // 聚合 HTTP 请求片段
                        // 添加自定义处理器
                        pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
                                System.out.println("收到 HTTP 请求：" + request.uri());


                                // 构建响应
                                FullHttpResponse response = new DefaultFullHttpResponse(
                                        HttpVersion.HTTP_1_1,
                                        HttpResponseStatus.OK
                                );
                                String msg = StrUtil.format("接收到了消息={}", LocalDateTime.now());
                                response.content().writeBytes(msg.getBytes());

                                // 设置响应头
                                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                                // 发送响应
                                ctx.writeAndFlush(response);
                            }
                        });


//                        pipeline.addLast(new SimpleChannelInboundHandler<Message>() {
//
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
//                                System.out.println("message = " + message);
//                            }
//                        });


                    }
                });

        ChannelFuture future = bootstrap.bind(port).sync();
        if (future.isSuccess()) {
            System.out.println("Netty HTTP 服务器启动成功，端口：" + port);
        }
    }

    @PreDestroy
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        System.out.println("Netty 服务器已停止");
    }
}

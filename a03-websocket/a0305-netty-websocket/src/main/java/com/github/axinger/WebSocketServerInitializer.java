package com.github.axinger;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
          .addLast(new HttpServerCodec())
          .addLast(new HttpObjectAggregator(65536))     // 聚合完整 HTTP 请求
          .addLast(new ChunkedWriteHandler())           // 支持大文件/流式响应（可选）
          .addLast(new WebSocketServerProtocolHandler("/ws", null, true)) // ← 关键！自动处理 WebSocket 握手和帧
          .addLast(new WebSocketBusinessHandler());     // 你的业务处理器
    }
}

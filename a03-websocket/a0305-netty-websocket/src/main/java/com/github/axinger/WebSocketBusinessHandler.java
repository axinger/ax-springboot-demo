package com.github.axinger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketBusinessHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 只处理 TextWebSocketFrame（文本消息）
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();
        System.out.println("Received from client: " + text);

        // 回复客户端
        ctx.writeAndFlush(new TextWebSocketFrame("Echo: " + text));
    }

    // 连接建立成功（在 WebSocket 握手完成后触发）
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("Client connected: " + ctx.channel().id());
    }

    // 连接断开
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected: " + ctx.channel().id());
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

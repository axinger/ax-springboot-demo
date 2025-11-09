package com.github.axinger.cnfig;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson2.JSONObject;
import com.github.axinger.model.MessageDTO;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MultiValueMap;
import org.yeauty.pojo.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Configuration
@org.yeauty.annotation.ServerEndpoint(path = "/plc/{userId}", port = "1200")
public class MyWebSocket {

    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static void sendAllMessage(String message) {
        for (Session session : sessionMap.values()) {
            session.sendText(message);
        }
    }

    public static void sendMessage(String toUserId, String msg) {
        Session session1 = sessionMap.get(toUserId);
        if (ObjUtil.isNotNull(session1)) {
            session1.sendText(msg);
        } else {
            throw new RuntimeException("未发现链接信息,无法发送消息");
        }
    }

    @org.yeauty.annotation.BeforeHandshake
    public void handshake(Session session,
                          io.netty.handler.codec.http.HttpHeaders headers,
                          @org.yeauty.annotation.RequestParam(name = "age") String age,
                          @org.yeauty.annotation.RequestParam MultiValueMap<String, Object> reqMap,
                          @org.yeauty.annotation.PathVariable(name = "userId") String userId,
                          @org.yeauty.annotation.PathVariable Map<String, Object> pathMap) {
        System.out.println("1111111111111111111111");

        session.setSubprotocols("stomp");
//        if (!"ok".equals(req)){
//            System.out.println("Authentication failed!");
//            session.close();
//        }
        String sessionId = session.id().asShortText();
        System.out.println("sessionId = " + sessionId);
        System.out.println("headers = " + headers);
        System.out.println("token = " + headers.get("token"));
        System.out.println("age = " + age);
        System.out.println("userId = " + userId);
        System.out.println("reqMap = " + reqMap);
        System.out.println("pathMap = " + pathMap);

    }

    @org.yeauty.annotation.OnOpen
    public void onOpen(
            Session session,
            io.netty.handler.codec.http.HttpHeaders headers,
            @org.yeauty.annotation.RequestParam(name = "age") String age,
            @org.yeauty.annotation.RequestParam MultiValueMap<String, Object> reqMap,
            @org.yeauty.annotation.PathVariable(name = "userId") String userId,
            @org.yeauty.annotation.PathVariable Map<String, Object> pathMap) {
        System.out.println("222222222222222222");
        System.out.println("headers = " + headers);
        System.out.println("token = " + headers.get("token"));
        System.out.println("age = " + age);
        System.out.println("userId = " + userId);
        System.out.println("reqMap = " + reqMap);
        System.out.println("pathMap = " + pathMap);

        sessionMap.put(userId, session);
        io.netty.channel.Channel channel = session.channel();
        String longText = channel.id().asLongText();


    }

    @org.yeauty.annotation.OnClose
    public void onClose(Session session) {
        System.out.println("one connection closed");
    }

    @org.yeauty.annotation.OnError
    public void onError(Session session, Throwable throwable) {

        log.error(throwable.getMessage());
    }

    @org.yeauty.annotation.OnMessage
    public void onMessage(Session session,
                          @org.yeauty.annotation.PathVariable(name = "userId") String userId,
                          String message) {
        System.out.println("onMessage=================");
        System.out.println("userId = " + userId);
        System.out.println(message);
        MessageDTO messageDTO = JSONObject.parseObject(message, MessageDTO.class);
        System.out.println("messageDTO = " + messageDTO);

        if (ObjUtil.isNotEmpty(messageDTO.getToUserId())) {
            Session session1 = sessionMap.get(messageDTO.getToUserId());
            session1.sendText(message);
        }

    }


    @org.yeauty.annotation.OnBinary
    public void onBinary(Session session, byte[] bytes) {
        for (byte b : bytes) {
            System.out.println("onBinary = " + b);
        }
        session.sendBinary(bytes);
    }

    @org.yeauty.annotation.OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("read idle");
                    break;
                case WRITER_IDLE:
                    System.out.println("write idle");
                    break;
                case ALL_IDLE:
                    System.out.println("all idle");
                    break;
                default:
                    break;
            }
        }
    }

}

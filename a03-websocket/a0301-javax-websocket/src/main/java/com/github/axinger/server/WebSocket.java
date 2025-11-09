package com.github.axinger.server;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    /**
     * Redis触发监听名字
     */
    public static final String REDIS_TOPIC_NAME = "socketHandler";
    /**
     * 线程安全Map
     */
    public static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();


    //==========【websocket接受、推送消息等方法 —— 具体服务节点推送ws消息】========================================================================================
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            sessionPool.put(userId, session);
            log.debug("【系统 WebSocket】有新的连接，总数为:" + sessionPool.size());
        } catch (Exception e) {
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        try {
            sessionPool.remove(userId);
            log.error("【系统 WebSocket】连接断开，总数为:" + sessionPool.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ws推送消息
     *
     * @param userId
     * @param message
     */
    public void pushMessage(String userId, String message) {
        for (Map.Entry<String, Session> item : sessionPool.entrySet()) {
            //userId key值= {用户id + "_"+ 登录token的md5串}
            //TODO vue2未改key新规则，暂时不影响逻辑
            if (item.getKey().contains(userId)) {
                Session session = item.getValue();
                try {
                    //update-begin-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                    synchronized (session) {
                        log.debug("【系统 WebSocket】推送单人消息:" + message);
                        session.getBasicRemote().sendText(message);
                    }
                    //update-end-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * ws遍历群发消息
     */
    public void pushMessage(String message) {
        try {
            for (Map.Entry<String, Session> item : sessionPool.entrySet()) {
                try {
                    item.getValue().getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            log.debug("【系统 WebSocket】群发消息:" + message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * ws接受客户端消息
     */
    @OnMessage
    public void onMessage(String data, @PathParam(value = "userId") String userId) {
        if (!"ping".equals(data)) {
            log.info("【系统 WebSocket】收到客户端消息:" + data);

            JSONObject object = JSONObject.parseObject(data);

            String toId = object.getString("toId");
            String msg = object.getString("msg");
            pushMessage(toId, msg);

        } else {
            log.info("【系统 WebSocket】收到客户端消息:" + data);
            pushMessage(userId, "pong");
        }

//        //------------------------------------------------------------------------------
//        JSONObject obj = new JSONObject();
//        //业务类型
//        obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_CHECK);
//        //消息内容
//        obj.put(WebsocketConst.MSG_TXT, "心跳响应");
//        this.pushMessage(userId, obj.toJSONString());
//        //------------------------------------------------------------------------------
    }

    /**
     * 配置错误信息处理
     *
     * @param session
     * @param t
     */
    @OnError
    public void onError(Session session, Throwable t) {
        log.warn("【系统 WebSocket】消息出现错误");
        t.printStackTrace();
    }
    //==========【系统 WebSocket接受、推送消息等方法 —— 具体服务节点推送ws消息】========================================================================================

}

package com.github.axinger.hands;

import cn.hutool.http.HttpUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

import static org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor.HTTP_SESSION_ID_ATTR_NAME;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MyHandshakeInterceptor.java
 * @description TODO
 * @createTime 2022年06月11日 16:42:00
 */
@Component
@Slf4j
public class MyHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest serverHttpRequest) {
            String url = request.getURI().toString();
            HttpHeaders headers = request.getHeaders();
            Map<String, String> param = HttpUtil.decodeParamMap(url, StandardCharsets.UTF_8);
            String userId = param.get("userId");
            System.out.println("当前session的ID=" + userId);
            HttpSession session = serverHttpRequest.getServletRequest().getSession();

            attributes.put("sessionId", session.getId());
            attributes.put(HTTP_SESSION_ID_ATTR_NAME, session.getId());
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String element = attributeNames.nextElement();
                System.out.println("element = " + element);
            }


            log.info("这里拦截判断权限 userId = {},session = {}", userId, session);
            if (Optional.ofNullable(userId).isPresent()) {
                attributes.put("WEBSOCKET_USERID", userId);
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("afterHandshake = ");
    }
}

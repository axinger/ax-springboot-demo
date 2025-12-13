package com.github.axinger.config;

import cn.hutool.http.HttpUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    //进入hander之前的拦截
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest serverHttpRequest) {
            String url = request.getURI().toString();
            HttpHeaders headers = request.getHeaders();
            Map<String, String> param = HttpUtil.decodeParamMap(url, StandardCharsets.UTF_8);
            String userId = param.get("userId");
            System.out.println("当前session的ID=" + userId);
            HttpSession session = serverHttpRequest.getServletRequest().getSession();
            log.info("这里拦截判断权限 userId = {},session = {}", userId, session);
            attributes.put("WEBSOCKET_USERID", userId);
        }
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, Exception e) {
        System.out.println("进来webSocket的afterHandshake拦截器！");
    }
}

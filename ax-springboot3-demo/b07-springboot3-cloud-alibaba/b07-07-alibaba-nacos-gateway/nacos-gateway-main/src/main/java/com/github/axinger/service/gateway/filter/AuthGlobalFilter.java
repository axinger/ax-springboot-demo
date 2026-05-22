package com.github.axinger.service.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName AuthGlobalFilter.java
 * @Description TODO
 * @createTime 2022年02月10日 20:14:00
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 提取request请求内容
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();
        String scheme = request.getURI().getScheme();
        HttpMethod method = request.getMethod();
        HttpHeaders httpHeaders = request.getHeaders();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        // 不建议提取body数据，因为请求体数据只能被消费一次。
        log.info("\npath:{}\nscheme:{}\nmethod:{}\nheaders:{}\nremoteAddress:{}",
                path, scheme, method, httpHeaders, remoteAddress);

        // header增加token
        ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(headers -> {
            headers.add("X-Gatewaw-Token", "2022ABC");
            headers.add("X-Gatewaw-Token", "2022ABC");
        }).build();

        // 计算请求时长
        exchange.getAttributes().put("START_TIME", System.currentTimeMillis());

        return chain.filter(exchange.mutate().request(newRequest).build()).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute("START_TIME");
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                log.info(exchange.getRequest().getURI().getRawPath() + " : " + executeTime + "ms");
            }
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }

}

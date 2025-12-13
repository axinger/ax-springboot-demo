package com.github.axinger.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RefreshScope
@Order(-1)
public class AuthFilter implements GlobalFilter {

    private final PathMatcher pathMatcher = new AntPathMatcher();
    @Value("${whitelist.paths}")
    List<String> whitelistPaths;

    private static Map<String, Object> getRequesttMap(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();
        String scheme = request.getURI().getScheme();
        HttpMethod method = request.getMethod();
        HttpHeaders httpHeaders = request.getHeaders();
        InetSocketAddress remoteAddress = request.getRemoteAddress();

        // 不建议提取body数据，因为请求体数据只能被消费一次。
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("scheme", scheme);
        attributes.put("method", method);
        attributes.put("remoteAddress", remoteAddress);
        attributes.put("path", path);
        attributes.put("headers", httpHeaders);
        return attributes;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 计算请求时长
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("白名单={}", whitelistPaths);
        log.info("请求参数={}", getRequesttMap(exchange));

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        if (isWhitelist(path)) {
            return getComplete(exchange, chain, stopWatch, null);
        }

        // 获取Token
//        String token = getJwtToken(request);
//        if (token == null) {
//            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "登录过期"));
//        }

        // 验证Token并解析权限
        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor("abc123".getBytes()))
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String roles = claims.get("roles", String.class);
//            List<String> authorities = Arrays.asList(roles.split(","));
//
//            // 检查权限是否足够访问当前路径
//            if (!hasPermission(authorities, path)) {
//                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "权限不足"));
//            }

            return getComplete(exchange, chain, stopWatch, "1");
        } catch (Exception e) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token验证失败"));
        }
    }

    public Mono<Void> getComplete(ServerWebExchange exchange, GatewayFilterChain chain, StopWatch stopWatch, String userId) {

        /// header增加token
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .headers(header -> {
                    header.add("x-fetch-gateway-token", "ABC123");
                    header.add("X-User-Id", userId);
                }).build();

        return chain.filter(exchange.mutate().request(newRequest).build()).then(Mono.fromRunnable(() -> {
            stopWatch.stop();
            log.info("请求={}m耗时={}", exchange.getRequest().getURI().getRawPath(), stopWatch.getTotalTimeSeconds());
        }));
    }

    // 判断是否为白名单路径
    private boolean isWhitelist(String path) {
        if (CollectionUtils.isEmpty(whitelistPaths)) {
            return false;
        }
        return whitelistPaths.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    // 自行验证token
    private void validateHToken(String token) throws ResponseStatusException {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token验证失败");
    }


    private boolean hasPermission(List<String> authorities, String path) {
        // 实现权限匹配逻辑，例如根据路径判断所需角色
        if (path.startsWith("/api/admin") && !authorities.contains("ROLE_ADMIN")) {
            return false;
        }
        return true;
    }

    private String getJwtToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


}

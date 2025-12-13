// package com.ax.service.gateway.filter;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.core.io.buffer.DataBufferUtils;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
// import java.nio.charset.StandardCharsets;
// import java.util.Map;
// import java.util.stream.Collectors;
//
/// **
// * @author xing
// * @version 1.0.0
// * @ClassName LoggerFilter.java
// * @Description TODO
// * @createTime 2022年02月10日 20:14:00
// */
//@Slf4j
//@Component
// public class LoggerFilter implements GlobalFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String method = request.getMethodValue();
//        log.info("getPath = {}", request.getPath());
//        log.info("getHeaders = {}", request.getHeaders());
//        log.info("getBody = {}", request.getBody());
//        log.info("method = {}", method);
//        if (HttpMethod.POST.matches(method)) {
//            return DataBufferUtils.join(exchange.getRequest().getBody())
//                    .flatMap(dataBuffer -> {
//                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
//                        dataBuffer.read(bytes);
//                        String bodyString = new String(bytes, StandardCharsets.UTF_8);
//                        logTrace(exchange, bodyString);
//                        exchange.getAttributes().put("POST_BODY", bodyString);
//                        DataBufferUtils.release(dataBuffer);
//                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
//                            DataBuffer buffer = exchange.getResponse().bufferFactory()
//                                    .wrap(bytes);
//                            return Mono.just(buffer);
//                        });
//
//                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
//                                exchange.getRequest()) {
//                            @Override
//                            public Flux<DataBuffer> getBody() {
//                                return cachedFlux;
//                            }
//                        };
//                        return chain.filter(exchange.mutate().request(mutatedRequest)
//                                .build());
//                    });
//        } else if (HttpMethod.GET.matches(method)) {
//            Map m = request.getQueryParams();
//            logTrace(exchange, m.toString());
//        }
//        return chain.filter(exchange);
//    }
//
//    /**
//     * 日志信息
//     *
//     * @param exchange
//     * @param param    请求参数
//     */
//    private void logTrace(ServerWebExchange exchange, String param) {
//        ServerHttpRequest serverHttpRequest = exchange.getRequest();
//        String path = serverHttpRequest.getURI().getPath();
//        String method = serverHttpRequest.getMethodValue();
//        String headers = serverHttpRequest.getHeaders().entrySet()
//                .stream()
//                .map(entry -> "            " + entry.getKey() + ": [" + String.join(";", entry.getValue()) + "]")
//                .collect(Collectors.joining("\n"));
//        log.info("\n" + "----------------             ----------------             ---------------->>\n" +
//                        "HttpMethod : {}\n" +
//                        "Uri        : {}\n" +
//                        "Param      : {}\n" +
//                        "Headers    : \n" +
//                        "{}\n" +
//                        "\"<<----------------             ----------------             ----------------"
//                , method, path, param, headers);
//    }
//
//}

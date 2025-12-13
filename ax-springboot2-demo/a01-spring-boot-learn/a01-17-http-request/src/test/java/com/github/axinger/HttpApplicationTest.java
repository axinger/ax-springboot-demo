package com.github.axinger;

import com.axing.common.response.dto.Result;
import com.github.axinger.server.UserServiceHttp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@SpringBootTest
class HttpApplicationTest {

    @Autowired
    private UserServiceHttp userServiceHttp;
    @Autowired
    private WebClient webClient;

    @Test
    public void http() {
        Result<Map<String, Object>> useToken = userServiceHttp.getUseToken("jim", "123", "abcv");
        System.out.println("useToken = " + useToken);
    }
//
//    @Test
//    public void test1() throws InterruptedException {
//
//
//        Mono<Result<Map<String, Object>>> res = webClient.get()
//                .uri("http://localhost:8080/getUserToken", val -> {
////                    val.queryParam("username", "jim");
////                    val.queryParam("password", "jim");
//
//
//                    MultiValueMap<String, String> map = new HttpHeaders();
//                    map.add("username", "u123");
//                    map.add("password", "p123");
//                    val.queryParams(map);
//                    return val.build();
//                })
//                .header("token", "test_token123")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, resp -> {
//                    System.out.println("4xx客户端错误 = " + resp.statusCode());
//                    return Mono.error(new RuntimeException("自定义错误消息"));
//                })
//                .onStatus(HttpStatus::is5xxServerError, resp -> {
//                    System.out.println("5xx服务器错误 = " + resp.statusCode());
//                    return Mono.error(new RuntimeException("服务器错误消息"));
//                })
////                .toEntity(Result<>.class)
//                .bodyToMono(new ParameterizedTypeReference<>() {
//                });
//
//
//        res.subscribe(val -> {
////            Result<Map<String, Object>> body = val.ge();
////            System.out.println("body = " + body);
//            System.out.println("val = " + val);
//        });
//        Result<Map<String, Object>> body = res.block();
//        System.out.println("body = " + body);
//
//        Thread.sleep(5000);
//    }
}

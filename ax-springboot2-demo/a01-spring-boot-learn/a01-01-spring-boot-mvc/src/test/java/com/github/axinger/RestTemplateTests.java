package com.github.axinger;

import com.axing.common.response.dto.Result;
import com.github.axinger.model.dto.UserPojo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@SpringBootTest
public class RestTemplateTests {

    @Autowired
    private RestTemplate restTemplate;


    @Test
    void test1() {

        ParameterizedTypeReference<Result<List<UserPojo<UserPojo.BookPojo>>>> type = new ParameterizedTypeReference<>() {
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("token", "jim");


        ///  这种方式,不能用泛型
        ResponseEntity<Result> exchange = restTemplate.getForEntity("http://localhost:12022/user/list", Result.class);

        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            Result<List<UserPojo<UserPojo.BookPojo>>> result = exchange.getBody();
            HttpHeaders headers1 = exchange.getHeaders();
            System.out.println("headers1 = " + headers1);
            List<UserPojo<UserPojo.BookPojo>> userPojoList = Optional.ofNullable(result).map(Result::getData).orElse(new ArrayList<>());
            System.out.println("userPojoList = " + userPojoList);
        }
    }


    @Test
    void test2() {

        ParameterizedTypeReference<Result<List<UserPojo<UserPojo.BookPojo>>>> type = new ParameterizedTypeReference<>() {
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("token", "jim");

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Result<List<UserPojo<UserPojo.BookPojo>>>> exchange = restTemplate.exchange("http://localhost:12022/user/list",
                HttpMethod.GET, httpEntity, type);

        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            Result<List<UserPojo<UserPojo.BookPojo>>> result = exchange.getBody();
            HttpHeaders headers1 = exchange.getHeaders();
            System.out.println("headers1 = " + headers1);
            List<UserPojo<UserPojo.BookPojo>> userPojoList = Optional.ofNullable(result).map(Result::getData).orElse(new ArrayList<>());
            System.out.println("userPojoList = " + userPojoList);
        }
    }


    public WebClient getWebClient() {

        Consumer<HttpHeaders> headersConsumer = headers -> {
            headers.add("Accept", "application/json");
            headers.add("Content-Type", "application/json");
            headers.add("token", "jim");
        };


        WebClient webClient = WebClient.builder()
//                .baseUrl("http://localhost:8080")
//                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)")
                .defaultHeaders(headersConsumer)
//                .defaultCookie("ACCESS_TOKEN", "test_token")
                .build();

        System.out.println("webClient = " + webClient);

        return webClient;
    }

    @Test
    void test3() throws InterruptedException {
        Mono<ResponseEntity<String>> entity = getWebClient().get().uri("http://localhost:12022/user/list")
                .header("token", "tom")
                .retrieve()
                .toEntity(String.class);

        entity.subscribe(va -> {
            String body = va.getBody();
            System.out.println("body = " + body);
        });
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    void test4() throws InterruptedException {


        ParameterizedTypeReference<Result<List<UserPojo<UserPojo.BookPojo>>>> type = new ParameterizedTypeReference<>() {
        };

        Mono<ResponseEntity<Result<List<UserPojo<UserPojo.BookPojo>>>>> entity = getWebClient().get()
                .uri("http://localhost:12022/user/list")
                .retrieve()
                /// onStatus(HttpStatus::is2xxSuccessful) 方法本身并不是用来处理返回结果的，
                /// 而是用来检查 HTTP 响应状态码是否属于 2xx 成功范围，并允许你在这个条件为真时执行一些特定的逻辑
                .onStatus(HttpStatus::is2xxSuccessful, clientResponse -> {
                    System.out.println("Received a successful response." + clientResponse.statusCode());
                    return Mono.empty(); // 返回空的Mono，继续处理链
                })
                // 对于4xx客户端错误
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("4xx Client Error: " + clientResponse.statusCode())))
                // 对于5xx服务器端错误
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("5xx Server Error: " + clientResponse.statusCode())))
                // 将响应体映射为 ResponseEntity<Result<List<UserPojo>>>
                .toEntity(type);

        entity.subscribe(val -> {
            HttpStatus statusCode = val.getStatusCode();
            Result<List<UserPojo<UserPojo.BookPojo>>> body = val.getBody();
            System.out.println("statusCode = " + statusCode);
            System.out.println("va1 = " + body);

        });
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    void test5() throws InterruptedException {

        ParameterizedTypeReference<Result<List<UserPojo<UserPojo.BookPojo>>>> type = new ParameterizedTypeReference<>() {
        };

        Mono<Result<List<UserPojo<UserPojo.BookPojo>>>> mono = getWebClient().get().uri("http://localhost:12022/user/list")
                .header("token", "tom")
                .retrieve()

                .onStatus(HttpStatus::is2xxSuccessful, clientResponse -> {
// 当返回2xx状态码时执行的操作
                    System.out.println("Received a successful response." + clientResponse.statusCode());
//                    System.out.println("Received a successful response." + clientResponse.bodyToMono(String.class).block());
                    return Mono.empty(); // 返回空的Mono，继续处理链

                })
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Client Error")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server Error")))

                .bodyToMono(type);


        mono.subscribe(va -> {
            Result<List<UserPojo<UserPojo.BookPojo>>> va1 = va;
            System.out.println("va1 = " + va1);

        });
        TimeUnit.SECONDS.sleep(5);
    }

}

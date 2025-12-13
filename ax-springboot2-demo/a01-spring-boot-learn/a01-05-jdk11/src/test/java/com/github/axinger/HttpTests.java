package com.github.axinger;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName HttpTests.java
 * @description TODO
 * @createTime 2022年07月27日 20:37:00
 */

public class HttpTests {

    @Test
    void test1() throws IOException, InterruptedException {

        String data = "";// json 请求数据
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.baidu.com"))
                .POST(HttpRequest.BodyPublishers.ofString(data, Charset.defaultCharset()))
                .header("Content-Type", "application/json") // 设置头部信息
                .timeout(Duration.ofSeconds(10)) // 设置响应超时时间
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}

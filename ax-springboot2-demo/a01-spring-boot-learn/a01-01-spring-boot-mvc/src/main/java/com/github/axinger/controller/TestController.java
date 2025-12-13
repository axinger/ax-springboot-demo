package com.github.axinger.controller;

import com.github.axinger.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private int count;

    @Autowired
    private CountService countService;
    @Autowired
    private WebClient webClient;

    @GetMapping("/count")
    public Object count1() {
        count++;
        System.out.println("count = " + count);
//        return List.of(count);

        ResponseEntity<Integer> response = ResponseEntity.ok(count);

        System.out.println("response = " + response);
        return response;
    }

    @GetMapping("/count2")
    public Object count2() {
//        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

// 获取请求体 request
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

// 获取响应体 response
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();

// 获取请求头 headers
        Enumeration<String> headerNames = request.getHeaderNames();
// 根据请求体参数从 request 中获取 header 请求头值
        Map<String, String> headers = new HashMap<>();
        if (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }


        return List.of(countService.count());
    }

    // 这使得该路径可以捕获 /b/ 后面的所有内容作为 materialCode 参数值
    @GetMapping("/b/{materialCode:.*}")
    public Object getInfoByCode(@PathVariable("materialCode") String materialCode) {
        return List.of(materialCode);
    }

    //方式一，直接调用create()方法
    @GetMapping("/test1")
    public String test1() {
        Mono<String> mono = webClient
                .get() // GET 请求
                .uri("http://localhost:8080/a/person")  // 请求路径
                .retrieve() // 获取响应体
                .bodyToMono(String.class); //响应数据类型转换
        return "from test1 " + mono.block();
    }
}

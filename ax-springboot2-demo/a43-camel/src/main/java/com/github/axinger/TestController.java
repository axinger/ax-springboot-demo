package com.github.axinger;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ProducerTemplate producerTemplate;

    // 定义端点
    @EndpointInject(value = "direct:processData")
    private Endpoint processDataEndpoint;

//    @Value("${camel.endpoints.process-data}")
//    private String processDataEndpoint;

    @GetMapping("/test")
    public String testRoute() {
        // 发送消息到 direct:processData 端点
        String result = producerTemplate.requestBody(processDataEndpoint, "input data", String.class);
        return "Route executed. Result: " + result;
    }
}

package com.github.axinger.mqtt;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqttTestController {

    /*

    参数名	描述	示例
qos	服务质量等级 (0,1,2)	qos=1
retained	是否保留消息	retained=true
brokerUrl	代理URL	brokerUrl=tcp://localhost:1883
clientId	客户端ID	clientId=myClient1
cleanStart	是否清除会话	cleanStart=true
keepAliveInterval	保活间隔(秒)	keepAliveInterval=60
     */

    @Autowired
    private ProducerTemplate producerTemplate;

    @GetMapping("/send-mqtt")
    public String sendMqttMessage(String message) {
        try {
            // ?retained=true
            producerTemplate.sendBody("paho-mqtt5:testTopic2?qos=1", message);
            return "Message sent to MQTT";
        } catch (Exception e) {
            return "发送失败: " + e.getMessage();
        }
    }
}

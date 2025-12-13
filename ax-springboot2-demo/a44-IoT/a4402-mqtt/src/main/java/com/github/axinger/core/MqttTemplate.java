package com.github.axinger.core;

import com.github.axinger.core.config.MqttProviderConfig;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = MqttProviderConfig.MQTT_MESSAGE_CHANNEL)
public interface MqttTemplate {
    void push(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, @Payload Object data);
}

package com.github.axinger.core.config;

import com.github.axinger.core.bean.MqttProperties;
import com.github.axinger.core.util.MqttUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(MqttConfig.class)
public class MqttProviderConfig {

    public static final String MQTT_MESSAGE_CHANNEL = "mqtt_message_channel";
    private final MqttProperties mqttProperties;

    /**
     * 生产者消息通道。
     */
    @Bean(name = MQTT_MESSAGE_CHANNEL)
    public MessageChannel mqttMessageChannel() {
        return new DirectChannel();
    }


    /**
     * 定义 Mqtt 消息处理程序 Bean。
     */
    @Bean
    @ServiceActivator(inputChannel = MQTT_MESSAGE_CHANNEL)
    public MessageHandler mqttMessageHandler(MqttPahoClientFactory mqttPahoClientFactory) {
        String clientId = MqttUtil.getClientIdAndIp(mqttProperties) + "_provider";
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory);
        handler.setDefaultQos(1);
        handler.setDefaultRetained(false);
        handler.setAsync(false);
        handler.setAsyncEvents(false);
        return handler;
    }
}

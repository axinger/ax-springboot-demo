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
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(MqttConfig.class)
public class MqttConsumerConfig {
    public static final String MQTT_CONSUMER_CHANNEL = "consumerMessageChannel";
    private final MqttProperties mqttProperties;

    /**
     * mqtt输入通道: 消费者
     */
    @Bean(name = MQTT_CONSUMER_CHANNEL)
    public MessageChannel consumerMessageChannel() {
        return new DirectChannel();
    }

    //配置client,监听的topic
    @Bean
    public MessageProducer messageProducer(MqttPahoClientFactory mqttPahoClientFactory) {
        String[] array = mqttProperties.getConsumerTopic().toArray(String[]::new);
        String clientId = MqttUtil.getClientIdAndIp(mqttProperties) + "_consumer";
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttPahoClientFactory, array);
        adapter.setCompletionTimeout(mqttProperties.getCompletionTimeout());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(consumerMessageChannel());
//        adapter.setOutputChannelName("IoTOutput名称");
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = MQTT_CONSUMER_CHANNEL)
    public MessageHandler consumerMessageHandler() {
        return message -> {
            String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
            String msg = message.getPayload().toString();
            // 这里可以处理接收的数据
            log.info("""
                    ----------------------------接收到订阅消息 START---------------------------
                    topic:{}
                    message:{}
                    -----------------------------接收到订阅消息 END-----------------------------
                    """, topic, msg);
        };
    }

}

package com.github.axinger.core.util;

import cn.hutool.core.util.StrUtil;
import com.github.axinger.core.bean.MqttProperties;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class MqttUtil {

    public static String getClientIdAndIp(MqttProperties mqttProperties) {
        String id = mqttProperties.getClientId();
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("MqttConfig错误={}", e.getMessage());
        }
        return StrUtil.format("{}_{}", id, ip);
    }
}

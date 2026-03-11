package com.github.axinger.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherService {

    /**
     * 获取当前时间
     * returnDirect 是否直接返回
     * true:直接返回结果,不走大模型，直接给客户,直接是纯文本结果
     * false: 拿到tool返回的结果，给大模型，最后由大模型回复，会加工添加说明
     */
    @Tool(description = "根据城市名称获取天气预报", returnDirect = false)
    public String getWeatherByCity(String city) {

        Map<String, String> map = Map.of(
                "北京", "1111小雨",
                "上海", "1111大雨",
                "南京", "333天晴"
        );

        return map.getOrDefault(city,"抱歉，未查询到城市");
    }
}

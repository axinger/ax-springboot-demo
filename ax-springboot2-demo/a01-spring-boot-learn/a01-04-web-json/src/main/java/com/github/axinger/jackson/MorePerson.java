package com.github.axinger.jackson;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MorePerson {

    private String id;
    private String name;

    private Map<String, Object> otherProperties = new HashMap<>();

    // 动态属性,不解析
    @com.fasterxml.jackson.annotation.JsonAnySetter  //Fastjson2 能识别jackson的注解
    @com.alibaba.fastjson2.annotation.JSONField(unwrapped = true) // Fastjson2 注解
    @com.alibaba.fastjson.annotation.JSONField(unwrapped = true)
    public void setOtherProperty(String key, Object value) {
        otherProperties.put(key, value);
    }

    @com.fasterxml.jackson.annotation.JsonAnyGetter
    @com.alibaba.fastjson2.annotation.JSONField(unwrapped = true)
    @com.alibaba.fastjson.annotation.JSONField(unwrapped = true)
    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }

}

package com.axing.common.request.log;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class WebLogPojo {

    @JsonProperty(value = "耗时")
    private String duration;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    @JsonProperty(value = "请求方法")
    private String method;

    /**
     * IP 地址
     */
    private String ip;

    @JsonProperty(value = "请求头")
    private Map<String, String> headers;
    /**
     * 请求参数
     */
    @JsonProperty(value = "请求参数")
    @JsonRawValue
    private Object parameter;

    /**
     * 请求体
     */
    @JsonProperty(value = "请求体")
    @JsonRawValue
    private Object body;

    /**
     * 请求返回的结果
     */
    @JsonProperty(value = "返回结果")
    private Object result;

    private Map<String, Object> otherProperties = new HashMap<>();

    @JsonAnySetter
    public void setOtherProperty(String key, Object value) {
        otherProperties.put(key, value);
    }

    @JsonAnySetter
    public void setOtherProperty(Map<String, Object> otherProperties) {
        this.otherProperties = otherProperties;
    }

    @JsonAnyGetter
    public Map<String, Object> getOtherProperties() {
        return otherProperties;
    }
}

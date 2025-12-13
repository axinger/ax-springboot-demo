package com.axing.starter.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "axinger.hello")
public class HelloProperties {

    /**
     * 自定义前缀
     */
    private String prefix = "前缀";

    /**
     * 自定义后缀
     */
    private String suffix = "后缀";

    private String name = "axing";

    private Integer age = 12;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

package com.github.axinger.entity;

import com.alibaba.fastjson2.annotation.JSONField;

//@Data
public class Pig {

    /**
     * 第二个字母大写,JSON解析
     * JSONField 对象解析成json的值
     */
    @JSONField(name = "aName")
    private String aName;
    private String aAge;

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAge() {
        return aAge;
    }

    public void setaAge(String aAge) {
        this.aAge = aAge;
    }
}

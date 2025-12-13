package com.github.axinger.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {

    String string1 = "A";
    String string2 = "B";
    List<String> list;
    List<String> list2;
    Map<String, Object> map1;
    Map<String, Object> map2;
    //    @JsonProperty(value = "aBoolean")//jackson,第二个字母大写,需要指定
    Boolean aBoolean;
    @JsonProperty(value = "aBoolean2")
    Boolean aBoolean2;
    @JsonFormat(pattern = "yyyyMM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    Date date;
    @JsonFormat(pattern = "yyyy年MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy年MM-dd HH:mm:ss")
    LocalDateTime localDateTime;
    LocalDateTime localDateTime2;
    LocalDate localDate2;
    @JsonProperty(value = "aLong")
    Long aLong;
    @JsonProperty(value = "aDouble")
    Double aDouble;
    @JsonProperty(value = "aFloat")
    Float aFloat;
    @JsonProperty(value = "aList")
    List<Object> aList;
    @JsonIgnore
    String jsonIgnore;
    Dog dog;
    private String name;
    private Integer age;
    private transient String aTransient;
    private transient String aaTransient;
}

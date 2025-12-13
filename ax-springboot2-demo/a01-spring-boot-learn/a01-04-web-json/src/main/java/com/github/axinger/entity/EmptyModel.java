package com.github.axinger.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class EmptyModel {

    private Map map;
    private List list;
    private Set set;
    private String string;
    private Double double1;
    private Float float1;
    private Integer integer1;
    private Boolean bool1;
}

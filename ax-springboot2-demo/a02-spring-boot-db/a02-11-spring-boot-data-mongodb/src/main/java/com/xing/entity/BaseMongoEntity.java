package com.xing.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseMongoEntity implements Serializable {

    @Id // ObjectId
    private String id;

    private Date createTime;

    private Date updateTime;

    private Boolean deleted;

    @Transient // 被该注解标注的，将不会被录入到数据库中。只作为普通的javaBean属性
    private Map<String, Object> param = new HashMap<>();
}

package com.xing.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName User.java
 * @description TODO
 * @createTime 2022年07月12日 19:11:00
 */

@Data
@Document(collection = "user")
public class User {


    @MongoId
    private String id;

    //@MongoId
    // private ObjectId id;
    //
    //@MongoId
    // private cn.hutool.core.lang.ObjectId id2;

    //@MongoId
    private Integer age;
    private String name;
    private Date birthday;

    private Dog dog;

}

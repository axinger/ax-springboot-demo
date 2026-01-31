package com.xing.dto;

import lombok.Data;
import lombok.ToString;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "mongoPicture")
public class MongoPicture {

    @Id
    private String id;

    private String name;

    private String contentType;

    private String type;

    private Long size;


    private String md5;

    private Binary content;

    private String status;
}


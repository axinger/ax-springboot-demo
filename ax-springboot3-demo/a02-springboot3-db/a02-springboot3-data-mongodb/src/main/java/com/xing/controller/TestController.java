package com.xing.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestController.java
 * @description TODO
 * @createTime 2022年07月12日 17:21:00
 */

@RestController
public class TestController {


    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/test1")
    public Object test1() {

        BasicDBObject basicDBObject = new BasicDBObject();
        final FindIterable<Document> documents = mongoTemplate.getCollection("student")
                .find(basicDBObject);

        List<Map> list = new ArrayList<>();


        documents.forEach(val -> {

            final Map<String, Object> map = val.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            map.put("id", map.get("_id").toString());
            list.add(map);

        });

        System.out.println("list = " + list);
        return list;
    }
}

package com.github.axinger;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.entity.Point;
import org.junit.jupiter.api.Test;

public class PointTests {

    @Test
    public void test1() throws JsonProcessingException {

        String json = """
                {
                  "xPoint": "1",
                  "yPoint": "2",
                  "zPoint": "3",
                  "createTime":"2025-01-01 12:00:00"
                }
                """;


        try {
            ObjectMapper mapper = new ObjectMapper();
            Point point = mapper.readValue(json, Point.class);
            System.out.println("point = " + point);
        } catch (JsonProcessingException e) {
            System.out.println("e = " + e);
        }


        Point point1 = JSON.parseObject(json, Point.class);
        System.out.println("point1 = " + point1);


        Point point2 = com.alibaba.fastjson.JSON.parseObject(json, Point.class);
        System.out.println("point2 = " + point2);

        Point point3 = JSONUtil.toBean(json, Point.class);
        System.out.println("point3 = " + point3);

    }
}

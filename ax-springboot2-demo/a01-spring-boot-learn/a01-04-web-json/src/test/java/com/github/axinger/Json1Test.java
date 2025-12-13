package com.github.axinger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

public class Json1Test {


    /**
     * 转义解析
     */
    @Test
    public void test1() {

        JSONObject originalJson = new JSONObject();
        originalJson.put("name", "jim");
        originalJson.put("age", 10);


        String jsonString = JSON.toJSONString(originalJson);
        System.out.println(jsonString); // {"name":"jim","age":10}


        String jsonString1 = JSON.toJSONString(jsonString);
        System.out.println(jsonString1); // "{\"name\":\"jim\",\"age\":10}"

        // 1. 先解析外层的字符串（去掉引号）
        String unescapedJson = JSON.parseObject(jsonString1, String.class);
        System.out.println(unescapedJson); //{"name":"jim","age":10}

        // 2. 再解析内部的 JSON
        JSONObject originalJson2 = JSON.parseObject(unescapedJson);
        System.out.println(originalJson2); // 输出: {"name":"jim","age":10}
    }
}

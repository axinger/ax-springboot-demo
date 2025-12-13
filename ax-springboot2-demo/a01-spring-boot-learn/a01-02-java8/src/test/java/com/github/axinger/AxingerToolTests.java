package com.github.axinger;

import com.github.axinger.tool.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AxingerToolTests {

    @Test
    void test1() {
        String template = "姓名={name},年龄={age},性别={sex}";
        Map<String, Object> params = new HashMap<>();
        params.put("name", "jim");
        params.put("age", 20);
        System.out.println(StringUtil.format(template, params));

        System.out.println("===================");
        System.out.println(StringUtil.format("姓名={},年龄={},性别={}", "jim", 20));
    }

}


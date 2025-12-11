package com.github.axinger;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionUtil;
import cn.hutool.extra.expression.engine.aviator.AviatorEngine;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
与模板引擎类似，Hutool针对较为流行的表达式计算引擎封装为门面模式，提供统一的API，去除差异。 现有的引擎实现有：

Aviator
Apache Jexl3
MVEL
JfireEL
Rhino
Spring Expression Language (SpEL)


<!-- https://mvnrepository.com/artifact/com.googlecode.aviator/aviator -->
<!--        不是谷歌的-->
<dependency>
    <groupId>com.googlecode.aviator</groupId>
    <artifactId>aviator</artifactId>
    <version>5.4.3</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.alibaba/qlexpress4 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>qlexpress4</artifactId>
    <version>4.0.6</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.alibaba/QLExpress -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>QLExpress</artifactId>
    <version>3.3.4</version>
</dependency>
        
        
 */
public class 表达式Tests {
    @Test
    void test() {
        final Dict dict = Dict.create()
                .set("a", 100.3)
                .set("b", 45)
                .set("c", -199.100);

        // -143.8
        final Object eval = ExpressionUtil.eval("a-(b-c)", dict);

        System.out.println("eval = " + eval);
    }

    @Test
    void test2() {

        Map<String, Object> map = new HashMap<>();
        map.put("a", 100.3);
        map.put("b", 45);
        map.put("c", -199.100);

        // -143.8
        final Object eval = ExpressionUtil.eval("a-(b-c)", map);

        System.out.println("eval = " + eval);
    }


    @Test
    void test3() {


        final Dict dict = Dict.create()
                .set("a", 100.3)
                .set("b", 45)
                .set("c", -199.100);

        // -143.8
//        ExpressionEngine engine = new SpELEngine();
        ExpressionEngine engine = new AviatorEngine();
        final Object eval = engine.eval("a-(b-c)", dict, List.of(表达式Tests.class));
        System.out.println("eval = " + eval);

    }

    @Test
    void test4() {
        {


            Map<String, Object> map = new HashMap<>();
            {
                Map<String, Object> sub = new HashMap<>();
                sub.put("name", "jim");
                sub.put("age", 11); // 不给值,默认是0
                map.put("student", sub);
            }

            String expression = "student.name == 'jim' && student.age <= 10";
            ExpressionEngine engine = new AviatorEngine();
            final Object eval = engine.eval(expression, map, List.of(表达式Tests.class));
            System.out.println("eval = " + eval);
        }

        {


            Map<String, Object> map = new HashMap<>();
            {
                Map<String, Object> sub = new HashMap<>();
                sub.put("isFlag", true);
                map.put("student", sub);
            }

            String expression = "student.isFlag==true";
            ExpressionEngine engine = new AviatorEngine();
            final Object eval = engine.eval(expression, map, List.of(表达式Tests.class));
            System.out.println("isFlag = " + eval);
        }
    }
}

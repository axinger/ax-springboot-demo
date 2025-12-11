package com.github.axinger;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
https://www.yuque.com/boyan-avfmj/aviatorscript

不需要return, 最后一个值就是返回值,不带;
return xx ; ,需要;

 */
public class 表达式_aviatorTests {

    @Test
    public void test01() {


        // 电商促销规则 - 淘宝/天猫同款
        // 不要return
        String expression1 = """
                if (orderAmount >= 200) {
                orderAmount * 0.8
                } else {
                orderAmount
                }
                """;

        Map<String, Object> env = new HashMap<>();
        env.put("orderAmount", 250.0);

        Object result1 = AviatorEvaluator.execute(expression1, env);
        System.out.println("结果1: " + result1); // 200.0

        // 多条件 if else 用 elsif
        String expression2 = """
                    if (orderAmount >= 200) {
                      return  orderAmount * 0.8;
                    } elsif (orderAmount >= 100) {
                        orderAmount * 0.9
                    } else {
                        orderAmount
                    }
                """;
//
        env.put("orderAmount", 100);
        Object result2 = AviatorEvaluator.execute(expression2, env);
        System.out.println("结果2: " + result2); // 90.0


        String expression3 = """
                                if a > 1000 {
                  println("a is greater than 1000.");
                } elsif a > 100 {
                  println("a is greater than 100.");
                } elsif a > 10 {
                   println("a is greater than 10.");
                } else {
                   println("a is less than 10 ");
                }
                
                """;
        env.put("a", 90);
        Object result3 = AviatorEvaluator.execute(expression3, env);
        System.out.println("结果3: " + result3); // 200.0
    }

    @Test
    public void test02_2() {
        // Compile a script
        Expression script = AviatorEvaluator.getInstance().compile("println('Hello, AviatorScript!');");
        script.execute();

    }

    @Test
    public void test02_3() throws IOException {
        // You can try to test every script in examples folder by changing the file name.
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/statements_return.av");
        Object result = exp.execute();
        System.out.println(result);
    }

    @Test
    public void test02_4() throws IOException {
        // You can try to test every script in examples folder by changing the file name.
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/if.av");
        Object result = exp.execute();
        System.out.println(result);
    }

    @Test
    public void test15() {
        String jsonStr = """
                {
                    "a": {
                        "b": [
                                {
                                    "x": 3
                                },
                                {
                                    "x": 4
                                }
                            ]
                    }
                }
                """;

        Map<String, Object> map = JSONObject.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
        });

        // 结果返回 3
        Object value = AviatorEvaluator.execute("a.b[0]['x']", map);
        System.out.println("value = " + value);
    }
}

package com.github.axinger;

import com.github.axinger.groovy.GroovyUtils;
import groovy.lang.GroovyShell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GroovyTests {
    /**
     * 使用 GroovyShell 执行动态计算
     */
    @Test
    @DisplayName("GroovyShell: 精度计算: 0.1 + 0.2=0.3 ")
    public void calculateWithShell11() {
        // 1. 创建 GroovyShell 实例
        GroovyShell shell = new GroovyShell();

        // 2. 定义要执行的 Groovy 脚本字符串
        String scriptText = """
                [
                    result : 0.1 + 0.2,
                    "msg"    :"精度计算"
                ]
                """;

        // 3. 执行脚本并获取结果
        Object result = shell.evaluate(scriptText);

        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("GroovyUtils: 精度计算: 0.1 + 0.2=0.3 ")
    public void calculateWithShell12() {

        // 1. 定义脚本
        // 这里的 a 和 b 直接对应 Map 中的 key
        String script = """
                [
                    result : a + b,
                    msg:  "检验精度计算${name}"
                    ]
                """;


        // 2. 准备上下文数据
        Map<String, Object> context = new HashMap<>();

        {
            System.out.println("\n使用 double=============================");
            context.put("a", 0.1);
            context.put("b", 0.2);
            context.put("name", "double");
            // 3. 执行
            Object result = GroovyUtils.execute(script, context);
            // 验证精度
            System.out.println("计算结果: " + result);
            System.out.println("结果类型: " + result.getClass().getName());
            if (result instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultMap = (Map<String, Object>) result;
                System.out.println("\n返回结果: " + resultMap);
                if (resultMap.get("result").toString().equals("0.3")) {
                    System.out.println("✅ 精度完美！");
                }
            }
        }

        {
            System.out.println("\n使用 BigDecimal=============================");
            // ✅ 正确写法：显式使用 BigDecimal
            context.put("a", new BigDecimal("0.1"));
            context.put("b", new BigDecimal("0.2"));
            context.put("name", "BigDecimal");
            Object result = GroovyUtils.execute(script, context);
            System.out.println("计算结果: " + result);
            System.out.println("结果类型: " + result.getClass().getName());

            if (result instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultMap = (Map<String, Object>) result;
                System.out.println("\n返回结果: " + resultMap);
                if (resultMap.get("result").toString().equals("0.3")) {
                    System.out.println("✅ 精度完美！");
                }
            }

        }
    }
}

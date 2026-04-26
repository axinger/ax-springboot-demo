package com.github.axinger;

import com.github.axinger.groovy.GroovyUtils;
import groovy.lang.GroovyShell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GroovyTests1 {
    /**
     * 使用 GroovyShell 执行动态计算
     */
    @Test
    @DisplayName("精度计算: 0.1 + 0.2=0.3 ")
    public void calculateWithShell11() {
        // 1. 创建 GroovyShell 实例
        GroovyShell shell = new GroovyShell();

        // 2. 定义要执行的 Groovy 脚本字符串
        String scriptText = "0.1 + 0.2";

        // 3. 执行脚本并获取结果
        Object result = shell.evaluate(scriptText);

        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("精度计算: 0.1 + 0.2=0.3 ")
    public void calculateWithShell12() {

        // 1. 准备上下文数据
        Map<String, Object> context = new HashMap<>();
        context.put("a", 0.1);
        context.put("b", 0.2);

        // ✅ 正确写法：用字符串，Groovy 接收后会视为 BigDecimal
//        context.put("a", "0.1");
//        context.put("b", "0.2");

        // ✅ 正确写法：显式使用 BigDecimal
//        context.put("a", new BigDecimal("0.1"));
//        context.put("b", new BigDecimal("0.2"));

        // 也可以传入其他对象
        context.put("name", "Groovy");

        // 2. 定义脚本
        // 这里的 a 和 b 直接对应 Map 中的 key
        String script = "a + b";

        // 3. 执行
        Object result = GroovyUtils.execute(script, context);

        // 4. 输出结果
        System.out.println("计算结果: " + result);
        System.out.println("结果类型: " + result.getClass().getName());

        // 验证精度
        if (result.toString().equals("0.3")) {
            System.out.println("✅ 精度完美！");
        }
    }
}

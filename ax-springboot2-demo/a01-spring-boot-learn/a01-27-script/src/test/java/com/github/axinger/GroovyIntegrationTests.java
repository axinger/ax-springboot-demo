package com.github.axinger;

import com.github.axinger.groovy.GroovyOptions;
import com.github.axinger.groovy.GroovyResult;
import com.github.axinger.groovy.GroovyScriptEngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroovyIntegrationTests {

    @Autowired
    private GroovyScriptEngineService engine;

    // --- 1. 基础计算 test1 ---
    @Test
    public void test1() {


        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);

        GroovyResult GroovyResult = engine.execute("a + b * c", context, GroovyOptions.builder().build());
        assertEquals(7, GroovyResult.getResult());
    }

    // --- 2. 返回数组和 Map test101 ---
    @Test
    public void test101() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);

        // 返回 List (Groovy 语法: [1, 2])
        GroovyResult res1 = engine.execute("[a, b]", context, GroovyOptions.builder().build());
        System.out.println("返回数组 = " + res1.getResult()); // [1, 2]
        assertTrue(res1.getResult() instanceof java.util.List);

        // 返回 Map (Groovy 语法: [k:v])
        GroovyResult res2 = engine.execute("[value1: a, value2: b]", context, GroovyOptions.builder().build());
        System.out.println("返回Map = " + res2.getResult()); // [value1:1, value2:2]
        assertTrue(res2.getResult() instanceof java.util.Map);
    }

    // --- 3. 变量赋值与回写 test102 ---
    @Test
    public void test102() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 0.1);
        context.put("b", 0.2);

        // 执行赋值语句
        engine.execute("c = a + b; d = 12;", context, GroovyOptions.builder().build());

        System.out.println("更新后的 Context: " + context);
        // Groovy 会自动处理 0.1 + 0.2 的精度（如果是字面量），但这里变量是 Double
        // 结果可能是 0.30000000000000004，除非开启 precise 模式并传入 BigDecimal
        assertNotNull(context.get("c"));
        assertEquals(12, context.get("d"));
    }

    // --- 4. 对象操作与 JSON 转换 test104 ---
    @Test
    public void test104() {
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setA(new BigDecimal("0.1"));
//        productDTO.setB(new BigDecimal("0.2"));
//        productDTO.setProductPrice(10);
//        productDTO.setNumber(5);
//
//        // 模拟 QLExpress 的 JSONObject.from(productDTO)
//        // Hutool 将 Bean 转为 Map
//        Map<String, Object> context = JSONUtil.parseObj(productDTO).toBean(Map.class);
//
//        String script = """
//                total = productPrice * number
//                desc = "Price is: ${productPrice}"
//                """;
//
//        engine.execute(script, context, GroovyOptions.builder().build());
//
//        System.out.println("计算后 Context: " + context);
//        assertEquals(50, context.get("total"));
//
//        // 转回对象
//        ProductDTO GroovyResult Dto = JSONUtil.toBean(JSONUtil.parseObj(context), ProductDTO.class);
//        assertEquals(50, GroovyResult Dto.getTotal());
    }

    // --- 5. 自定义函数 test2 ---
    @Test
    public void test2() {
        Map<String, Object> context = new HashMap<>();

//        // 注入闭包作为函数
//        context.put("join", (groovy.lang.Closure<String>) (String... params) -> String.join(",", params));
//
//        GroovyResult res = engine.execute("join('1', '2', '3')", context, GroovyOptions.builder().build());
//        assertEquals("1,2,3", res.getResult());
    }

    // --- 6. 高精度计算 test6 ---
    @Test
    public void test6() {
        Map<String, Object> context = new HashMap<>();
        // 传入 BigDecimal 以确保精度
        context.put("a", new BigDecimal("0.1"));
        context.put("b", new BigDecimal("0.2"));

        GroovyResult res = engine.execute("a + b == 0.3", context, GroovyOptions.builder().precise(true).build());

        // Groovy 中 BigDecimal + BigDecimal 结果是精确的
        assertTrue((Boolean) res.getResult());
    }

    // --- 7. 安全策略测试 test11 ---
    @Test
    public void test11_security() {
        Map<String, Object> context = new HashMap<>();

        // 尝试执行危险操作 (取决于安全配置)
        // 在 OPEN 模式下，下面这行可能会执行成功（取决于 JVM 权限）
        // 在 WHITE_LIST 模式下，应该会被 SecureASTCustomizer 拦截或限制

//        assertThrows(Exception.class, () -> {
//            engine.execute("class X {}", context, GroovyOptions.builder()
//                    .securityStrategy(SecurityStrategy.WHITE_LIST) // 禁止定义类
//                    .build());
//        });
    }
}
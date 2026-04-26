package com.github.axinger;

import cn.hutool.json.JSONUtil;
import com.github.axinger.dto.ProductDTO;
import com.github.axinger.groovy.*;
import com.github.axinger.service.HelloService;
import groovy.lang.GroovyShell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Groovy 脚本引擎综合测试用例
 * 覆盖常用场景：基础计算、对象操作、函数调用、Spring集成、安全策略等
 */
@SpringBootTest
public class GroovyTests2 {



    @Autowired
    private GroovyScriptEngineService engine;

    @Autowired
    private HelloService helloService;

    // ==================== 1. 基础计算测试 ====================

    @Test
    @DisplayName("基础算术运算: a + b * c")
    public void testBasicArithmetic() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);

        GroovyResult result = engine.execute("a + b * c", context, GroovyOptions.builder().build());

        assertEquals(7, result.result());
        System.out.println("基础计算结果: " + result.result());
    }

    @Test
    @DisplayName("比较运算与逻辑运算")
    public void testComparisonAndLogic() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 10);
        context.put("b", 20);
        context.put("c", 30);

        // 比较运算
        GroovyResult r1 = engine.execute("a < b && b < c", context, GroovyOptions.builder().build());
        assertTrue((Boolean) r1.result());

        // 三元运算
        GroovyResult r2 = engine.execute("a > b ? '大' : '小'", context, GroovyOptions.builder().build());
        assertEquals("小", r2.result());

        // 空安全运算符 (Groovy 特性)
        GroovyResult r3 = engine.execute("x?.toString() ?: '默认值'", context, GroovyOptions.builder().build());
        assertEquals("默认值", r3.result());
    }

    // ==================== 2. 数组/List 和 Map 操作 ====================

    @Test
    @DisplayName("返回 List 和 Map")
    public void testListAndMapReturn() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);

        // 返回 List (Groovy 语法: [1, 2])
        GroovyResult res1 = engine.execute("[a, b, a + b]", context, GroovyOptions.builder().build());
        System.out.println("返回数组 = " + res1.result()); // [1, 2, 3]
        assertTrue(res1.result() instanceof java.util.List);
        assertEquals(Arrays.asList(1, 2, 3), res1.result());

        // 返回 Map (Groovy 语法: [k:v])
        GroovyResult res2 = engine.execute("[value1: a, value2: b, sum: a + b]", context, GroovyOptions.builder().build());
        System.out.println("返回Map = " + res2.result()); // [value1:1, value2:2, sum:3]
        assertTrue(res2.result() instanceof java.util.Map);

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) res2.result();
        assertEquals(1, map.get("value1"));
        assertEquals(2, map.get("value2"));
        assertEquals(3, map.get("sum"));
    }

    @Test
    @DisplayName("List 操作: 过滤、映射、排序")
    public void testListOperations() {
        Map<String, Object> context = new HashMap<>();
        context.put("list", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // 过滤偶数
        GroovyResult r1 = engine.execute("list.findAll { it % 2 == 0 }", context, GroovyOptions.builder().build());
        System.out.println("偶数过滤: " + r1.result());
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), r1.result());

        // 映射: 每个数乘以2
        GroovyResult r2 = engine.execute("list.collect { it * 2 }", context, GroovyOptions.builder().build());
        System.out.println("乘以2: " + r2.result());
        assertEquals(Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), r2.result());

        // 求和
        GroovyResult r3 = engine.execute("list.sum()", context, GroovyOptions.builder().build());
        assertEquals(55, r3.result());

        // 最大值
        GroovyResult r4 = engine.execute("list.max()", context, GroovyOptions.builder().build());
        assertEquals(10, r4.result());
    }

    // ==================== 3. 变量赋值与回写 ====================

    @Test
    @DisplayName("变量赋值与上下文回写 - 精确计算模式")
    public void testVariableAssignment() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 0.1);
        context.put("b", 0.2);

        // 执行赋值语句 - 开启精确模式
        GroovyResult result = engine.execute("c = a + b; d = 12; message = 'Hello Groovy'", context, GroovyOptions.builder()
                .precise(true)
                .build());

        System.out.println("更新后的 Context: " + result.context());

        // 验证回写的变量
        // 在精确模式下，0.1 + 0.2 = 0.3 (BigDecimal 精确计算)
        assertEquals(new BigDecimal("0.3"), result.context().get("c"));
        assertEquals(12, result.context().get("d"));
        assertEquals("Hello Groovy", result.context().get("message"));
    }

    @Test
    @DisplayName("变量赋值与上下文回写 - 非精确计算模式")
    public void testVariableAssignmentNonPrecise() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", 0.1);
        context.put("b", 0.2);

        // 执行赋值语句 - 关闭精确模式（默认）
        GroovyResult result = engine.execute("c = a + b; d = 12; message = 'Hello Groovy'", context, GroovyOptions.builder()
                .precise(false)
                .build());

        System.out.println("更新后的 Context (非精确模式): " + result.context());

        // 在非精确模式下，0.1 + 0.2 = 0.30000000000000004 (Double 浮点误差)
        assertEquals(0.30000000000000004, result.context().get("c"));
        assertEquals(12, result.context().get("d"));
        assertEquals("Hello Groovy", result.context().get("message"));
    }

    @Test
    @DisplayName("复杂对象赋值与修改")
    public void testComplexAssignment() {
        Map<String, Object> context = new HashMap<>();
        context.put("counter", 0);

        String script = """
                counter = counter + 1
                doubled = counter * 2
                items = [1, 2, 3]
                items << counter  // 添加到列表
                data = [name: 'Test', value: counter]
                """;

        GroovyResult result = engine.execute(script, context, GroovyOptions.builder().build());

        assertEquals(1, context.get("counter"));
        assertEquals(2, context.get("doubled"));
        assertTrue(context.get("items") instanceof List);
        assertTrue(context.get("data") instanceof Map);

        @SuppressWarnings("unchecked")
        List<Integer> items = (List<Integer>) context.get("items");
        assertEquals(Arrays.asList(1, 2, 3, 1), items);
    }

    // ==================== 4. 对象操作与 JSON 转换 ====================

    @Test
    @DisplayName("对象操作: ProductDTO 计算总价")
    public void testObjectOperations() {
        ProductDTO product = new ProductDTO();
        product.setProductName("iPhone");
        product.setProductPrice(new BigDecimal("5999.00"));
        product.setNumber(2);
        product.setA(new BigDecimal("0.1"));
        product.setB(new BigDecimal("0.2"));

        // 使用 Hutool 将 Bean 转为 Map
        Map<String, Object> context = JSONUtil.parseObj(product).toBean(Map.class);

        String script = """
                total = productPrice * number
                desc = "产品: ${productName}, 单价: ${productPrice}, 数量: ${number}, 总价: ${total}"
                precisionTest = a + b
                """;

        GroovyResult result = engine.execute(script, context, GroovyOptions.builder()
                .precise(true)
                .build());

        System.out.println("计算后 Context: " + context);

        // 验证计算结果
        assertEquals(new BigDecimal("11998.00"), context.get("total"));
        assertNotNull(context.get("desc"));
        assertTrue(context.get("desc").toString().contains("iPhone"));

        // 验证 BigDecimal 精度
        assertEquals(new BigDecimal("0.3"), context.get("precisionTest"));

        // 转回对象验证
        ProductDTO resultDto = JSONUtil.toBean(JSONUtil.parseObj(context), ProductDTO.class);
        assertEquals(new BigDecimal("11998.00"), resultDto.getTotal());
    }

    @Test
    @DisplayName("嵌套对象操作")
    public void testNestedObjectOperations() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("value", 100);

        Map<String, Object> outer = new HashMap<>();
        outer.put("inner", inner);
        outer.put("multiplier", 2);

        GroovyResult result = engine.execute("inner.value * multiplier", outer, GroovyOptions.builder().build());
        assertEquals(200, result.result());
    }

    // ==================== 5. 自定义函数 (闭包) ====================

    @Test
    @DisplayName("注入闭包作为自定义函数")
    public void testCustomFunction() {
        Map<String, Object> context = new HashMap<>();

        // 注入 join 函数（使用匿名内部类实现 Groovy Closure）
        context.put("join", new groovy.lang.Closure<String>(null) {
            public String doCall(String... params) {
                return String.join(",", params);
            }
        });

        // 注入计算函数（使用匿名内部类实现 Groovy Closure）
        context.put("calculate", new groovy.lang.Closure<BigDecimal>(null) {
            public BigDecimal doCall(BigDecimal price, Integer qty) {
                return price.multiply(new BigDecimal(qty));
            }
        });

        // 测试 join 函数
        GroovyResult r1 = engine.execute("join('1', '2', '3')", context, GroovyOptions.builder().build());
        assertEquals("1,2,3", r1.result());

        // 测试 calculate 函数
        context.put("price", new BigDecimal("99.99"));
        context.put("qty", 3);
        GroovyResult r2 = engine.execute("calculate(price, qty)", context, GroovyOptions.builder().build());
        assertEquals(new BigDecimal("299.97"), r2.result());
    }

    @Test
    @DisplayName("Groovy 闭包定义与使用")
    public void testGroovyClosure() {
        Map<String, Object> context = new HashMap<>();

        String script = """
                square = { x -> x * x }
                isEven = { x -> x % 2 == 0 }
                result1 = square(5)
                result2 = isEven(4)
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertEquals(25, context.get("result1"));
        assertEquals(true, context.get("result2"));
    }

    // ==================== 6. 高精度计算 ====================

    @Test
    @DisplayName("BigDecimal 高精度计算")
    public void testPreciseCalculation() {
        Map<String, Object> context = new HashMap<>();
        context.put("a", new BigDecimal("0.1"));
        context.put("b", new BigDecimal("0.2"));
        context.put("c", new BigDecimal("0.3"));

        // 精确比较
        GroovyResult r1 = engine.execute("a + b == c", context, GroovyOptions.builder()
                .precise(true)
                .build());
        assertTrue((Boolean) r1.result());

        // 复杂计算
        GroovyResult r2 = engine.execute("(a + b) * 10 / 3", context, GroovyOptions.builder()
                .precise(true)
                .build());
        assertEquals(new BigDecimal("1.0"), r2.result());
    }

    @Test
    @DisplayName("金融计算场景")
    public void testFinancialCalculation() {
        Map<String, Object> context = new HashMap<>();
        context.put("principal", new BigDecimal("10000.00"));  // 本金
        context.put("rate", new BigDecimal("0.05"));           // 年利率 5%
        context.put("years", 3);

        String script = """
                // 复利计算
                compoundInterest = principal * ((1 + rate) ** years - 1)
                totalAmount = principal + compoundInterest
                """;

        engine.execute(script, context, GroovyOptions.builder().precise(true).build());

        BigDecimal interest = (BigDecimal) context.get("compoundInterest");
        BigDecimal total = (BigDecimal) context.get("totalAmount");

        System.out.println("利息: " + interest);
        System.out.println("总金额: " + total);

        assertTrue(interest.compareTo(new BigDecimal("1576")) > 0);
        assertTrue(total.compareTo(new BigDecimal("11576")) > 0);
    }

    // ==================== 7. 安全策略测试 ====================

    @Test
    @DisplayName("白名单安全策略: 禁止定义类")
    public void testSecurityWhiteList() {
        Map<String, Object> context = new HashMap<>();

        // 在白名单模式下，定义类应该被限制
        assertThrows(Exception.class, () -> {
            engine.execute("class X {}", context, GroovyOptions.builder()
                    .securityStrategy(SecurityStrategy.WHITE_LIST)
                    .build());
        });
    }

    @Test
    @DisplayName("开放模式: 允许更多操作")
    public void testSecurityOpen() {
        Map<String, Object> context = new HashMap<>();

        // 开放模式下可以执行大多数操作
        GroovyResult result = engine.execute("def x = 10; x * 2", context, GroovyOptions.builder()
                .securityStrategy(SecurityStrategy.OPEN)
                .build());

        assertEquals(20, result.result());
    }

    // ==================== 8. Spring 单例封装测试 ====================

    @Test
    @DisplayName("GroovyScriptEngine 静态方法调用")
    public void testSingletonAccess() {
        Map<String, Object> context = new HashMap<>();
        context.put("x", 100);
        context.put("y", 200);

        // 使用静态方法（类似 QLExpress 的单例模式）
        GroovyResult result = GroovyScriptEngine.execute("x + y", context);
        assertEquals(300, result.result());

        // 使用 eval 快捷方法
        Object evalResult = GroovyScriptEngine.eval("x * y", context);
        assertEquals(20000, evalResult);

        // 使用类型转换的 eval
        Integer intResult = GroovyScriptEngine.eval("x + y", context, Integer.class);
        assertEquals(300, intResult);
    }

    // ==================== 9. Spring Bean 集成 ====================

    @Test
    @DisplayName("在 Groovy 中调用 Spring Bean")
    public void testSpringBeanIntegration() {
        Map<String, Object> context = new HashMap<>();

        // 将 Spring Bean 注入上下文
        context.put("helloService", helloService);
        context.put("name", "World");

        GroovyResult result = engine.execute("helloService.hello(name)", context, GroovyOptions.builder().build());

        assertEquals("Hello, World!", result.result());
    }

    @Test
    @DisplayName("多个 Spring Bean 交互")
    public void testMultipleBeanInteraction() {
        Map<String, Object> context = new HashMap<>();
        context.put("service", helloService);

        String script = """
                greeting = service.hello('Groovy')
                length = greeting.length()
                upper = greeting.toUpperCase()
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertEquals("Hello, Groovy!", context.get("greeting"));
        assertEquals(16, context.get("length"));
        assertEquals("HELLO, GROOVY!", context.get("upper"));
    }

    // ==================== 10. 日期时间操作 ====================

    @Test
    @DisplayName("日期时间计算")
    public void testDateTimeOperations() {
        Map<String, Object> context = new HashMap<>();
        context.put("now", LocalDateTime.now());
        context.put("today", LocalDate.now());
        context.put("birthDate", LocalDate.of(1990, 5, 15));

        String script = """
                age = today.year - birthDate.year
                isAdult = age >= 18
                formatted = now.format(java.time.format.DateTimeFormatter.ofPattern('yyyy-MM-dd'))
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertNotNull(context.get("age"));
        assertEquals(true, context.get("isAdult"));
        assertNotNull(context.get("formatted"));
    }

    @Test
    @DisplayName("日期格式化与解析")
    public void testDateFormatting() {
        Map<String, Object> context = new HashMap<>();
        context.put("dateStr", "2024-03-15");

        GroovyResult result = engine.execute(
                "java.time.LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern('yyyy-MM-dd'))",
                context,
                GroovyOptions.builder().build()
        );

        assertTrue(result.result() instanceof LocalDate);
        assertEquals(LocalDate.of(2024, 3, 15), result.result());
    }

    // ==================== 11. 字符串操作 ====================

    @Test
    @DisplayName("Groovy 字符串操作")
    public void testStringOperations() {
        Map<String, Object> context = new HashMap<>();
        context.put("name", "Alice");
        context.put("text", "Hello, World!");

        String script = """
                // GString 插值
                greeting = "Hello, ${name}!"
                
                // 字符串方法
                reversed = text.reverse()
                upper = text.toUpperCase()
                contains = text.contains('World')
                
                // 正则匹配
                matched = text =~ /Hello.*/
                hasMatch = matched.find()
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertEquals("Hello, Alice!", context.get("greeting"));
        assertEquals("!dlroW ,olleH", context.get("reversed"));
        assertEquals("HELLO, WORLD!", context.get("upper"));
        assertEquals(true, context.get("contains"));
        assertEquals(true, context.get("hasMatch"));
    }

    // ==================== 12. 条件与循环 ====================

    @Test
    @DisplayName("条件语句")
    public void testConditionals() {
        Map<String, Object> context = new HashMap<>();
        context.put("score", 85);

        String script = """
                if (score >= 90) {
                    grade = 'A'
                } else if (score >= 80) {
                    grade = 'B'
                } else if (score >= 60) {
                    grade = 'C'
                } else {
                    grade = 'D'
                }
                passed = score >= 60
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertEquals("B", context.get("grade"));
        assertEquals(true, context.get("passed"));
    }

    @Test
    @DisplayName("循环结构")
    public void testLoops() {
        Map<String, Object> context = new HashMap<>();

        String script = """
                // for 循环
                sum1 = 0
                for (i in 1..10) {
                    sum1 += i
                }
                
                // each 迭代
                items = [1, 2, 3, 4, 5]
                sum2 = 0
                items.each { sum2 += it }
                
                // times
                product = 1
                5.times { product *= (it + 1) }
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertEquals(55, context.get("sum1"));
        assertEquals(15, context.get("sum2"));
        assertEquals(120, context.get("product")); // 5!
    }

    // ==================== 13. 空值处理 ====================

    @Test
    @DisplayName("空值安全操作")
    public void testNullSafety() {
        Map<String, Object> context = new HashMap<>();
        context.put("obj", null);
        context.put("existing", "value");

        String script = """
                // 安全导航操作符
                safe1 = obj?.toString()
                
                // Elvis 操作符
                result1 = safe1 ?: 'default'
                result2 = existing ?: 'default'
                
                // 非空断言
                isNull = (obj == null)
                """;

        engine.execute(script, context, GroovyOptions.builder().build());

        assertNull(context.get("safe1"));
        assertEquals("default", context.get("result1"));
        assertEquals("value", context.get("result2"));
        assertEquals(true, context.get("isNull"));
    }

    // ==================== 14. 边界情况测试 ====================

    @Test
    @DisplayName("空脚本和空上下文")
    public void testEdgeCases() {
        // 空脚本
        GroovyResult r1 = engine.execute("", new HashMap<>(), GroovyOptions.builder().build());
        assertNull(r1.result());

        // null 上下文
        GroovyResult r2 = engine.execute("1 + 1", null, GroovyOptions.builder().build());
        assertEquals(2, r2.result());

        // 纯注释
        GroovyResult r3 = engine.execute("// 这是一个注释\n/* 多行\n注释 */\n42", new HashMap<>(), GroovyOptions.builder().build());
        assertEquals(42, r3.result());
    }

    @Test
    @DisplayName("大数据量计算")
    public void testLargeDataProcessing() {
        Map<String, Object> context = new HashMap<>();
        List<Integer> largeList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            largeList.add(i);
        }
        context.put("data", largeList);

        GroovyResult result = engine.execute(
                "data.findAll { it % 2 == 0 }.collect { it * it }.sum()",
                context,
                GroovyOptions.builder().build()
        );

        // 偶数平方和: 4 + 16 + 36 + ... + 1000000
        assertNotNull(result.result());
        System.out.println("大数据计算结果: " + result.result());
    }
}

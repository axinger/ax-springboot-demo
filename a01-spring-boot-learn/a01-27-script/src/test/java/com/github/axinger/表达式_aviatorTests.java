package com.github.axinger;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class 表达式_aviatorTests {
    /**
     * 修复文本块中的 else if 语法
     */
    public static String fixTextBlock(String textBlock) {
        if (textBlock == null || textBlock.trim().isEmpty()) {
            return textBlock;
        }

        // 1. 移除文本块的起始和结束标记
        String cleaned = textBlock.trim();
        if (cleaned.startsWith("\"\"\"")) {
            // 移除开头的 """
            cleaned = cleaned.substring(3);
            // 移除结尾的 """
            if (cleaned.endsWith("\"\"\"")) {
                cleaned = cleaned.substring(0, cleaned.length() - 3);
            }
        }

        // 2. 修复 else 和 if 之间的换行
        cleaned = cleaned.replaceAll("else\\s*\\n\\s*if", "else if");

        // 3. 移除多余的空格和换行
        cleaned = cleaned.trim().replaceAll("\\s+", " ");

        return cleaned;
    }

    @Test
    public void test01() {


        // 电商促销规则 - 淘宝/天猫同款
//        String rule = """
//                if(orderAmount >= 200) { return orderAmount * 0.8; }
//                else if(orderAmount >= 100) { return orderAmount * 0.9; }
//                else { return orderAmount; }
//                """;
//        String rule = "orderAmount >= 200 ? orderAmount * 0.8 : (orderAmount >= 100 ? orderAmount * 0.9 : orderAmount)";
//
//        Map<String, Object> env = new HashMap<>();
//        env.put("orderAmount", 250.0);
//
//        Double finalPrice = (Double) AviatorEvaluator.execute(rule, env);
//        System.out.println("finalPrice = " + finalPrice);
//        // 结果：200.0 (打8折)


        // 基本 if-else 表达式
//        String expression1 = "if (orderAmount >= 200) { orderAmount * 0.8 } else { orderAmount }";
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

        // 多条件 if-else if-else 不行
        String textBlock = """
                if (orderAmount >= 200) {
                    orderAmount * 0.8
                } else if (orderAmount >= 100) {
                    orderAmount * 0.9
                } else {
                    orderAmount
                }
                """;

//        String expression2 = fixTextBlock(textBlock);
//        String expression2 = "if (orderAmount >= 200) { orderAmount * 0.8 } else if (orderAmount >= 100) { orderAmount * 0.9 } else { orderAmount }";
//        env.put("orderAmount", 150.0);
//        Object result2 = AviatorEvaluator.execute(expression2, env);
//        System.out.println("结果2: " + result2); // 135.0
//
//        env.put("orderAmount", 80.0);
//        Object result3 = AviatorEvaluator.execute(expression2, env);
//        System.out.println("结果3: " + result3); // 80.0
    }


    @Test
    public void test02() {
        // 1. 最简单的表达式求值
        Long result = (Long) AviatorEvaluator.execute("1 + 2 + 3");
        System.out.println("1 + 2 + 3 = " + result);  // 6

        // 2. 使用变量
        Map<String, Object> env = new HashMap<>();
        env.put("x", 10);
        env.put("y", 20);
        Long sum = (Long) AviatorEvaluator.execute("x + y", env);
        System.out.println("x + y = " + sum);  // 30

        // 3. 字符串操作
        env.put("name", "张三");
        String greeting = (String) AviatorEvaluator.execute("'Hello, ' + name", env);
        System.out.println(greeting);  // Hello, 张三

        // 4. 编译后多次执行（性能更好）
        Expression compiled = AviatorEvaluator.compile("a * b + c");

        env.clear();
        env.put("a", 2);
        env.put("b", 3);
        env.put("c", 4);
        System.out.println("2 * 3 + 4 = " + compiled.execute(env));  // 10

        env.put("a", 5);
        env.put("b", 6);
        env.put("c", 7);
        System.out.println("5 * 6 + 7 = " + compiled.execute(env));  // 37
    }

    @Test
    public void test03() {
        Map<String, Object> env = new HashMap<>();

        // 整数
        env.put("intValue", 100);
        System.out.println(AviatorEvaluator.execute("intValue + 50", env));  // 150

        // 浮点数
        env.put("doubleValue", 3.14);
        System.out.println(AviatorEvaluator.execute("doubleValue * 2", env));  // 6.28

        // 字符串
        env.put("str", "Hello");
        System.out.println(AviatorEvaluator.execute("str + ' World'", env));  // Hello World

        // 布尔值
        env.put("flag", true);
        System.out.println(AviatorEvaluator.execute("flag && true", env));  // true

        // nil (null)
        env.put("nullValue", null);
        System.out.println(AviatorEvaluator.execute("nullValue == nil", env));  // true

        // 正则表达式
        env.put("email", "test@example.com");
        Boolean matches = (Boolean) AviatorEvaluator.execute(
                "email =~ /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$/", env);
        System.out.println("Email格式正确: " + matches);  // true

        // BigInteger 和 BigDecimal
        env.put("bigInt", new BigInteger("123456789012345678901234567890"));
        env.put("bigDec", new BigDecimal("123.456"));
        System.out.println(AviatorEvaluator.execute("bigInt + 1", env));
        System.out.println(AviatorEvaluator.execute("bigDec * 2", env));
    }

    @Test
    public void test04() {
        Map<String, Object> env = new HashMap<>();
        env.put("a", 10);
        env.put("b", 3);

        // 加法
        System.out.println("a + b = " + AviatorEvaluator.execute("a + b", env));  // 13

        // 减法
        System.out.println("a - b = " + AviatorEvaluator.execute("a - b", env));  // 7

        // 乘法
        System.out.println("a * b = " + AviatorEvaluator.execute("a * b", env));  // 30

        // 除法
        System.out.println("a / b = " + AviatorEvaluator.execute("a / b", env));  // 3

        // 取模
        System.out.println("a % b = " + AviatorEvaluator.execute("a % b", env));  // 1

        // 负数
        System.out.println("-a = " + AviatorEvaluator.execute("-a", env));  // -10

        // 复杂表达式
        System.out.println("(a + b) * 2 = " +
                AviatorEvaluator.execute("(a + b) * 2", env));  // 26
    }

    @Test
    public void test05() {
        Map<String, Object> env = new HashMap<>();
        env.put("x", 10);
        env.put("y", 20);
        env.put("name", "张三");

        // 等于
        System.out.println("x == 10: " + AviatorEvaluator.execute("x == 10", env));  // true

        // 不等于
        System.out.println("x != y: " + AviatorEvaluator.execute("x != y", env));  // true

        // 大于
        System.out.println("y > x: " + AviatorEvaluator.execute("y > x", env));  // true

        // 小于
        System.out.println("x < y: " + AviatorEvaluator.execute("x < y", env));  // true

        // 大于等于
        System.out.println("x >= 10: " + AviatorEvaluator.execute("x >= 10", env));  // true

        // 小于等于
        System.out.println("y <= 20: " + AviatorEvaluator.execute("y <= 20", env));  // true

        // 字符串比较
        System.out.println("name == '张三': " +
                AviatorEvaluator.execute("name == '张三'", env));  // true
    }


    @Test
    public void test06() {
        Map<String, Object> env = new HashMap<>();
        env.put("a", true);
        env.put("b", false);
        env.put("age", 25);

        // 逻辑与
        System.out.println("a && b: " + AviatorEvaluator.execute("a && b", env));  // false

        // 逻辑或
        System.out.println("a || b: " + AviatorEvaluator.execute("a || b", env));  // true

        // 逻辑非
        System.out.println("!a: " + AviatorEvaluator.execute("!a", env));  // false

        // 复合条件
        System.out.println("age >= 18 && age <= 60: " +
                AviatorEvaluator.execute("age >= 18 && age <= 60", env));  // true

        // 短路运算
        System.out.println("b || (age > 18): " +
                AviatorEvaluator.execute("b || (age > 18)", env));  // true
    }

    @Test
    public void test07() {
        Map<String, Object> env = new HashMap<>();
        env.put("score", 85);
        env.put("age", 17);

        // 基本三元运算
        String result = (String) AviatorEvaluator.execute(
                "score >= 60 ? '及格' : '不及格'", env);
        System.out.println("成绩: " + result);  // 及格

        // 嵌套三元运算
        String grade = (String) AviatorEvaluator.execute(
                "score >= 90 ? '优秀' : (score >= 80 ? '良好' : (score >= 60 ? '及格' : '不及格'))",
                env);
        System.out.println("等级: " + grade);  // 良好

        // 条件判断
        String status = (String) AviatorEvaluator.execute(
                "age >= 18 ? '成年' : '未成年'", env);
        System.out.println("状态: " + status);  // 未成年
    }

    /// 4.2 字符串函数
    @Test
    public void test08() {
        // abs - 绝对值
        System.out.println("abs(-10) = " + AviatorEvaluator.execute("math.abs(-10)"));  // 10

        // sqrt - 平方根
        System.out.println("sqrt(16) = " + AviatorEvaluator.execute("math.sqrt(16)"));  // 4.0

        // pow - 幂运算
        System.out.println("pow(2, 3) = " + AviatorEvaluator.execute("math.pow(2, 3)"));  // 8.0

        // round - 四舍五入
        System.out.println("round(3.6) = " + AviatorEvaluator.execute("math.round(3.6)"));  // 4

        // floor - 向下取整
        System.out.println("floor(3.9) = " + AviatorEvaluator.execute("math.floor(3.9)"));  // 3

        // ceil - 向上取整
        System.out.println("ceil(3.1) = " + AviatorEvaluator.execute("math.ceil(3.1)"));  // 4

        // max/min - 最大最小值
        System.out.println("max(10, 20, 5) = " +
                AviatorEvaluator.execute("math.max(10, 20, 5)"));  // 20
        System.out.println("min(10, 20, 5) = " +
                AviatorEvaluator.execute("math.min(10, 20, 5)"));  // 5

        // log - 对数
        System.out.println("log(10) = " + AviatorEvaluator.execute("math.log(10)"));

        // sin/cos/tan - 三角函数
        System.out.println("sin(0) = " + AviatorEvaluator.execute("math.sin(0)"));  // 0.0
    }

    /// 4.3 集合函数
    @Test
    public void test09() {
        Map<String, Object> env = new HashMap<>();
        env.put("list", Arrays.asList(1, 2, 3, 4, 5));
        env.put("map", new HashMap<String, Object>() {{
            put("name", "张三");
            put("age", 25);
        }});

        // count - 集合大小
        System.out.println("count: " +
                AviatorEvaluator.execute("count(list)", env));  // 5

        // include - 包含判断
        System.out.println("include: " +
                AviatorEvaluator.execute("include(list, 3)", env));  // true

        // seq.get - 获取元素
        System.out.println("get: " +
                AviatorEvaluator.execute("seq.get(list, 0)", env));  // 1

        // seq.map - 映射转换
        Expression mapExpr = AviatorEvaluator.compile("seq.map(list, lambda(x) -> x * 2 end)");
        Object result = mapExpr.execute(env);
        System.out.println("map: " + result);  // [2, 4, 6, 8, 10]

        // seq.filter - 过滤
        Expression filterExpr = AviatorEvaluator.compile(
                "seq.filter(list, lambda(x) -> x > 2 end)");
        System.out.println("filter: " + filterExpr.execute(env));  // [3, 4, 5]

        // seq.reduce - 归约
        Expression reduceExpr = AviatorEvaluator.compile(
                "seq.reduce(list, lambda(sum, x) -> sum + x end, 0)");
        System.out.println("reduce: " + reduceExpr.execute(env));  // 15

        // map.keys - 获取所有键
        System.out.println("keys: " +
                AviatorEvaluator.execute("seq.map(map, lambda(k,v) -> k end)", env));
    }

    /// 4.4 日期时间函数
    @Test
    public void test10() {
        Map<String, Object> env = new HashMap<>();
        env.put("now", new Date());
        env.put("dateStr", "2024-01-15");

        // date.now - 当前时间戳
        System.out.println("now: " + AviatorEvaluator.execute("date.now()"));

        // date.format - 格式化日期
        System.out.println("format: " +
                AviatorEvaluator.execute("date.format(now, 'yyyy-MM-dd HH:mm:ss')", env));

        // date.parse - 解析日期字符串
        Date parsed = (Date) AviatorEvaluator.execute(
                "date.parse(dateStr, 'yyyy-MM-dd')", env);
        System.out.println("parsed: " + parsed);

        // date.year/month/day - 提取日期部分
        System.out.println("year: " +
                AviatorEvaluator.execute("date.year(now)", env));
        System.out.println("month: " +
                AviatorEvaluator.execute("date.month(now)", env));
        System.out.println("day: " +
                AviatorEvaluator.execute("date.day(now)", env));

        // 日期比较
        env.put("date1", new Date());
        env.put("date2", new Date(System.currentTimeMillis() - 86400000));  // 昨天
        System.out.println("date1 > date2: " +
                AviatorEvaluator.execute("date1 > date2", env));  // true
    }

    /// 使用自定义函数
    @Test
    public void test11() {
        // 注册自定义函数
        AviatorEvaluator.addFunction(new AddFunction());
        AviatorEvaluator.addFunction(new MaxFunction());

        // 使用自定义函数
        Map<String, Object> env = new HashMap<>();
        env.put("a", 10);
        env.put("b", 20);

        System.out.println("add(a, b) = " +
                AviatorEvaluator.execute("add(a, b)", env));  // 30

        System.out.println("myMax(1, 5, 3, 9, 2) = " +
                AviatorEvaluator.execute("myMax(1, 5, 3, 9, 2)"));  // 9
    }

    /// 5.2 复杂自定义函数
    @Test
    public void test12() {

        AviatorEvaluator.addFunction(new StringUtilsFunction());

        Map<String, Object> env = new HashMap<>();
        env.put("text", "hello");

        System.out.println("reverse: " +
                AviatorEvaluator.execute("strUtil('reverse', text)", env));  // olleh

        System.out.println("capitalize: " +
                AviatorEvaluator.execute("strUtil('capitalize', text)", env));  // Hello

        System.out.println("repeat: " +
                AviatorEvaluator.execute("strUtil('repeat', text, 3)", env));  // hellohellohello
    }

    /// 6.1 Lambda 基础
    @Test
    public void test13() {
        Map<String, Object> env = new HashMap<>();

        // 简单 lambda
        Expression expr1 = AviatorEvaluator.compile("lambda(x) -> x * 2 end");
        env.put("num", 5);
        System.out.println(expr1.execute(env));  // 10

        // 多参数 lambda
        Expression expr2 = AviatorEvaluator.compile("lambda(x, y) -> x + y end");
        System.out.println(expr2.execute(env));

        // lambda 作为参数
        env.put("list", Arrays.asList(1, 2, 3, 4, 5));
        Expression expr3 = AviatorEvaluator.compile(
                "seq.map(list, lambda(x) -> x * x end)");
        System.out.println(expr3.execute(env));  // [1, 4, 9, 16, 25]

        // filter 过滤偶数
//        Expression expr4 = AviatorEvaluator.compile(
//                "seq.filter(list, lambda(x) -> x % 2 == 0 end)");
//        System.out.println(expr4.execute(env));  // [2, 4]

        // reduce 累加
//        Expression expr5 = AviatorEvaluator.compile(
//                "seq.reduce(list, lambda(sum, x) -> sum + x end, 0)");
//        System.out.println(expr5.execute(env));  // 15
    }

    /// 6.2 复杂 Lambda 应用
    @Test
    public void test14() {
        Map<String, Object> env = new HashMap<>();

        // 对象列表处理
        List<Map<String, Object>> users = Arrays.asList(
                new HashMap<String, Object>() {{
                    put("name", "张三");
                    put("age", 25);
                    put("salary", 8000);
                }},
                new HashMap<String, Object>() {{
                    put("name", "李四");
                    put("age", 30);
                    put("salary", 12000);
                }},
                new HashMap<String, Object>() {{
                    put("name", "王五");
                    put("age", 28);
                    put("salary", 10000);
                }}
        );

        env.put("users", users);

        // 筛选年龄大于 25 的用户
        Expression filterExpr = AviatorEvaluator.compile(
                "seq.filter(users, lambda(u) -> u.age > 25 end)");
        System.out.println("年龄>25: " + filterExpr.execute(env));

        // 提取所有用户名
        Expression mapExpr = AviatorEvaluator.compile(
                "seq.map(users, lambda(u) -> u.name end)");
        System.out.println("所有姓名: " + mapExpr.execute(env));  // [张三, 李四, 王五]

        // 计算总工资
        Expression reduceExpr = AviatorEvaluator.compile(
                "seq.reduce(users, lambda(sum, u) -> sum + u.salary end, 0)");
        System.out.println("总工资: " + reduceExpr.execute(env));  // 30000

        // 排序（使用 sort 函数）
        Expression sortExpr = AviatorEvaluator.compile(
                "seq.sort(users, lambda(u1, u2) -> u1.age - u2.age end)");
        System.out.println("按年龄排序: " + sortExpr.execute(env));

        // 链式操作
        Expression chainExpr = AviatorEvaluator.compile(
                "seq.reduce(" +
                        "  seq.map(" +
                        "    seq.filter(users, lambda(u) -> u.age > 25 end)," +
                        "    lambda(u) -> u.salary end" +
                        "  )," +
                        "  lambda(sum, s) -> sum + s end," +
                        "  0" +
                        ")");
        System.out.println("年龄>25的总工资: " + chainExpr.execute(env));  // 22000
    }
}

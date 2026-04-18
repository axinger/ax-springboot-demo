package com.github.axinger;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.exception.QLException;
import com.alibaba.qlexpress4.exception.QLSyntaxException;
import com.alibaba.qlexpress4.runtime.Parameters;
import com.alibaba.qlexpress4.runtime.QContext;
import com.alibaba.qlexpress4.runtime.function.CustomFunction;
import com.alibaba.qlexpress4.security.QLSecurityStrategy;
import com.github.axinger.model.ProductDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/*

https://github.com/alibaba/QLExpress
 */
@Slf4j
public class 表达式_alibaba_qlexpress4Tests {

    /// 第一个 QLExpress 程序
    @SneakyThrows
    @Test
    public void test1() {
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        Object result = express4Runner.execute("a + b * c", context, QLOptions.DEFAULT_OPTIONS).getResult();
        assertEquals(7, result);
    }

    @SneakyThrows
    @Test
    public void test101() {
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        context.put("d", "111");
        context.put("e", "222");

        // 方式1: 返回数组
        Object result = express4Runner.execute("""
                return [a, b];
                """, context, QLOptions.DEFAULT_OPTIONS).getResult();
        System.out.println("返回数组 = " + result); // 输出: [1, 2]
        System.out.println("返回数组类型 = " + result.getClass()); // class java.util.ArrayList


        // 返回Map，可以带字段名
        Object result2 = express4Runner.execute("""
                return {'value1': a, 'value2': b};
                """, context, QLOptions.DEFAULT_OPTIONS).getResult();
        System.out.println("返回map = " + result2); // 输出: {value1=1, value2=2}
        System.out.println("返回map类型 = " + result2.getClass()); // 返回map类型 = class java.util.LinkedHashMap


//        InitOptions options = new InitOptions();
//        options.addAllowClass("com.github.axinger.model.ProductDTO"); // 允许 new 该类
//        Express4Runner runner = new Express4Runner(options);
//        // 返回Map，可以带字段名
//        Object result3 = runner.execute("""
//            return  new com.github.axinger.model.ProductDTO(d,a);
//            """, context, QLOptions.DEFAULT_OPTIONS).getResult();
//        System.out.println("返回map = " + result3); 
//        System.out.println("返回map类型 = " + result3.getClass());
    }


    /*
    因此在 QLExpress4 中，全局变量默认不会写入到 context 中。

如果想要兼容 3 的特性，需要将 polluteUserContext 选项设置为 true，参考代码如下
     */
    @Test
    public void test102() {

        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        QLOptions populateOption = QLOptions.builder().polluteUserContext(true).build();
        Map<String, Object> populatedMap = new HashMap<>();
        populatedMap.put("a", 0.1);
        populatedMap.put("b", 0.2);
        express4Runner.execute("c = a+b;d = 12", populatedMap, populateOption);

        System.out.println("populatedMap = " + populatedMap);
//        assertEquals(11, populatedMap.get("b"));
    }

    @SneakyThrows
    @Test
    public void test104() {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setA(0.1);
        productDTO.setB(0.2);
        productDTO.setProductPrice(1);
        productDTO.setNumber(2);
        
        JSONObject context = JSONObject.from(productDTO);
        System.out.println("context = " + context);

        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        QLOptions populateOption = QLOptions.builder()
                .precise(true) /// 精确计算
                .polluteUserContext(true) ///全局变量默认不会写入到 context 中。
                .build();

        express4Runner.execute("""
                 c = a+b;d = 12;
                totalPrice= number*productPrice;
                """, context, populateOption);

        System.out.println("context = " + context);
    }
    
    
    /*
    添加自定义函数与操作符
最简单的方式是通过 Java Lambda 表达式快速定义函数/操作符的逻辑：
     */

    @Test
    public void test2() {

        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
// custom function
        express4Runner.addVarArgsFunction("join",
                params -> Arrays.stream(params).map(Object::toString).collect(Collectors.joining(",")));
        Object resultFunction =
                express4Runner.execute("join(1,2,3)", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS).getResult();
        assertEquals("1,2,3", resultFunction);

// custom operator
        express4Runner.addOperatorBiFunction("join", (left, right) -> left + "," + right);
        Object resultOperator =
                express4Runner.execute("1 join 2 join 3", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS).getResult();
        assertEquals("1,2,3", resultOperator);
    }

    
    /*
    
    如果自定义函数的逻辑比较复杂，或者需要获得脚本的上下文信息，也可以通过继承 CustomFunction 的方式实现。
     */


    public class HelloFunction implements CustomFunction {
        @Override
        public Object call(QContext qContext, Parameters parameters)
                throws Throwable {
            String tenant = (String) qContext.attachment().get("tenant");
            System.out.println("走自定义方法==========");
            return "hello," + tenant;
        }
    }

    @Test
    public void test3() {
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        express4Runner.addFunction("hello", new HelloFunction());
        String resultJack = (String) express4Runner.execute("hello()",
                Collections.emptyMap(),
                // Additional information(tenant for example) can be brought into the custom function from outside via attachments
                QLOptions.builder().attachments(Collections.singletonMap("tenant", "jack")).build()).getResult();
        assertEquals("hello,jack", resultJack);
        String resultLucy =
                (String) express4Runner
                        .execute("hello()",
                                Collections.emptyMap(),
                                QLOptions.builder().attachments(Collections.singletonMap("tenant", "lucy")).build())
                        .getResult();
        assertEquals("hello,lucy", resultLucy);
    }

    
    /*
    QLExpress4还支持通过QLExpress脚本添加自定义函数。需要注意的是，
    在函数外定义的变量（如示例中的defineTime）在函数定义时就已初始化完成，后续调用函数时不会重新计算该变量的值。
     */


    //建议尽可能使用Java方式定义自定义函数，这样可以获得更好的性能和稳定性。
    
    /*
校验语法正确性
在不执行脚本的情况下，单纯校验语法的正确性，
其中包含了操作符的限制校验，调用 check 并且捕获异常，如果捕获到 QLSyntaxException，则说明存在语法错误
 */

    @Test
    public void test4() {
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        try {
            express4Runner.check("a+b;\n(a+b");
        } catch (QLSyntaxException e) {
            assertEquals(2, e.getLineNo());
            assertEquals(4, e.getColNo());
            assertEquals("SYNTAX_ERROR", e.getErrorCode());
            // <EOF> represents the end of script
            assertEquals(
                    "[Error SYNTAX_ERROR: mismatched input '<EOF>' expecting ')']\n" + "[Near: a+b; (a+b<EOF>]\n"
                            + "                ^^^^^\n" + "[Line: 2, Column: 4]",
                    e.getMessage());
        }
    }
    
    
    /*
    高精度计算
QLExpress 内部会用 BigDecimal 表示所有无法用 double 精确表示数字，来尽可能地表示计算精度：

举例：0.1 在 double 中无法精确表示
     */

    @Test
    public void test5() {

        System.out.println("java缺少精度 = " + (0.1 + 0.2));

        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        Object result = express4Runner.execute("0.1", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS).getResult();
        assertTrue(result instanceof BigDecimal);
        System.out.println("自动转换类型" + result.getClass());
        assertNotEquals(0.3, 0.1 + 0.2, 0.0);
        assertTrue((Boolean) express4Runner.execute("0.3==0.1+0.2", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS)
                .getResult());

        Object result1 = express4Runner.execute("0.1+0.2", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS)
                .getResult();
        System.out.println("result1 = " + result1);
    }

    @Test
    public void test51() {
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);

        assertTrue((Boolean) express4Runner.execute("0.3==0.1+0.2", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS)
                .getResult());

        Object result1 = express4Runner.execute("0.1+0.2", Collections.emptyMap(), QLOptions.DEFAULT_OPTIONS)
                .getResult();
        System.out.println("result1 = " + result1);
    }
    
    /*
    除了默认的精度保证外，还提供了 precise 开关，打开后所有的计算都使用BigDecimal，防止外部传入的低精度数字导致的问题：
     */

    @Test
    public void test6() {
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        Map<String, Object> context = new HashMap<>();
        context.put("a", 0.1);
        context.put("b", 0.2);
        assertFalse((Boolean) express4Runner.execute("0.3==a+b", context, QLOptions.DEFAULT_OPTIONS).getResult());
// open precise switch
        assertTrue((Boolean) express4Runner.execute("0.3==a+b", context, QLOptions.builder().precise(true).build())
                .getResult());
    }
    
    /*
    安全策略
QLExpress4 默认采用隔离安全策略，不允许脚本访问 Java 对象的字段和方法，这确保了脚本执行的安全性。
如果需要访问 Java 对象，可以通过不同的安全策略进行配置。
     */


    public class MyDesk {

        private String book1;

        private String book2;

        public String getBook1() {
            return book1;
        }

        public void setBook1(String book1) {
            this.book1 = book1;
        }

        public String getBook2() {
            return book2;
        }

        public void setBook2(String book2) {
            this.book2 = book2;
        }
    }


    Map<String, Object> context = new HashMap<>() {{
        MyDesk desk = new MyDesk();
        desk.setBook1("Thinking in Java");
        desk.setBook2("Effective Java");
        put("desk", desk);
    }};

    private void assertErrorCode(Express4Runner express4Runner, Map<String, Object> context, String s, String fieldNotFound) {
        try {
            Object result = express4Runner.execute(s, context, QLOptions.DEFAULT_OPTIONS).getResult();
            log.info("result = {}", result);
        } catch (QLException e) {
            log.error(fieldNotFound);
        }
    }

    /*
    1. 隔离策略（默认）
默认情况下，QLExpress4 采用隔离策略，不允许访问任何字段和方法：
     */
    @Test
    public void test11() {

        // default isolation strategy, no field or method can be found
        Express4Runner express4RunnerIsolation = new Express4Runner(InitOptions.DEFAULT_OPTIONS);
        assertErrorCode(express4RunnerIsolation, context, "desk.book1", "FIELD_NOT_FOUND");
        assertErrorCode(express4RunnerIsolation, context, "desk.getBook2()", "METHOD_NOT_FOUND");
    }

  

    /*
    2. 黑名单策略
通过黑名单策略，可以禁止访问特定的字段或方法，其他字段和方法可以正常访问：
     */

    @Test
    public void test12() throws NoSuchMethodException {
        // black list security strategy
        Set<Member> memberList = new HashSet<>();
        memberList.add(MyDesk.class.getMethod("getBook2"));
        Express4Runner express4RunnerBlackList = new Express4Runner(
                InitOptions.builder().securityStrategy(QLSecurityStrategy.blackList(memberList)).build());
        assertErrorCode(express4RunnerBlackList, context, "desk.book2", "FIELD_NOT_FOUND");
        Object resultBlack =
                express4RunnerBlackList.execute("desk.book1", context, QLOptions.DEFAULT_OPTIONS).getResult();
        Assert.equals("Thinking in Java", resultBlack);
    }

    /*
    3. 白名单策略
通过白名单策略，只允许访问指定的字段或方法，其他字段和方法都会被禁止：
     */

    @Test
    public void test13() throws NoSuchMethodException {
        Set<Member> memberList = new HashSet<>();
        memberList.add(MyDesk.class.getMethod("getBook2"));
        // white list security strategy
        Express4Runner express4RunnerWhiteList = new Express4Runner(

                InitOptions.builder().securityStrategy(QLSecurityStrategy.whiteList(memberList)).build());
        Object resultWhite =
                express4RunnerWhiteList.execute("desk.getBook2()", context, QLOptions.DEFAULT_OPTIONS).getResult();
        Assert.equals("Effective Java", resultWhite);
        assertErrorCode(express4RunnerWhiteList, context, "desk.getBook1()", "METHOD_NOT_FOUND");
    }

    /*
    4. 开放策略
开放策略允许访问所有字段和方法，类似于 QLExpress3 的行为，但需要注意安全风险：
     */
    @Test
    public void test14() {
        // open security strategy
        Express4Runner express4RunnerOpen =
                new Express4Runner(InitOptions.builder().securityStrategy(QLSecurityStrategy.open()).build());
        Assert.equals("Thinking in Java",
                express4RunnerOpen.execute("desk.book1", context, QLOptions.DEFAULT_OPTIONS).getResult());
        Assert.equals("Effective Java",
                express4RunnerOpen.execute("desk.getBook2()", context, QLOptions.DEFAULT_OPTIONS).getResult());

    }
    /*
    策略建议
建议直接采用默认策略，在脚本中不要直接调用 Java 对象的字段和方法。而是通过自定义函数和操作符的方式（参考 添加自定义函数与操作符），对嵌入式脚本提供系统功能。这样能同时保证脚本的安全性和灵活性，用户体验还更好。

如果确实需要调用 Java 对象的字段和方法，至少应该使用白名单策略，只提供脚本有限的访问权限。

至于黑名单和开放策略，不建议在外部输入脚本的场景使用，除非确保每个脚本都会经过审核。
     */
}

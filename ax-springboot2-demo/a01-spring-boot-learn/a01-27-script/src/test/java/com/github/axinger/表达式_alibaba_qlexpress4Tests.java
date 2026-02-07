package com.github.axinger;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.security.QLSecurityStrategy;
import com.github.axinger.model.ProductDTO;
import com.ql.util.express.DefaultContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Member;
import java.util.*;

/*

https://github.com/alibaba/QLExpress
 */
public class 表达式_alibaba_qlexpress4Tests {
    @Test
    public void test1() {

        // 定义促销规则（支持 if-else、return 等语句）
        String rule = """
                if (orderAmount >= 200) {
                    return orderAmount * 0.8;
                } else if (orderAmount >= 100) {
                    return orderAmount * 0.9;
                } else {
                    return orderAmount;
                }
                """;
        //Express4Runner 单例
        // 创建表达式执行器（V4 版本）
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);

        // 准备上下文参数
        DefaultContext<String, Object> map1 = new DefaultContext<>();
        map1.put("orderAmount", 250.0);

        // 执行规则
        Object result = express4Runner.execute(rule, map1, QLOptions.DEFAULT_OPTIONS).getResult();


        System.out.println("finalPrice = " + result); // 输出：200.0


        /// 但是当脚本首次执行时，因为没有缓存，依旧会比较慢。

        ///可以通过下面的方法在首次执行前就将脚本缓存起来，保证首次执行的速度：
//        express4Runner.parseToDefinitionWithCache("a+b");

        /// 表达式缓存
        /// 通过 cache 选项可以开启表达式缓存，这样相同的表达式就不会重新编译，能够大大提升性能。
        /// 注意该缓存没有限制大小，只适合在表达式为有限数量的情况下使用：
        // open cache switch
        Object result2 = express4Runner.execute("1+2", new HashMap<>(), QLOptions.builder().cache(true).build()).getResult();
        System.out.println("result2 = " + result2);

    }


    /// 通过黑名单策略，可以禁止访问特定的字段或方法，其他字段和方法可以正常访问：
    @SneakyThrows
    @Test
    public void test2() {

        String rule = """
                if (product.orderAmount >= 200) {
                    return product.orderAmount * 0.8;
                } else if (product.orderAmount >= 100) {
                    return product.orderAmount * 0.9;
                } else {
                    return product.orderAmount;
                }
                """;

        // 创建表达式执行器（V4 版本）

        Set<Member> memberList = new HashSet<>();
//        memberList.add(ProductDTO.class.getMethod("getOrderAmount"));
        Express4Runner express4RunnerBlackList = new Express4Runner(InitOptions.builder().securityStrategy(QLSecurityStrategy.blackList(memberList)).build());

        ProductDTO userInfo = new ProductDTO();
        userInfo.setOrderAmount(205);
        Map<String, Object> context = Collections.singletonMap("product", userInfo);

        // 执行规则
        Object result = express4RunnerBlackList.execute(rule, context, QLOptions.DEFAULT_OPTIONS).getResult();

        System.out.println("finalPrice = " + result); // 输出：200.0
    }


    // 创建单例的Express4Runner实例，避免重复创建
    private static final Express4Runner DEFAULT_RUNNER = new Express4Runner(InitOptions.DEFAULT_OPTIONS);

    /**
     * 通用的QLExpress执行方法
     */
    private Object executeRule(String rule, Map<String, Object> context) {
        return DEFAULT_RUNNER.execute(rule, context, QLOptions.DEFAULT_OPTIONS).getResult();
    }

    /**
     * 带安全策略的执行方法
     */
    private Object executeRuleWithSecurity(String rule, Map<String, Object> context, Set<Member> blackList) {
        Express4Runner secureRunner = new Express4Runner(
                InitOptions.builder()
                        .securityStrategy(QLSecurityStrategy.blackList(blackList))
                        .build()
        );
        return secureRunner.execute(rule, context, QLOptions.DEFAULT_OPTIONS).getResult();
    }

    /**
     * 测试更复杂的业务规则
     */
    @Test
    public void testComplexRule() {
        // QLExpress对集合操作的支持有限，使用简化版本
        String rule = """
                // 简化的购物车计算 - QLExpress v4集合支持有限
                
                // 手动计算总价（替代循环）
                item1Total = item1Price * item1Quantity;
                item2Total = item2Price * item2Quantity;
                item3Total = item3Price * item3Quantity;
                
                cartTotal = item1Total + item2Total + item3Total;
                
                // 应用满减优惠
                if (cartTotal >= 1000) {
                    return cartTotal - 100;
                } else if (cartTotal >= 500) {
                    return cartTotal - 50;
                } else {
                    return cartTotal;
                }
                """;

        Map<String, Object> context = Map.of(
                "item1Price", 100.0,
                "item1Quantity", 2,
                "item2Price", 50.0,
                "item2Quantity", 3,
                "item3Price", 200.0,
                "item3Quantity", 1
        );

        Object result = executeRule(rule, context);
        System.out.println("最终支付金额: " + result); // 输出：500.0
    }

}

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
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class 表达式_qlexpress4Tests {
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

        // 创建表达式执行器（V4 版本）
        Express4Runner express4Runner = new Express4Runner(InitOptions.DEFAULT_OPTIONS);

        // 准备上下文参数
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("orderAmount", 250.0);

        // 执行规则
        Object result = express4Runner.execute(rule, context, QLOptions.DEFAULT_OPTIONS).getResult();

        System.out.println("finalPrice = " + result); // 输出：200.0

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
}

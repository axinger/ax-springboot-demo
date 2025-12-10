package com.github.axinger;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

public class 表达式_spring_el {

    @Test
    public void test1() {
        // 促销规则：嵌套三元表达式
        // 注意：使用 #orderAmount 而不是 orderAmount
        String rule = "#orderAmount >= 200 ? #orderAmount * 0.8 : (#orderAmount >= 100 ? #orderAmount * 0.9 : #orderAmount)";

        ExpressionParser parser = new SpelExpressionParser();

        StandardEvaluationContext context = new StandardEvaluationContext();
        // 创建安全的评估上下文（限制功能）
//        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        // 将变量放入上下文
        context.setVariable("orderAmount", 250.0);

        // 执行表达式
        Double result = parser.parseExpression(rule)
                .getValue(context, Double.class);

        System.out.println("finalPrice = " + result); // 输出：200.0
    }

    @Test
    public void test2() {
//        String rule = "orderAmount >= 200 ? orderAmount * 0.8 : (orderAmount >= 100 ? orderAmount * 0.9 : orderAmount)";
        String rule = "#orderAmount >= 200 ? #orderAmount * 0.8 : (#orderAmount >= 100 ? #orderAmount * 0.9 : #orderAmount)";



        // 使用 Map 作为根对象
        Map<String, Object> params = new HashMap<>();
        params.put("orderAmount", 250.0);

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(rule);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(params);  // 设置所有变量

        Double result = expression.getValue(context, Double.class);
        System.out.println("计算结果: " + result); // 200.0

//        ExpressionParser parser = new SpelExpressionParser();
//        Integer value = parser.parseExpression("1>2 ? 1 : 2").getValue(int.class);//1跟2之间的较大者为2。
//        System.out.println(value);
//        Integer value1 = parser.parseExpression("1<2 ? 2 : 1").getValue(int.class);//1跟2之间的较大者为2。
//        System.out.println(value1);
    }
}

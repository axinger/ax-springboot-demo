package com.github.axinger.config;

import com.baomidou.dynamic.datasource.aop.DynamicDatasourceNamedInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyConfig {

    @Bean
    public DynamicDatasourceNamedInterceptor dsAdvice(DsProcessor dsProcessor) {
        DynamicDatasourceNamedInterceptor interceptor = new DynamicDatasourceNamedInterceptor(dsProcessor);
        Map<String, String> patternMap = new HashMap<>();
//        patternMap.put("select*", "db_2");
//        patternMap.put("add*", "db_2");
//        patternMap.put("update*", "db_2");
//        patternMap.put("delete*", "db_2");
//        interceptor.addPatternMap(patternMap);
        return interceptor;
    }

    @Bean("ds1AdviceAdvisor")
    public Advisor ds1AdviceAdvisor(DynamicDatasourceNamedInterceptor dsAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution (* com.github.axinger.db1.service.*.*(..))");
        return new DefaultPointcutAdvisor(pointcut, dsAdvice);
    }

    @Bean("ds2AdviceAdvisor")
    public Advisor ds2AdviceAdvisor(DynamicDatasourceNamedInterceptor dsAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution (* com.github.axinger.db2.service.*.*(..))");
        return new DefaultPointcutAdvisor(pointcut, dsAdvice);
    }

}

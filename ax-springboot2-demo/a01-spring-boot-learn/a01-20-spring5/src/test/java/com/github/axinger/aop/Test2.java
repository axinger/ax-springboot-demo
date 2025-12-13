package com.github.axinger.aop;

import com.github.axinger.aop.config.AopConfig;
import com.github.axinger.aop.model.AopUser;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Test2 {

    @Test
    public void test1() {


        ApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
        AopUser user = context.getBean("aopUser", AopUser.class);
        System.out.println("user = " + user);
        user.add();
    }

}

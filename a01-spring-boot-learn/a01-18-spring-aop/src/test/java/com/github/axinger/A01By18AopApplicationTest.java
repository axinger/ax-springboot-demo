package com.github.axinger;

import com.github.axinger.controller.TestController;
import com.github.axinger.service.LogService1;
import com.github.axinger.service.UserService;
import com.github.axinger.service2.LogService2;
import com.github.axinger.service2.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class A01By18AopApplicationTest {

    @Autowired
    UserService service;
    @Autowired
    PersonService personService;
    @Autowired
    TestController testController;
    @Autowired
    private LogService1 logService1;
    @Autowired
    private LogService2 logService2;

    @Test
    void test1() {
        String str = service.add("1", "2");
        System.out.println("str = " + str);
    }

    // PersonServiceImpl personService;
    @Test
    void test2() {
        String str = personService.add("1", "2");
        System.out.println("str = " + str);

        // String st2 = personService.add2("1", "2");
        // System.out.println("st2 = " + st2);

        // String add3 = personService.add3("1", "2");
        // System.out.println("add3 = " + add3);
    }

    @Test
    void test_TestController() {
        testController.index();
    }

    @Test
    void test_TestController1() {

        logService1.log("tom");
    }

    @Test
    void test3() {
        logService2.log("jim");
    }


    @Test
    void test_transaction() {

        // isAopProxy(Object obj) ： 判断对象是否是 AOP 代理对象；

        boolean isAopProxy = org.springframework.aop.support.AopUtils.isAopProxy(logService2);
        System.out.println("Is AOP proxy: " + isAopProxy);

        //判断是否是 JDK 动态代理（基于接口）；
        AopUtils.isJdkDynamicProxy(logService2);

        //判断是否是 CGLIB 代理（基于子类）
        AopUtils.isCglibProxy(logService2);
        // 替代方案2：检查代理标记接口
        boolean isProxy = logService2 instanceof org.springframework.aop.SpringProxy;
        System.out.println("Implements SpringProxy: " + isProxy);

        //获取真实目标类：getTargetClass
        Class<?> targetClass = AopUtils.getTargetClass(logService2);

        //想判断一个方法是不是被 AOP 增强了（有没有对应的切面通知）
//        AopUtils.isAspectJAdviceMethod(logService2.getClass().getMethods()[0]);
//
//        // 切入点：匹配UserService的所有方法@Pointcut("execution(* com.example.service.UserService.*(..))")public void userServicePointcut {}
//// 前置通知@Before("userServicePointcut")public void beforeLog(JoinPoint joinPoint) {
//        Method method = ((MethodSignature) joinPoint.getSignature).getMethod;// 判断当前方法是不是切面通

        //findAnnotation 会 沿着继承链和接口链递归查找 ，这在 “子类继承父类方法” 的场景中特别有用。
//        AopUtils.findAnnotation(logService2.getClass(), LogListener.class);
    }
}

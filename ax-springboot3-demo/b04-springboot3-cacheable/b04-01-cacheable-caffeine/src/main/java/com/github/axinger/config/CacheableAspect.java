package com.github.axinger.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1)
@Slf4j
public class CacheableAspect {

    // @Pointcut("@annotation(org.springframework.cache.annotation.Cacheable)")
    // public void cacheablePointCut() {
    // }


    @SneakyThrows
    @Around("@annotation(cacheable)")
    public Object around(ProceedingJoinPoint joinPoint, Cacheable cacheable) {
        // String[] value1 = cacheable.value();
        // //
        // //
        // System.out.println("cacheable.value() = " + Arrays.toString(value1));
        //
        // // 获取方法的参数名和参数值
        // MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // List<String> paramNameList = Arrays.asList(methodSignature.getParameterNames());
        // List<Object> paramList = Arrays.asList(joinPoint.getArgs());
        //
        // System.out.println("paramNameList = " + paramNameList);
        // System.out.println("paramList = " + paramList);


        // InvocationHandler invocationHandler = Proxy.getInvocationHandler(cacheable);

        return joinPoint.proceed();
    }
//
//     @SneakyThrows
//     @Before("cacheablePointCut()")
//     public void addCacheableResolver(ProceedingJoinPoint jp) {
//         // Annotation cacheableAnnotation = getCacheableAnnotation(joinPoint);
//         //
//         //
//         // Object handler = Proxy.getInvocationHandler(cacheableAnnotation);
//         // Field f;
//         // try {
//         //     f = handler.getClass().getDeclaredField("memberValues");
//         // } catch (NoSuchFieldException | SecurityException e) {
//         //     throw new IllegalStateException(e);
//         // }
//         // f.setAccessible(true);
//         // Map<String, Object> memberValues;
//         // try {
//         //     memberValues = (Map<String, Object>) f.get(handler);
//         // } catch (IllegalArgumentException | IllegalAccessException e) {
//         //     throw new IllegalStateException(e);
//         // }
//         // log.info("修改操作事件类型由={}",memberValues);
//         // memberValues.put("cacheResolver", "cacheableResolver");
//
//
//         // 1.获取目标对象类型
//         Class<?> targetCls = jp.getTarget().getClass();
// // 2.获取目标方法对象
// // 2.1获取方法签名信息
//         MethodSignature ms = (MethodSignature) jp.getSignature();
// // 2.2获取方法对象
// // 假如底层配置为jdk代理,则method指向接口中的抽象方法对象.
// // 假如底层配置为CGLIB代理,则这个method指向具体目标对象中的方法对象
// // Method method=ms.getMethod();
// // 假如希望无论是jdk代理还是cglib代理,我们让method变量指向的都是目标对象中的方法对象,那如何实现?
//         Method method = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
// // 3.获取方法上的reqiredLog注解对象
//         Cacheable requiredLog = method.getAnnotation(Cacheable.class);
// // 4.获取注解中的operation的值.
//         System.out.println("requiredLog.cacheNames() = " + requiredLog.cacheNames());
//         System.out.println("requiredLog.value() = " + requiredLog.value());
//
//
//     }
//
//     private Annotation getCacheableAnnotation(JoinPoint joinPoint) {
//         MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//         Method method = signature.getMethod();
//
//         return method.getAnnotation(Cacheable.class);
//     }
}

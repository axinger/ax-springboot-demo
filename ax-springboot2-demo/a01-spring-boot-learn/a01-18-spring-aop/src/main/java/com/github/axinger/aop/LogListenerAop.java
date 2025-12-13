package com.github.axinger.aop;

import com.github.axinger.annotation.LogListener;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogListenerAop {

    @Before(value = "@annotation(logListener)")
    public void logBefore(LogListener logListener) {
        log.info("value={}", logListener.value());
    }


    @Around("@annotation(logListener)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogListener logListener) throws Throwable {
        Method method = getMethod(joinPoint);
        if (method != null && method.isAnnotationPresent(LogListener.class)) {
            // 获取注解中的值
            String value = logListener.value();
            // 继续执行目标方法
            Object result = joinPoint.proceed();
            Object[] args = joinPoint.getArgs();
            log.info("method={},value={},name={}, args={}", method.getName(),
                    value,
                    joinPoint.getSignature().getName(),
                    Arrays.toString(args));
            return result;
        }
        return joinPoint.proceed();
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}

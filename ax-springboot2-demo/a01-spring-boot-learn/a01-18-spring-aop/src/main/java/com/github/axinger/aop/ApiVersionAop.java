package com.github.axinger.aop;

import cn.hutool.core.util.ObjectUtil;
import com.github.axinger.annotation.ApiVersion;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ApiVersionAop {

    /// /@Around("@within(自定义注解)")//自定义注解标注在的类上；该类的所有方法（不包含子类方法）执行aop方法
    // 定义切点表达式
    // @Pointcut("@annotation(com.github.axinger.annotation.ApiVersion)")
    // @Pointcut("@within(com.github.axinger.annotation.ApiVersion)")
    // public void service() {
    // }
    @Pointcut("@within(com.github.axinger.annotation.ApiVersion)")
    public void classPointCut() {
    }

    @Before("classPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        System.out.println("Before==========================");
        Object[] args = joinPoint.getArgs();
        System.out.println("args = " + args);
    }

    // @Before("@annotation(com.github.axinger.annotation.ApiVersion)")
    // public void before(ApiVersion apiVersion){
    //     System.out.println("apiVersion = " + apiVersion);
    // }

    @Around(value = "classPointCut() && @within(apiVersion)") // 取参数 要和 @Pointcut 一致
    public Object process1(ProceedingJoinPoint point, ApiVersion apiVersion) throws Throwable {
        System.out.println("Around==process1==============================================");
        System.out.println("apiVersion.value() = " + apiVersion.value());
        // 访问目标方法的参数：
        Object[] args = point.getArgs();
        if (ObjectUtil.isNotEmpty(args)
                && ObjectUtil.equals(args[0].getClass(), String.class)) {
            args[0] = args[0] + "改变后的参数1";
        }
        // if (args != null
        //         && args.length > 0
        //         && args[0].getClass() == String.class) {
        //     args[0] = args[0]+"改变后的参数1";
        // }
        System.out.println("@Around：①执行目标方法之前 参数 : " + Arrays.toString(args));
        // 用改变后的参数执行目标方法
        Object returnValue = point.proceed(args);
        System.out.println("@Around：②执行目标方法之后 参数 : " + Arrays.toString(args));
        System.out.println("@Around：被织入的目标对象为：" + point.getTarget());
        return returnValue;
        // System.out.println("Around==process1:");
        // // 用改变后的参数执行目标方法
        // Object returnValue = point.proceed();
        // return returnValue;
    }


}

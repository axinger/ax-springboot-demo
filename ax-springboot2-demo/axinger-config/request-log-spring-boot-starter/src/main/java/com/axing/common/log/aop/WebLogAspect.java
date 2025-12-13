package com.axing.common.log.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.axing.common.log.model.WebLogPojo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一日志处理切面
 * Created by macro on 2018/4/26.
 */
@Aspect
@Configuration
@Order(1)
@Slf4j
public class WebLogAspect {

//    @Pointcut("execution(public * com.github.axinger.controller.*.*(..))")
//    public void webLog() {
//    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController ) || @within(org.springframework.stereotype.Controller)")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;

        // 记录请求信息
        WebLogPojo webLogPojo = new WebLogPojo();
        Object result = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // if (method.isAnnotationPresent(ApiOperation.class)) {
        //    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        //    webLog.setDescription(apiOperation.value());
        //}
        long endTime = System.currentTimeMillis();
        if (attributes != null) {
            request = attributes.getRequest();
            String urlStr = request.getRequestURL().toString();
            webLogPojo.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
            webLogPojo.setIp(request.getRemoteAddr());
            webLogPojo.setMethod(request.getMethod());
            webLogPojo.setParameter(getParameter(method, joinPoint.getArgs()));
            webLogPojo.setResult(result);
            webLogPojo.setSpendTime((int) (endTime - startTime));
            webLogPojo.setStartTime(startTime);
            webLogPojo.setUri(request.getRequestURI());
            webLogPojo.setUrl(request.getRequestURL().toString());
        }
        log.info("请求参数和请求返回 = {}", webLogPojo);
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            // 将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!ObjectUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.isEmpty()) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}

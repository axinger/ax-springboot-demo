package com.axing.common.request.log;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.extra.servlet.ServletUtil;
import com.axing.common.json.util.JsonUtil;
import com.axing.common.request.config.RepeatableReadRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Configuration
@Order(1)
@Slf4j
public class WebLogAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController ) || @within(org.springframework.stereotype.Controller)")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            TimeInterval timer = new TimeInterval();
            timer.start();
            Object result = joinPoint.proceed();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            WebLogPojo webLogPojo = new WebLogPojo();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
//                webLogPojo.setIp(request.getRemoteAddr());
                webLogPojo.setIp(ServletUtil.getClientIP(request));
                webLogPojo.setDuration(timer.intervalPretty());
                webLogPojo.setUrl(request.getRequestURL().toString());
                webLogPojo.setMethod(request.getMethod());

                webLogPojo.setHeaders(ServletUtil.getHeaderMap(request));
                /// 获得所有请求参数
                webLogPojo.setParameter(ServletUtil.getParamMap(request));
//                webLogPojo.setParameter(JsonUtil.toJson(ServletUtil.getParams(request)));
                ///调用该方法后，getParam方法将失效

                if (request instanceof RepeatableReadRequestWrapper) {
                    String body = ((RepeatableReadRequestWrapper) request).getBodyAsString();
                    webLogPojo.setBody(body);
                }
//                webLogPojo.setBody(ServletUtil.getBody(request));
                webLogPojo.setResult(result);
            }
            log.info("请求日志={}", JsonUtil.toJson(webLogPojo));
        } catch (Exception e) {
            log.error("请求日志异常: {}", e.getMessage());
        }
        return joinPoint.proceed();
    }
}

package com.axing.common.request.log;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.axing.common.json.util.JsonUtil;
import com.axing.common.request.config.RepeatableReadRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StopWatch;
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


        StopWatch stopWatch = new StopWatch();
        Object result = null;
        Throwable exception = null; // 用于捕获的异常对象

        try {
            stopWatch.start();
            // 1. 执行目标方法
            result = joinPoint.proceed();
            return result;

        } catch (Throwable throwable) {
            // 2. 捕获异常，暂存异常对象，不立即返回
            exception = throwable;
            // 如果需要重新抛出异常，这里先记录，最后再抛
            throw throwable;
        } finally {
            // 3. 停止计时
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            // 4. 统一记录日志 (无论成功还是异常都会执行)
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                // 如果有异常，result 为 null，exception 有值
                // 如果无异常，result 有值，exception 为 null
                logResponse(attributes.getRequest(), result, stopWatch.getTotalTimeMillis(), exception);
            }
        }
    }

    public void logResponse(HttpServletRequest request, Object result, long duration, Throwable exception) {
        try {
            WebLogPojo webLogPojo = new WebLogPojo();
            webLogPojo.setIp(ServletUtil.getClientIP(request));
            webLogPojo.setDuration(DateUtil.formatBetween(duration));
            webLogPojo.setUrl(request.getRequestURL().toString());
            webLogPojo.setMethod(request.getMethod());

            webLogPojo.setHeaders(ServletUtil.getHeaderMap(request));
            /// 获得所有请求参数
            webLogPojo.setParameter(ServletUtil.getParamMap(request));
            ///调用该方法后，getParam方法将失效
            if (request instanceof RepeatableReadRequestWrapper requestWrapper) {
                String body = requestWrapper.getBodyAsString();
                webLogPojo.setBody(body);
            }
//                webLogPojo.setBody(ServletUtil.getBody(request));
            webLogPojo.setResult(result);
            log.info("请求日志={}", JsonUtil.toPrettyPrinterJson(webLogPojo));
        } catch (Exception e) {
            log.error("请求日志异常: {}", e.getMessage());
        }
    }
}

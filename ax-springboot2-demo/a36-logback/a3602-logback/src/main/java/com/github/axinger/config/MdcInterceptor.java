package com.github.axinger.config;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * MDC 拦截器 - 自动设置 traceId 和 userId 到日志上下文
 *
 * @author xing
 */
@Component
public class MdcInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String USER_ID_KEY = "userId";

    /**
     * 请求头名称常量
     */
    public static final String HEADER_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_USER_ID = "X-User-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头获取 traceId，如果没有则生成新的
//        String traceId = request.getHeader(HEADER_TRACE_ID);
        String traceId = IdUtil.fastUUID();
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
        }

        // 从请求头获取 userId
//        String userId = request.getHeader(HEADER_USER_ID);
        String userId = "abcd1234";
        if (userId == null || userId.isEmpty()) {
            userId = "anonymous";
        }

        // 设置到 MDC
        MDC.put(TRACE_ID_KEY, traceId);
        MDC.put(USER_ID_KEY, userId);

        // 将 traceId 添加到响应头，便于链路追踪
        response.setHeader(HEADER_TRACE_ID, traceId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理 MDC，防止内存泄漏
        MDC.clear();
    }

    /**
     * 生成唯一的 traceId
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}

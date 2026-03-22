package com.github.axinger.config;

import cn.hutool.core.util.IdUtil;
import com.github.axinger.util.UserContext;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String userId = request.getHeader("X-User-Id");
        if (userId != null) {
            UserContext.setUserId(userId);
        }

        String traceId = IdUtil.fastSimpleUUID();
        MDC.put("traceId", traceId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        // 请求结束后清理，防止内存泄漏（虽然 TTL 内部有机制，但显式清理是好习惯）
        UserContext.remove();
        MDC.clear();
    }
}

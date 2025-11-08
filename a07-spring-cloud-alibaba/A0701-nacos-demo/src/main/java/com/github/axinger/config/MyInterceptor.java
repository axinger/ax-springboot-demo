package com.github.axinger.config;

import com.axing.common.response.dto.Result;
import com.axing.common.util.utils.ResponseUtil;
import com.github.axinger.bean.FilterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyInterceptor implements HandlerInterceptor {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final FilterProperties filterProperties;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        log.info("请求: contextPath={},requestURI={},method={}", contextPath, requestURI, method);

        // 检查是否应该排除该路径
        List<String> excludePatterns = filterProperties.getWhitelist();
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            boolean anyMatch = excludePatterns.stream()
                    .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
            if (anyMatch) {
                log.info("白名单,不拦截={}", requestURI);
                return true;
            }
        }

        // 检查是否匹配需要拦截的路径
        List<String> blacklist = filterProperties.getBlacklist();
        if (!CollectionUtils.isEmpty(blacklist)) {
            boolean anyMatch = blacklist.stream()
                    .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
            if (anyMatch) {
                log.info("黑名单,拦截={}", requestURI);
                ResponseUtil.writeFail(response, Result.fail("被拦截了"));
                return false;
//                throw new RuntimeException(JSON.toJSONString(Result.fail("被拦截了")));
            }
        }

        log.info("路径 {} 不匹配任何模式，不拦截", requestURI);
        return true;
    }
}

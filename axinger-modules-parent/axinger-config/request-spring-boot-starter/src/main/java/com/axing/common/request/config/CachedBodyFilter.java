package com.axing.common.request.config;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 一旦在 Controller 中使用了 @RequestBody，或者在 Filter/Interceptor 中读取过，就无法再次读取
 * 包装 HttpServletRequest，缓存 Body,防止回流
 */
@Component
public class CachedBodyFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String contentType = request.getContentType();
        String method = request.getMethod();

        // 仅对 POST/PUT/PATCH 且 Content-Type 为 application/json 的请求缓存 body
        if (shouldCacheBody(method, contentType)) {
            RepeatableReadRequestWrapper wrappedRequest = new RepeatableReadRequestWrapper(request);
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean shouldCacheBody(String method, String contentType) {
        if (contentType == null){
            return false;
        }
        return ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method))
                && contentType.contains("application/json");
    }
}

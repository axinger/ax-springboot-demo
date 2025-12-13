package com.github.axinger.filter;

import com.github.axinger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
OncePerRequestFilter 优势：

确保每个请求只执行一次

避免在forward等情况下重复执行

提供了 shouldNotFilter 方法用于排除特定URL
 */
@Slf4j
@Component
@RequiredArgsConstructor
@javax.servlet.annotation.WebFilter(urlPatterns = "/**")
public class OrderFilterOnce extends OncePerRequestFilter {


    @Autowired
    private UserService userService;

//    private final UserService userService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("OrderFilterOnce,拦截器==" + userService);


        // 只会在这个请求第一次到达时执行这段逻辑
        String userId = "test-user-id";
        // 设置 userId 或者其他处理逻辑

        System.out.println("userId = " + userId);


        filterChain.doFilter(request, response);
    }

    @Override
    protected String getAlreadyFilteredAttributeName() {
        return super.getAlreadyFilteredAttributeName(); // 可以重写返回自定义的属性名
    }
}

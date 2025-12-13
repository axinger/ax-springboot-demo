package com.github.axinger.filter;

import com.github.axinger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Component
@javax.servlet.annotation.WebFilter(urlPatterns = "/order/*") // 是/order/*,不是/order/**
@RequiredArgsConstructor
public class OrderFilter implements javax.servlet.Filter {


    //    @Autowired
//    private UserService userService;
    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        log.info("/order/**,javax.servlet.Filter拦截器=={}", userService);
        //前置：强制转换为http协议的请求对象、响应对象 （转换原因：要使用子类中特有方法）
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 如果token有效，继续请求
        chain.doFilter(request, response);
    }
}

package com.github.axinger.filter;

import com.github.axinger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * @WebFilter和@Component一起使用导致urlPatterns不起作用
 * 只使用一个, 但是@Order 又不生效
 */
@Slf4j
@Component
//@javax.servlet.annotation.WebFilter(urlPatterns = "/test/**",
//        filterName = "testFilter",
//        dispatcherTypes = {DispatcherType.REQUEST})
//@Order(-1)
@RequiredArgsConstructor
public class TestFilter implements javax.servlet.Filter {

//    @Autowired
//    private UserService userService;

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //前置：强制转换为http协议的请求对象、响应对象 （转换原因：要使用子类中特有方法）
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("/test/**,javax.servlet.Filter拦截器=={}", userService);
        //6.放行
        chain.doFilter(request, response);
    }
}

//package com.github.axinger.filter;
//
//import com.github.axinger.config.MyHolder;
//import com.github.axinger.config.MyTrackId;
//import com.github.axinger.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.security.Principal;
//
//@Slf4j
//@Component
//@javax.servlet.annotation.WebFilter(urlPatterns = "/order/**", dispatcherTypes = {DispatcherType.REQUEST})
//public class LoginFilter implements Filter {
//
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//
//        log.info("/order/**,javax.servlet.Filter拦截器=="+userService);
//        //前置：强制转换为http协议的请求对象、响应对象 （转换原因：要使用子类中特有方法）
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        //1.context-path及获取请求后面的路径,除去host:port的部分
//        String requestURI = request.getRequestURI();
//        System.out.println("requestURI = " + requestURI);
//        //2.获取请求方法
//        String method = request.getMethod();
//        System.out.println("method = " + method);
//        String pathInfo = request.getPathInfo();
//        System.out.println("pathInfo = " + pathInfo);
//        System.out.println("request.getContextPath() = " + request.getContextPath());
//        System.out.println("request.getQueryString() = " + request.getQueryString());
/// /        System.out.println("request.changeSessionId() = " + request.changeSessionId());
//        System.out.println("request.getAuthType() = " + request.getAuthType());
//        try {
//            request.login("admin", "123456");
//            // 登录成功
//            Principal userPrincipal = request.getUserPrincipal();
//            System.out.println("userPrincipal = " + userPrincipal);
//        } catch (ServletException e) {
//            // 登录失败
//            System.out.println("登录失败e = " + e.getMessage());
//        }
//
//        request.logout();
//
//        //3.完整的获取请求url,包含ip
//        String url = request.getRequestURL().toString();
//        System.out.println("url = " + url);
//
//        if ((request.getContextPath() + "/filter/2").equals(requestURI)) {
//            throw new RuntimeException("Filter内部错误");
//        }
//
//
//        // 如果token有效，继续请求
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//        // 资源释放
//        MyHolder.removeUserId();
//        MyTrackId.removeId();
//    }
//
//    // 模拟token验证方法
//    private boolean isValidToken(String token) {
//        return token.equals("valid-token"); // 替换为实际的token验证逻辑
//    }
//
//}

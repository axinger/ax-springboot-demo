package com.github.axinger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(-10000)
@Slf4j
@Component
// 多租户实现，动态切换数据源，使用nacos配置
public class DynamicSwitchDbFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //在DispatcherServlet之前执行
        HttpServletRequest req = (HttpServletRequest) request;
        //业务方决定对应的库
        String orgCode = req.getHeader("org_code");
        log.info(req.getRequestURI());
//        if (ObjUtil.isEmpty(orgCode)) {
//            log.error("header org code is null,can't find db");
//            throw new RemoteException("header org code is null,can't find db");
//        }
//
//        log.info("switch db name:{}", orgCode);
//        //切换数据源，就是datasource下面的key
//        DynamicDataSourceContextHolder.push(orgCode);
        filterChain.doFilter(request, response);
//
//        //通过ThreadLocal传递库信息，所以需要清楚ThreadLocal
//        log.info("clean db name:{}", orgCode);
//        DynamicDataSourceContextHolder.clear();
    }


    @Override
    public void destroy() {

    }
}

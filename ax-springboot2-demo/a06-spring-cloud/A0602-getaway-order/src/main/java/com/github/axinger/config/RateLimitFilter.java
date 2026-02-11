package com.github.axinger.config;


import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(-1)
@WebFilter(filterName = "rateLimitFilter")
public class RateLimitFilter implements Filter {

    private RedissonIpRateLimiter rateLimiter;
    private RateLimitProperties rateLimitProperties;

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public void init(FilterConfig filterConfig) {
        // 在 init 阶段从 Spring 容器获取 Bean
        rateLimiter = SpringUtil.getBean(RedissonIpRateLimiter.class);
        rateLimitProperties = SpringUtil.getBean(RateLimitProperties.class);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (rateLimitProperties.isEnabled()) {
            String uri = urlPathHelper.getPathWithinApplication(request);
            String method = request.getMethod();
            String clientIp = ServletUtil.getClientIP(request);

            if (!rateLimiter.allowRequest(method, uri, clientIp)) {
                log.info("触发了限流:IP: {}, Method: {}, URI: {}", clientIp, method, uri);
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");

                Map<String, Object> map = new HashMap<>();
                map.put("code", 429);
                map.put("message", "请求过于频繁，请稍后再试");
                String json = JSONObject.toJSONString(map);
                response.getWriter().write(json);
                return;
            }
        }

        chain.doFilter(request, response);
    }


}

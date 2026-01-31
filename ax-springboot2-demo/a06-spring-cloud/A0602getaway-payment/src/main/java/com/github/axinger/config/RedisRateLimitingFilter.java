package com.github.axinger.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 限流,令牌桶机制
 */
@Component
public class RedisRateLimitingFilter implements Filter {
    //    @Autowired
//    private RedissonClient redissonClient;
    //    private RRateLimiter rateLimiter;
    private RateLimiter rateLimiter;

    //    @Bean
//    public RRateLimiter rateLimiter() {
//        RRateLimiter rateLimiter = redissonClient.getRateLimiter("rateLimiter");
//        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);
//        return rateLimiter;
//    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        RRateLimiter rateLimiter = redissonClient.getRateLimiter("rateLimiter");
////        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);
//        rateLimiter.trySetRate(RateType.OVERALL, 1, 3, RateIntervalUnit.SECONDS);
//        this.rateLimiter = rateLimiter;

        RateLimiter limiter = RateLimiter.create(0.2); // 每秒生成5个令
//        RateLimiter limiter = RateLimiter.create(5.0, 1, TimeUnit.SECONDS); // 每秒5令牌，预热期1秒。‌‌
        this.rateLimiter = limiter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (rateLimiter.tryAcquire(1)) {
            chain.doFilter(request, response);
        } else {
//            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
            //429，但它确实是 RFC 6585（后被纳入 RFC 7231 扩展）中专门为限流场景定义的标准状态码
            //429 Too Many Requests	客户端在给定时间内发送了太多请求	✅ 强烈推荐
            ((HttpServletResponse) response).setStatus(429);
        }
    }

    @Override
    public void destroy() {
    }
}

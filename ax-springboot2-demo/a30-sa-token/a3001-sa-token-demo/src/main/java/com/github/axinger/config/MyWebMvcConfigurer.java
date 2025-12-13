package com.github.axinger.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    // 放行路径
    private static final String[] EXCLUDE_PATH_PATTERNS = {
            // Swagger
            "**/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/swagger-ui.html/**",
            "/doc.html/**",
            "/error",
            "/favicon.ico",
            "sso/auth",
            "/csrf",
            "/swagger-ui/*",
            "/v3/**",
            "/swagger-ui.html/**",
            "/swagger-resources/**",
            "/user/doLogin"
    };

    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    /// 拦截url
    private String[] getMatch() {

        return new String[]{
                "/test/**",
                "/orders/**",
        };
    }

    /// 不拦截url
    private String[] getNotMatch() {
        return new String[]{
                "/auth/**",
                "/test/test2",
        };
    }

    // 注册 Sa-Token 的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {


                    /// 登录校验 -- 拦截所有路由，并排除 notMatch 用于开放登录
                    SaRouter.match(getMatch())
                            .notMatch(getNotMatch())
                            .check(r -> {
                                StpUtil.checkLogin();
                            });

                    // 连缀写法
                    // SaRouter.match("/**").check(r -> System.out.println("开始拦截 = "+r.isHit));


//                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
//                    SaRouter.match("/**", "/user/doLogin", r -> StpUtil.checkLogin());
//
//                    // 角色校验 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
//                    SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
//
//                    // 权限校验 -- 不同模块校验不同权限
//                    // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//                    SaRouter.match("/user/**", r -> StpUtil.checkPermission("user.add"))
//                            .check(r -> System.out.println("user是否命中 = " + r.isHit));
//                    SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//                    SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//                    SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//                    SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//                    SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));


                }))
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }

}

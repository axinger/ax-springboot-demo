package com.github.axinger.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.github.axinger.auth.db.dto.PermissionRulesVO;
import com.github.axinger.auth.db.mapper.UserPermissionsAndRoleMapper;
import com.github.axinger.model.AuthProperties;
import com.github.axinger.service.impl.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    private final AuthProperties authProperties;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private final UserInfoService userInfoService;


    /// 拦截url
    private List<String> getMatch() {
        return authProperties.getMatchPathList();
    }

    /// 不拦截url
    private List<String> getNotMatch() {
        return authProperties.getNotMatchPathList();
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

                    userInfoService.getAuthPermission().forEach((path, list) -> {
                        String[] strArray = Convert.toStrArray(list);
                        //void checkPermissionOr 校验：当前账号是否含有指定权限标识 [指定多个，只要其一验证通过即可]
                        //boolean hasPermissionOr 判断：当前账号是否含有指定权限标识 [指定多个，只要其一验证通过即可]
                        SaRouter.match(path, () -> StpUtil.checkPermissionOr(strArray))
                                .check(r -> log.info("权限是否命中path={},strArray{},isHit={}", path, strArray, r.isHit));
                    });

                    userInfoService.getAuthRoles().forEach((path, list) -> {
                        String[] strArray = Convert.toStrArray(list);
                        SaRouter.match(path, () -> StpUtil.checkRoleOr(strArray))
                                .check(r ->
                                        log.info("角色是否命中path{},strArray={},isHit={}", path, strArray, r.isHit)
                                );
                    });

                }))
                .addPathPatterns("/**");
    }

}

package com.github.axinger.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.github.axinger.auth.db.dto.PermissionRulesVO;
import com.github.axinger.auth.db.mapper.GetPermissionsMapper;
import com.github.axinger.model.UserConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    private final UserConfig userConfig;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    private GetPermissionsMapper getAllPermissionRules;


    /// 拦截url
    private List<String> getMatch() {
        return userConfig.getMatchPathList();
    }

    /// 不拦截url
    private List<String> getNotMatch() {
        return userConfig.getNotMatchPathList();
    }

    // 动态获取鉴权规则 
    public Map<String, String> getAuthRules() {
        Map<String, String> rules = new HashMap<>();

        // 从数据库获取所有权限规则
        List<PermissionRulesVO> list = getAllPermissionRules.getAllPermissionRules();

        for (PermissionRulesVO rule : list) {
            String path = rule.getPath();
            String permission = rule.getCode();
            // 如果路径已存在，则跳过或合并处理
            if (!rules.containsKey(path)) {
                rules.put(path, permission);
            }
        }

        return rules;
    }

    public Map<String, String[]> getAuthRole() {
        Map<String, String[]> rules = new HashMap<>();

        // 从数据库获取所有角色规则
        List<PermissionRulesVO> roleRules = getAllPermissionRules.getAllRoleRules();

        for (PermissionRulesVO rule : roleRules) {
            String path = rule.getPath();
            String role = rule.getCode();

            // 如果路径已存在，则添加到角色数组中
            if (rules.containsKey(path)) {
                String[] existingRoles = rules.get(path);
                String[] newRoles = Arrays.copyOf(existingRoles, existingRoles.length + 1);
                newRoles[existingRoles.length] = role;
                rules.put(path, newRoles);
            } else {
                rules.put(path, new String[]{role});
            }
        }

        return rules;
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

                    {
                        Map<String, String> map = getAuthRules();
                        for (String path : map.keySet()) {
                            SaRouter.match(path, () -> StpUtil.checkPermission(map.get(path)))
                                    .check(r -> System.out.println("权限是否命中 = " + r.isHit));
                        }
                    }

                    {
                        Map<String, String[]> map = getAuthRole();
                        for (String path : map.keySet()) {
                            SaRouter.match(path, () -> StpUtil.checkRoleOr(map.get(path)))
                                    .check(r -> System.out.println("角色是否命中 = " + r.isHit));
                        }
                    }
                }))
                .addPathPatterns("/**")

        ;
    }

}

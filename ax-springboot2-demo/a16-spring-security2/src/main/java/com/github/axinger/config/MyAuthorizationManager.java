package com.github.axinger.config;

import com.github.axinger.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.function.Supplier;

@Component
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private PermissionService permissionService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
        // 获取访问url
        String requestURI = object.getRequest().getRequestURI();
        String method = object.getRequest().getMethod();
        String token = object.getRequest().getHeader("Authorization");
        System.out.println("当前访问的url：" + requestURI);
        System.out.println("当前访问的token：" + token);

        Authentication auth = supplier.get();
        Object username = supplier.get().getPrincipal();
        if (username instanceof UserDetails userDetails) {

            System.out.println("当前用户：" + userDetails.getUsername());
        }

        // 1. 如果是注解方式已通过验证，直接放行
        if (auth.isAuthenticated() && auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN") ||
                        g.getAuthority().equals("user:delete"))) {
            return new AuthorizationDecision(true);
        }

        String requiredPermission = permissionService.getRequiredPermission(requestURI, method);

        // 获取当前用户权限
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        // 判断是否拥有权限
        for (GrantedAuthority r : authorities) {
            if (ObjectUtils.nullSafeEquals(r.getAuthority(), requiredPermission)) {
                return new AuthorizationDecision(true); // 返回有权限
            }
        }

        return new AuthorizationDecision(false); //返回没有权限
    }
}

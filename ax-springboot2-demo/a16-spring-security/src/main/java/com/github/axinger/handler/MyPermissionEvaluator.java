package com.github.axinger.handler;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;

/**
 * 权限评估器
 * 我们需要自定义对hasPermission()方法的处理，
 * 就需要自定义PermissionEvaluator，创建类CustomPermissionEvaluator，实现PermissionEvaluator接口。
 */
@Component
public class MyPermissionEvaluator implements PermissionEvaluator {
    /**
     * 自定义验证方法
     *
     * @param authentication 登录的时候存储的用户信息
     * @param targetUrl      @PreAuthorize("hasPermission('/hello/**','r')") 中hasPermission的第一个参数
     * @param permission     @PreAuthorize("hasPermission('/hello/**','r')") 中hasPermission的第二个参数
     * @return
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        System.out.println("targetDomainObject = " + targetUrl);

        if (!(targetUrl instanceof String)) {
            return false;
        }
        String url = (String) targetUrl;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String httpMethod = request.getMethod();

        // 获取用户的角色
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("authorities = " + authorities);


        // 查询数据库中的权限规则
//        List<UrlPermission> permissions = urlPermissionRepository.findByUrlPatternAndHttpMethod(url, httpMethod);
//
//        // 检查用户是否有足够的权限
//        for (GrantedAuthority authority : authorities) {
//            for (UrlPermission permissionRule : permissions) {
//                if (authority.getAuthority().equals(permissionRule.getRoleName())) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // 这里可以实现针对特定资源的权限检查逻辑
        return false;
    }
}

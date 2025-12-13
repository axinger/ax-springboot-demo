package com.github.axinger.config;

import com.github.axinger.service.PermissionService;
import com.github.axinger.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    //此处声明异常全局处理
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final PermissionService permissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 获取当前请求路径和方法
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        // 排除登录、刷新token等公共接口
        if (isExcludePath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtTokenUtil.resolveToken(request);
        if (token == null) {
            SecurityContextHolder.clearContext();
            throw new InsufficientAuthenticationException("没有token用户禁止访问");
        }

        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new AccessDeniedException("token解析异常" + e.getMessage());
        }

        // 获取当前用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("未认证用户禁止访问");
        }

        // 获取用户权限
        Set<String> permissions = permissionService.getUserPermissions(authentication.getName());

        // 根据请求路径和方法查找需要的权限
        String requiredPermission = permissionService.getRequiredPermission(requestURI, method);

        // 检查权限
        if (requiredPermission != null && !permissions.contains(requiredPermission)) {
            throw new AccessDeniedException("权限不足，无法访问");
        }

        //把当前用户信息放入Security全局缓存中
//        SecurityContextHolder.getContext().setAuthentication(getAuth(claims));
        chain.doFilter(request, response);
    }

    private boolean isExcludePath(String path) {
        return path.startsWith("/auth/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs");
    }
}

//package com.github.axinger.handler;
//
//import org.springframework.security.access.AccessDecisionVoter;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.CollectionUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//public class MyAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {
//
//    public static final List<String> WHITELIST = List.of("/login", "/favicon.ico", "/**/test1","/");
//    private final AntPathMatcher pathMatcher = new AntPathMatcher();
//
//    @Override
//    public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
/// /        System.out.println("object = " + targetUrl);
//
/// /      if (targetUrl instanceof HttpServletRequest) {
/// /
/// /          System.out.println("authentication = " + authentication);
/// /      }
/// /        if (!(targetUrl instanceof String)) {
/// /            return ACCESS_ABSTAIN; // 如果没有特殊规则，返回 Abstain
/// /        }
/// /        String url = (String) targetUrl;
/// /        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
/// /        String httpMethod = request.getMethod();
/// /
/// /        System.out.println("httpMethod = " + httpMethod);
/// /        System.out.println("url = " + url);
//
//
/// /        if (object instanceof FilterInvocation) {
/// /            FilterInvocation filterInvocation = (FilterInvocation) object;
//        HttpServletRequest request = object.getRequest();
//        String requestUrl = request.getRequestURI();
//
//        // 你可以根据请求 URL 做进一步的权限判断
//        System.out.println("Requested URL: " + requestUrl);
//
/// /        if (requestUrl.equals("/login") || requestUrl.equals("/favicon.ico")) {
/// /            return ACCESS_GRANTED;
/// /        }
//
//        boolean whitelisted = isWhitelisted(request.getRequestURI());
//        if (whitelisted) {
//            return ACCESS_GRANTED;
//        }
//
//        Map<String, List<String>> map = new HashMap<>();
//        map.put("/users/1", List.of("ROLE_ADMIN", "add"));
//        map.put("/users/2", List.of("delete"));
//        map.put("/users/3", List.of("select"));
//        List<String> requiredAuthorities = map.get(requestUrl);
//
//        String name = authentication.getName();
//        Object principal = authentication.getPrincipal();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        /// 获取用户的权限集合，并转换为字符串形式
//        Set<String> userAuthorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet());
//
//        // 检查用户是否有所有必需的权限
//        boolean hasAllRequiredAuthorities = userAuthorities.containsAll(requiredAuthorities);
//
//        return hasAllRequiredAuthorities ? ACCESS_GRANTED : ACCESS_DENIED;
//    }
//
//    @Override
//    public boolean supports(ConfigAttribute attribute) {
//        // 这里可以返回 true 或者根据属性做更具体的判断
//        return true;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return FilterInvocation.class.isAssignableFrom(clazz);
//    }
//
//    public boolean isWhitelisted(String path) {
//        if (CollectionUtils.isEmpty(WHITELIST)) {
//            return false;
//        }
//        return WHITELIST.stream()
//                .anyMatch(pattern -> pathMatcher.match(pattern, path));
//    }
//}

package com.github.axinger.handler;

import com.axing.common.util.jwt.JwtHelper;
import com.github.axinger.domain.SysUserEntity;
import com.github.axinger.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    private SysUserMapper sysUserMapper;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = JwtHelper.createToken("", userDetails.getUsername());
        log.info("创建token={}", token);
        return token;
    }

    public String validateToken(String token) {
        return JwtHelper.validateToken(token);
    }

//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    public Authentication getAuthentication(String token) {
//        String username = JwtHelper.getUserName(token);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
//    }

    public Authentication getAuthentication(String token) {
        String username;
        try {
            username = JwtHelper.validateToken(token); // 获取用户名
            log.info("username={}", username);
        } catch (Exception e) {
            throw new BadCredentialsException("用户名不存在");
        }
        if (username == null) {
            throw new BadCredentialsException("用户名不存在");
        }
        SysUserEntity userInfo = sysUserMapper.findUserWithRolesAndPermissionsByUsername(username);
        if (userInfo == null) {
            throw new BadCredentialsException("用户名不存在");
        }

        // 这里还可以加一些其他信息的判断，比如用户账号已停用等判断。
        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
        // 构建返回的用户登录成功的token
        return new UsernamePasswordAuthenticationToken(userInfo, null, authorities);

    }

}

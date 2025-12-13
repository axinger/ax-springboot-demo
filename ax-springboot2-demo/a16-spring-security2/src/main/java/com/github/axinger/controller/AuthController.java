package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import com.github.axinger.config.UserDetailsServiceImpl;
import com.github.axinger.dto.LoginDTO;
import com.github.axinger.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        try {
            String username = loginDTO.getUsername();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword(), null)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());

            String token = jwtTokenUtil.generateToken(username);

            // 生成持久化Token
//            String token = tokenService.allocateToken(username).getKey();

            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            result.put("username", loginDTO.getUsername());

            return Result.success(result);
        } catch (BadCredentialsException e) {
            return Result.fail("用户名或密码错误");
        }
    }

    @GetMapping("/refreshToken")
    public Result<Map<String, String>> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(token, userDetails.getUsername())) {
            String newToken = jwtTokenUtil.generateToken(userDetails.getUsername());
            Map<String, String> result = new HashMap<>();
            result.put("token", newToken);
            return Result.success(result);
        }

        return Result.fail("token刷新失败");
    }
}

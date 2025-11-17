package com.github.axinger.controller;

import cn.dev33.satoken.util.SaResult;
import com.github.axinger.model.dto.AccountUserLoginDTO;
import com.github.axinger.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/account/user")
public class UserController {

    @Autowired
    private UserManager userManager;

    @PostMapping("/doLogin")
    public SaResult doLogin(@RequestBody AccountUserLoginDTO req) {
        userManager.login(req);
        return SaResult.ok("登录成功");
    }
}

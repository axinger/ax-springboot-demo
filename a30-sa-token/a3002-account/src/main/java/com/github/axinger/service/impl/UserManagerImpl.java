package com.github.axinger.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.github.axinger.model.User;
import com.github.axinger.model.dto.AccountUserLoginDTO;
import com.github.axinger.service.UserManager;
import com.github.axinger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserService userService;

    private Map<String, User> map = new HashMap<>() {{

        put("jin", User.builder()
                .id("1")
                .userName("jim")
                .password("0b4e7a0e5fe84ad35fb5f95b9ceeac79")
                .build());
        put("tom", User.builder()
                .id("2")
                .userName("tom")
                .password("0b4e7a0e5fe84ad35fb5f95b9ceeac79")
                .build());
    }};

    @Override
    public String login(AccountUserLoginDTO req) {
        // 生成密码
        // 调用数据库校验是否存在用户
        User user = map.get(req.getUserName());
        if (user == null) {
            throw new RuntimeException("账号或密码错误");
        }

        // 为账号生成Token凭证与Session会话
        StpUtil.login(user.getId());
        // 为该用户的session存储更多信息
        // 这里为了方便直接把user实体存进去了，也包括了密码，自己实现时不建议这样做。
        StpUtil.getSession().set("USER_DATA", user);

        //   SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // return   tokenInfo.tokenValue;
        return null;
    }

}

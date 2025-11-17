package com.github.axinger.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.lang.func.LambdaUtil;
import com.axing.common.response.dto.Result;
import com.github.axinger.model.AuthModel;
import com.github.axinger.service.impl.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor

public class AuthController {

    private final UserInfoService userInfoService;

    // 登录接口
    @PostMapping("/login")
    public Result<?> doLogin(@RequestBody AuthModel.LoginDTO dto) {
        AuthModel.UserInfoDTO userInfo = userInfoService.getUserInfo(dto.getUsername(), dto.getPassword());
        // 第1步，先登录上
        // StpUtil.login(10001);
        // 设置登录账号id为10001，第二个参数指定是否为[记住我]，当此值为false后，关闭浏览器后再次打开需要重新登录

        SaLoginParameter loginModel = new SaLoginParameter();
        /// jwt 的 extraData参数
        Map<String, Object> extraData = new HashMap<>();
        extraData.put(LambdaUtil.getFieldName(AuthModel.OrganizationDTO::getId), userInfo.getOrg().getId());
        extraData.put(LambdaUtil.getFieldName(AuthModel.OrganizationDTO::getName), userInfo.getOrg().getName());
        extraData.put(LambdaUtil.getFieldName(AuthModel.OrganizationDTO::getRegion), userInfo.getOrg().getRegion());
        loginModel.setExtraData(extraData);
        StpUtil.login(userInfo.getUserId(), loginModel);
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return Result.success(tokenInfo);
    }

    // 测试注销  ---- http://localhost:8081/acc/logout
    @GetMapping("logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }
    
    // 查询登录状态  ---- http://localhost:8081/acc/isLogin
    @GetMapping("isLogin")
    public Result<?> isLogin(String token) {
        return Result.success("是否登录：" + StpUtil.isLogin(token));
    }

    // 查询 Token 信息  ---- http://localhost:8081/acc/tokenInfo
    @GetMapping("tokenInfo")
    public Result<?> tokenInfo() {
        return Result.success(StpUtil.getTokenInfo());
    }

}

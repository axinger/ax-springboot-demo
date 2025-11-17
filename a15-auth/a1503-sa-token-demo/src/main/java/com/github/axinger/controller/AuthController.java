package com.github.axinger.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.lang.func.LambdaUtil;
import com.axing.common.response.dto.Result;
import com.github.axinger.auth.db.entity.SysUsersEntity;
import com.github.axinger.model.AuthModel;
import com.github.axinger.service.impl.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/// AuthController (认证相关)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoService userInfoService;

    // 登录接口
    @PostMapping("/login")
    public Result<?> doLogin(@RequestBody AuthModel.LoginDTO dto) {
        SysUsersEntity userInfo = userInfoService.getUserInfo(dto.getUsername(), dto.getPassword());
        // 第1步，先登录上
        // StpUtil.login(10001);
        // 设置登录账号id为10001，第二个参数指定是否为[记住我]，当此值为false后，关闭浏览器后再次打开需要重新登录

        SaLoginParameter loginModel = new SaLoginParameter();
        /// jwt 的 extraData参数
        Map<String, Object> extraData = new HashMap<>();
        extraData.put(LambdaUtil.getFieldName(SysUsersEntity::getId), userInfo.getId());
        extraData.put(LambdaUtil.getFieldName(SysUsersEntity::getUsername), userInfo.getUsername());
        extraData.put(LambdaUtil.getFieldName(SysUsersEntity::getEmail), userInfo.getEmail());
        loginModel.setExtraData(extraData);
        StpUtil.login(userInfo.getId(), loginModel);
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return Result.success(tokenInfo);
    }

    // 测试注销
    @GetMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }

    // 查询登录状态
    @GetMapping("isLogin")
    public Result<?> isLogin() {
        return Result.success("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息
    @GetMapping("tokenInfo")
    public Result<?> tokenInfo() {
        return Result.success(StpUtil.getTokenInfo());
    }


    /**
     * 刷新token - 不需要权限
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        // 刷新token逻辑
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前登录用户信息 - 不需要特殊权限，只需登录
     */
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }

    /**
     * 修改密码 - 不需要特殊权限，只需登录
     */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword() {
        // 业务逻辑实现
        return ResponseEntity.ok().build();
    }
}

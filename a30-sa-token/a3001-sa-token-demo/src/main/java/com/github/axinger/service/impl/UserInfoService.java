package com.github.axinger.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson2.JSON;
import com.github.axinger.model.AuthModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserInfoService implements StpInterface {


    String json = """
            [
            {
             "userId": "1",
                "username": "jim",
                "password": "123456",
                "org": {
                    "id": "1",
                    "name": "行政部",
                    "region": "1楼"
                }
            },
            
            {
              "userId": "2",
                "username": "tom",
                "password": "123456",
                "org": {
                    "id": "1",
                    "name": "行政部",
                    "region": "1楼"
                }
            }
            ]
            """;

    public List<AuthModel.UserInfoDTO> getUserInfo() {
        List<AuthModel.UserInfoDTO> list = JSON.parseArray(json, AuthModel.UserInfoDTO.class);
        log.info("list = {}", list);
        return list;
    }

    public AuthModel.UserInfoDTO getUserInfo(String username, String password) {
        List<AuthModel.UserInfoDTO> list = getUserInfo();
        for (AuthModel.UserInfoDTO userInfoDTO : list) {
            if (username.equals(userInfoDTO.getUsername())) {
                if (!password.equals(userInfoDTO.getPassword())) {
                    throw new RuntimeException("密码错误");
                }
                return userInfoDTO;
            }
        }
        throw new RuntimeException("用户不存在");
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        if (ObjUtil.equal(Convert.toStr(loginId), "1")) {
            list.add("orders:get");
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<>();
        if (ObjUtil.equal(Convert.toStr(loginId), "1")) {
            list.add("LEADER");
        }
        return list;
    }
}

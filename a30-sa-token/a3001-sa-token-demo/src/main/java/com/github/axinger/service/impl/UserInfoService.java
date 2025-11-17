package com.github.axinger.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.model.AuthModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserInfoService {

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
}

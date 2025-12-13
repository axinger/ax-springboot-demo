package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.entity.User;

import java.util.List;

/**
 * @author xing
 * @description 针对表【t_user】的数据库操作Service
 * @createDate 2022-06-22 15:34:34
 */
public interface UserService extends IService<User> {

    List<User> saveAndDel();
}

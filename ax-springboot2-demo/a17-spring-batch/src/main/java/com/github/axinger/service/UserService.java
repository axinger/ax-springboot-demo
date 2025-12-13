package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.domain.User;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author xing
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-11-23 01:21:27
 */
public interface UserService extends IService<User> {
    void selectAll(ResultHandler<User> handler);
}

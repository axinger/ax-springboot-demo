package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.entity.User;
import com.github.axinger.mapper.UserMapper;
import com.github.axinger.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xing
 * @description 针对表【t_user】的数据库操作Service实现
 * @createDate 2022-06-22 15:34:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    /**
     * 内部调用, 2个方法都@Transactional,就不会失效
     *
     * @return
     */
    @Transactional
    @Override
    public List<User> saveAndDel() {
        User user = new User();
        user.setName("tom");
        this.save(user);

        delete();

        return this.list();
    }


    @Transactional
    public void delete() {
        User user = this.lambdaQuery()
                .last("limit 1")
                .one();
        this.removeById(user);

        int i = 1 / 0;
    }
}





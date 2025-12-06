package com.github.axinger.auth.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.auth.db.entity.SysUsersEntity;
import com.github.axinger.auth.db.mapper.SysUsersMapper;
import com.github.axinger.auth.db.service.SysUsersService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【sys_users(用户表)】的数据库操作Service实现
 * @createDate 2025-12-05 17:37:35
 */
@Service
public class SysUsersServiceImpl extends ServiceImpl<SysUsersMapper, SysUsersEntity>
        implements SysUsersService {

}





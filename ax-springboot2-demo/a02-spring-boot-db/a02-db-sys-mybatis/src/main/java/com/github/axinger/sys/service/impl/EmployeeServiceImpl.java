package com.github.axinger.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.sys.domain.EmployeeEntity;
import com.github.axinger.sys.mapper.EmployeeMapper;
import com.github.axinger.sys.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【employee】的数据库操作Service实现
 * @createDate 2022-12-17 20:22:09
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, EmployeeEntity>
        implements EmployeeService {

}





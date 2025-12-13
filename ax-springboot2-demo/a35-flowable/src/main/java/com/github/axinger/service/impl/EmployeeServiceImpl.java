package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.EmployeeEntity;
import com.github.axinger.mapper.EmployeeMapper;
import com.github.axinger.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【employee】的数据库操作Service实现
 * @createDate 2025-05-24 13:29:29
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, EmployeeEntity>
        implements EmployeeService {

}





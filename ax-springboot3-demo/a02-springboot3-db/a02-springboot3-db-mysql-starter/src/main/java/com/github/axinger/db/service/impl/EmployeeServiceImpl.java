package com.github.axinger.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.db.domain.Employee;
import com.github.axinger.db.mapper.EmployeeMapper;
import com.github.axinger.db.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xing
 * @description 针对表【employee】的数据库操作Service实现
 * @createDate 2022-12-17 20:22:09
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @Override
    public List<Employee> listLeftSon(Wrapper wrapper) {
        return this.baseMapper.listLeftSon(wrapper);
    }
}





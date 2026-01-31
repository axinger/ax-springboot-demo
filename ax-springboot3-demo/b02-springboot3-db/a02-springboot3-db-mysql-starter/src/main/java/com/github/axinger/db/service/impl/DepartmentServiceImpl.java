package com.github.axinger.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.db.domain.Department;
import com.github.axinger.db.mapper.DepartmentMapper;
import com.github.axinger.db.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service实现
 * @createDate 2022-12-17 19:55:51
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Override
    public List<Department> getDepartmentByEmployee(Wrapper wrapper) {
        return this.baseMapper.getDepartmentByEmployee(wrapper);
    }

    @Override
    public List<Department> listLeft(Wrapper wrapper) {
        return this.baseMapper.listLeft(wrapper);
    }


    @Override
    public List<Department> listLeftEmployee(Wrapper wrapper) {
        return this.getBaseMapper().listLeftEmployee(wrapper);
    }

    @Override
    public List<Department> listAllSon(Wrapper wrapper) {
        return this.baseMapper.listAllSon(wrapper);
    }
}





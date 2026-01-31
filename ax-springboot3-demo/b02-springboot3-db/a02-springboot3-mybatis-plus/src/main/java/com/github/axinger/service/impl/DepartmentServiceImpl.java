package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.DepartmentEntity;
import com.github.axinger.mapper.DepartmentMapper;
import com.github.axinger.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service实现
 * @createDate 2022-12-17 19:55:51
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity>
        implements DepartmentService {

    @Override
    public List<DepartmentEntity> departByEmployeeList(Wrapper<DepartmentEntity> wrapper) {
        return this.baseMapper.departByEmployeeList(wrapper);
    }

    @Override
    public List<DepartmentEntity> listLeft(Wrapper<DepartmentEntity> wrapper) {
        return this.baseMapper.listLeft(wrapper);
    }

    @Override
    public List<DepartmentEntity> listLeftSon(Wrapper<DepartmentEntity> wrapper) {
        return this.baseMapper.listLeftSon(wrapper);
    }

    @Override
    public List<DepartmentEntity> listWhere(Wrapper<DepartmentEntity> wrapper) {
        return this.baseMapper.listWhere(wrapper);
    }
}





package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.axinger.domain.EmployeeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xing
 * @description 针对表【employee】的数据库操作Mapper
 * @createDate 2022-12-17 20:22:09
 * @Entity com.github.axinger.domain.Employee
 */
public interface EmployeeMapper extends BaseMapper<EmployeeEntity> {

    List<EmployeeEntity> leftDepartment(@Param(Constants.WRAPPER) Wrapper<EmployeeEntity> wrapper);
}





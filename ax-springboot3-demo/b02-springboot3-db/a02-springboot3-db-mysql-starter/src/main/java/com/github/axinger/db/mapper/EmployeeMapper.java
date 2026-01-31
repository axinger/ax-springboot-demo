package com.github.axinger.db.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.axinger.db.domain.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xing
 * @description 针对表【employee】的数据库操作Mapper
 * @createDate 2022-12-17 20:22:09
 * @Entity com.github.axinger.domain.Employee
 */
@DS("db_ax_test")
public interface EmployeeMapper extends BaseMapper<Employee> {

    List<Employee> listLeftSon(@Param(Constants.WRAPPER) Wrapper wrapper);
}





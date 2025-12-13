package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.axinger.domain.DepartmentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Mapper
 * @createDate 2022-12-17 19:55:51
 * @Entity com.github.axinger.domain.Department
 */
public interface DepartmentMapper extends BaseMapper<DepartmentEntity> {


    /**
     * 查询结果错误
     *
     * @param wrapper
     * @return
     */
    @Results({
            @Result(
                    column = "dept_id", property = "empList",
                    many = @Many(select = "com.github.axinger.mapper.EmployeeMapper.selectByDeptId")
            )
    })
    @Select("SELECT * FROM department d LEFT JOIN employee e ON (e.dept_id = d.id) "
            + "${ew.customSqlSegment}")
    List<DepartmentEntity> departByEmployeeList(@Param(Constants.WRAPPER) Wrapper wrapper);


    /**
     * 不简单,但是精确
     *
     * @param wrapper
     * @return
     */
    List<DepartmentEntity> listLeft(@Param(Constants.WRAPPER) Wrapper wrapper);


    List<DepartmentEntity> listLeftSon(@Param(Constants.WRAPPER) Wrapper wrapper);

    List<DepartmentEntity> listWhere(@Param(Constants.WRAPPER) Wrapper wrapper);


}





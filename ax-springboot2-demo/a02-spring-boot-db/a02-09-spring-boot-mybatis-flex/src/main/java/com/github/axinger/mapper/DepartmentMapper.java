package com.github.axinger.mapper;

import com.github.axinger.domain.Department;
import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 映射层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Mapper
@UseDataSource("ds1")
public interface DepartmentMapper extends BaseMapper<Department> {


}

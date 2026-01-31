package com.github.axinger.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.db.domain.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service
 * @createDate 2022-12-17 19:55:51
 */
public interface DepartmentService extends IService<Department> {

    List<Department> getDepartmentByEmployee(@Param(Constants.WRAPPER) Wrapper wrapper);

    List<Department> listLeft(@Param(Constants.WRAPPER) Wrapper wrapper);

    List<Department> listLeftEmployee(@Param(Constants.WRAPPER) Wrapper wrapper);

    List<Department> listAllSon(@Param(Constants.WRAPPER) Wrapper wrapper);


}

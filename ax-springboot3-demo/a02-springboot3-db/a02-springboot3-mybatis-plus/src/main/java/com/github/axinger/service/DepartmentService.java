package com.github.axinger.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.domain.DepartmentEntity;

import java.util.List;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service
 * @createDate 2022-12-17 19:55:51
 */
public interface DepartmentService extends IService<DepartmentEntity> {

    List<DepartmentEntity> departByEmployeeList(Wrapper<DepartmentEntity> wrapper);

    List<DepartmentEntity> listLeft(Wrapper<DepartmentEntity> wrapper);

    /**
     * select 返回结果,再包装一个select
     *
     * @param wrapper
     * @return
     */
    List<DepartmentEntity> listLeftSon(Wrapper<DepartmentEntity> wrapper);

    /**
     * 不支持 lambda 方法引用
     * 使用where 左边会丢数据,不推荐
     *
     * @param wrapper
     * @return
     */
    List<DepartmentEntity> listWhere(Wrapper<DepartmentEntity> wrapper);


}

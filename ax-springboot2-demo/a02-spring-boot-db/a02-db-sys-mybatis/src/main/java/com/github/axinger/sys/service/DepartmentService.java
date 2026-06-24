package com.github.axinger.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.sys.domain.DepartmentEntity;
import com.github.axinger.sys.service.impl.DepartmentChainWrapper;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service
 * @createDate 2022-12-17 19:55:51
 */
public interface DepartmentService extends IService<DepartmentEntity> {


    /**
     * 开启链式查询模式
     */
    DepartmentChainWrapper chainQuery();

    // 原有的自定义查询方法
    List<DepartmentEntity> listLeftSon(Wrapper<DepartmentEntity> wrapper);


    // 新增：接受 Consumer 的方法
    List<DepartmentEntity> listLeftSon(Consumer<LambdaQueryWrapper<DepartmentEntity>> consumer);
}

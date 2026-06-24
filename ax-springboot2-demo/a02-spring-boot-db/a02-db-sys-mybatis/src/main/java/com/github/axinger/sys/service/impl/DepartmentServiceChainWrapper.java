package com.github.axinger.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.github.axinger.sys.domain.DepartmentEntity;
import com.github.axinger.sys.service.DepartmentService;

import java.util.List;

/**
 * 自定义链式查询包装器
 * 继承 AbstractChainWrapper 以获得 eq, like, in 等链式方法
 */
public class DepartmentServiceChainWrapper
        extends AbstractChainWrapper<DepartmentEntity, SFunction<DepartmentEntity, ?>, DepartmentServiceChainWrapper, LambdaQueryWrapper<DepartmentEntity>> {

    private final DepartmentService service;

    public DepartmentServiceChainWrapper(DepartmentService service) {
        super();
        this.wrapperChildren = new LambdaQueryWrapper<>();
        this.service = service;
    }

    /**
     * 自定义的终端方法：执行左连接查询
     * 调用方式：service.chainQuery().eq(...).listLeftSon();
     */
    public List<DepartmentEntity> listLeftSon() {
        // 获取构建好的 Wrapper 并传递给 Service
        return service.listLeftSon(this.getWrapper());
    }
}
package com.github.axinger.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper;
import com.github.axinger.sys.domain.DepartmentEntity;
import com.github.axinger.sys.service.DepartmentService;

import java.util.List;
import java.util.function.Consumer;

/**
 * 自定义链式查询包装器
 * <p>
 * 继承 AbstractChainWrapper，泛型 Children = DepartmentChainWrapper，
 * 使 eq/like/in 等链式方法返回 DepartmentChainWrapper 类型，从而能调用自定义终端方法。
 * <p>
 * 调用方式：
 * <pre>
 *   service.chainQuery().eq(DepartmentEntity::getId, 1).listLeftSon();
 *   service.chainQuery().like(DepartmentEntity::getName, "技术").listLeftSon(w -> w.eq(..., ...));
 * </pre>
 *
 * @author xing
 */
public class DepartmentChainWrapper extends AbstractChainWrapper<DepartmentEntity, SFunction<DepartmentEntity, ?>, DepartmentChainWrapper, LambdaQueryWrapper<DepartmentEntity>> {

    private final DepartmentService service;

    public DepartmentChainWrapper(DepartmentService service) {
        super();
        this.wrapperChildren = new LambdaQueryWrapper<>();
        this.service = service;
    }

    /**
     * 自定义终端方法：执行左连接查询（使用当前链式条件）
     */
    public List<DepartmentEntity> listLeftSon() {
        return service.listLeftSon(getWrapper());
    }

    /**
     * 自定义终端方法：执行左连接查询（额外追加 Consumer 条件）
     */
    public List<DepartmentEntity> listLeftSon(Consumer<LambdaQueryWrapper<DepartmentEntity>> consumer) {
        consumer.accept((LambdaQueryWrapper<DepartmentEntity>) getWrapper());
        return service.listLeftSon(getWrapper());
    }
}

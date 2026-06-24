package com.github.axinger.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.sys.domain.DepartmentEntity;
import com.github.axinger.sys.mapper.DepartmentMapper;
import com.github.axinger.sys.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author xing
 * @description 针对表【department】的数据库操作Service实现
 * @createDate 2022-12-17 19:55:51
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity>
        implements DepartmentService {


    /**
     * 提供链式查询的入口
     */
    @Override
    public DepartmentServiceChainWrapper chainQuery() {
        return new DepartmentServiceChainWrapper(this);
    }

    @Override
    public List<DepartmentEntity> listLeftSon(Wrapper<DepartmentEntity> wrapper) {
        return baseMapper.listLeftSon((LambdaQueryWrapper<DepartmentEntity>) wrapper);
    }

    // DepartmentServiceImpl.java
    @Override
    public List<DepartmentEntity> listLeftSon(Consumer<LambdaQueryWrapper<DepartmentEntity>> consumer) {
        // 1. 创建 Wrapper
        LambdaQueryWrapper<DepartmentEntity> wrapper = new LambdaQueryWrapper<>();

        // 2. 应用外部传入的条件 (例如 .eq, .like)
        consumer.accept(wrapper);

        // 3. 执行查询
        return this.listLeftSon(wrapper);
    }
}





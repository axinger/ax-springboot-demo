package com.github.axinger.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.OpcServerPoint;
import com.github.axinger.mapper.OpcServerPointMapper;
import com.github.axinger.service.OpcServerPointService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author xing
 * description 针对表【opc_server_point(kepwaere服务配置)】的数据库操作Service实现
 * createDate 2022-03-15 14:02:18
 */
@Service
@DS("iot_edge_db")
public class OpcServerPointServiceImpl extends ServiceImpl<OpcServerPointMapper, OpcServerPoint>
        implements OpcServerPointService {


    @Override
    public Page<OpcServerPoint> findPage(Long current,
                                         Long size,
                                         OpcServerPoint queryVo) {

        // 分页
        Page<OpcServerPoint> page = new Page<>(current, size);

        // 设置条件
        LambdaQueryWrapper<OpcServerPoint> wrapper = new LambdaQueryWrapper<>();

        Optional.ofNullable(queryVo).map(OpcServerPoint::getId).ifPresent(val -> wrapper.like(OpcServerPoint::getId, val));

        Optional.ofNullable(queryVo).map(OpcServerPoint::getName).ifPresent(val -> wrapper.like(OpcServerPoint::getName, val));
        return this.page(page, wrapper);

    }
}

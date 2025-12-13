package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.domain.OpcServerPoint;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author xing
 * description 针对表【opc_server_point(kepwaere服务配置)】的数据库操作Service
 * createDate 2022-03-15 14:02:18
 */
public interface OpcServerPointService extends IService<OpcServerPoint> {

    /**
     * 分页查询
     *
     * @param current
     * @param size
     * @param queryVo
     * @return
     */
    Page<OpcServerPoint> findPage(@PathVariable Long current,
                                  @PathVariable Long size,
                                  OpcServerPoint queryVo);

}

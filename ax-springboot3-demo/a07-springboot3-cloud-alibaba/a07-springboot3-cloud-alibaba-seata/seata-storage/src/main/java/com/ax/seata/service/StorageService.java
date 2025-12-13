package com.ax.seata.service;

import com.ax.seata.domain.Storage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author xing
 * @description 针对表【t_storage】的数据库操作Service
 * @createDate 2022-04-03 21:07:29
 */
public interface StorageService extends IService<Storage> {

    int decrease(Long productId, Integer count);
}

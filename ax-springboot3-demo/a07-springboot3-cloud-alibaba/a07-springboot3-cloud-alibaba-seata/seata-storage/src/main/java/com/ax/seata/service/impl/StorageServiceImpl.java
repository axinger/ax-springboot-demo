package com.ax.seata.service.impl;

import com.ax.seata.domain.Storage;
import com.ax.seata.mapper.StorageMapper;
import com.ax.seata.service.StorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_storage】的数据库操作Service实现
 * @createDate 2022-04-03 21:07:29
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage>
        implements StorageService {

    @Override
    public int decrease(Long productId, Integer count) {
        return baseMapper.decrease(productId, count);
    }
}





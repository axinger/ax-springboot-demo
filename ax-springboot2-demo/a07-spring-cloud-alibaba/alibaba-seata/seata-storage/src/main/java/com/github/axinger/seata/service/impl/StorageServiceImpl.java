package com.github.axinger.seata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.seata.domain.Storage;
import com.github.axinger.seata.mapper.StorageMapper;
import com.github.axinger.seata.service.StorageService;
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





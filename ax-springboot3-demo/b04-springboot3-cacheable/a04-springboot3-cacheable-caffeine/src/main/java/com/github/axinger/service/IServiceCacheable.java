package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = {"st", "sc"})
public interface IServiceCacheable<T> extends IService<T> {

    // @Cacheable(key = "#id")
    // @Override
    // default T getById(Serializable id) {
    //     return IService.super.getById(id);
    // }
    //
    // @Override
    // default List<T> list() {
    //     return IService.super.list();
    // }


}

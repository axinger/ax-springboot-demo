package com.github.axinger.service;

import com.github.axinger.domain.School;
import org.springframework.cache.annotation.CacheConfig;

/**
 * @author xing
 * @description 针对表【t_school】的数据库操作Service
 * @createDate 2023-01-06 12:08:40
 */
@CacheConfig(cacheNames = "st")
public interface SchoolService extends IServiceCacheable<School> {


}

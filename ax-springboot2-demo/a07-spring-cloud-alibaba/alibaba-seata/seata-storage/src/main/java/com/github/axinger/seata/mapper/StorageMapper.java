package com.github.axinger.seata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.seata.domain.Storage;
import org.apache.ibatis.annotations.Param;

/**
 * @author xing
 * @description 针对表【t_storage】的数据库操作Mapper
 * @createDate 2022-04-03 21:07:29
 * @Entity com.ax.seata.domain.Storage
 */
public interface StorageMapper extends BaseMapper<Storage> {

    int decrease(@Param("productId") Long productId, @Param("count") Integer count);
}





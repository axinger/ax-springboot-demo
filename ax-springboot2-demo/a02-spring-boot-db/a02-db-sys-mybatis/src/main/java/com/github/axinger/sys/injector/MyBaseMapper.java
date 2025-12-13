package com.github.axinger.sys.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 自定义查询所有
     *
     * @return
     */
    List<T> myFindAll();


    /**
     * 以下定义的 4个 method 其中 3 个是内置的选装件
     */
    int insertBatchSomeColumn(List<T> entityList);
}

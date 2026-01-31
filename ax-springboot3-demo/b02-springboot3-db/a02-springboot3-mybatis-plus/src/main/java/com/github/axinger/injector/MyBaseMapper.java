package com.github.axinger.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 自定义查询所有
     *
     * @return
     */
    List<T> findAll();
}

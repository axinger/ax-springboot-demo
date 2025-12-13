package com.github.axinger.service;

import java.util.Map;

public interface SecondsKillService {

    boolean doSecKill(Integer userId, Integer prodId);


    /// 初始化库存
    void createProductCount(Integer prodId);


    Integer productCount(Integer prodId);


    boolean doSecKillOfRedissonLock(Integer userId, Integer prodId);


    /**
     * 添加秒杀产品库存
     *
     * @param prodId
     * @return
     */
    Integer addSk(Integer prodId);

    Map<String, Object> killExecute();
}

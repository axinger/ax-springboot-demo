package com.github.axinger.seata.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xing
 * @description 针对表【t_order】的数据库操作Service
 * @createDate 2021-12-19 00:46:12
 */
@Component
@FeignClient("demo-seata-storage")
public interface StorageService {

    /**
     * 减少库存
     */
    @GetMapping(value = "/storage/decrease")
    Object decrease(@RequestParam(value = "productId") Long id,
                    @RequestParam(value = "count") Integer count);

}

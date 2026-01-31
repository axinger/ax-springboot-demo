package com.ax.seata.controller;

import com.ax.seata.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName StorageController.java
 * @Description TODO
 * @createTime 2021年12月19日 03:07:00
 */

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/test")
    public Object test() {
        return "test-StorageController";
    }

    /**
     * 减少库存
     */
    @GetMapping(value = "/storage/decrease")
    Object decrease(@RequestParam(value = "productId") Long productId,
                    @RequestParam(value = "count") Integer count) {
        System.out.println("接收到库存请求=============" + productId);
//        int a = 1/0;
        return storageService.decrease(productId, count);
    }

}

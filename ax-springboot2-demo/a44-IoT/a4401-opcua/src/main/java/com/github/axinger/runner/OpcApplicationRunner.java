package com.github.axinger.runner;

import com.github.axinger.domain.OpcServerPoint;
import com.github.axinger.service.OpcConnectService;
import com.github.axinger.service.OpcServerPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author xing
 * @date 2022/4/17 22:04
 */
@Component
@Slf4j
public class OpcApplicationRunner implements ApplicationRunner {


    @Resource
    private OpcServerPointService opcServerPointService;

    @Resource
    private OpcConnectService opcConnectService;

//    @Resource
//    OpcServerConfig opcServerConfig;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        /// 应用启动,创建opc链接
        final List<OpcServerPoint> list = opcServerPointService.list();


//        if (ObjectUtil.isEmpty(list)) {
//            OpcServerPoint opcServerPoint = new OpcServerPoint();
//            opcServerPoint.setId(opcServerConfig.getId());
//            opcServerPoint.setUrl(opcServerConfig.getUrl());
//            opcServerPoint.setUsername(opcServerConfig.getUsername());
//            opcServerPoint.setPassword(opcServerConfig.getPassword());
//            list.add(opcServerPoint);
//        }


        List<CompletableFuture<Boolean>> futureList = list.stream()
                .map(val -> CompletableFuture.supplyAsync(() -> opcConnectService.createConnect(val)))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        allOf.whenComplete((k, v) -> log.info("opc连接池={}", OpcConnectService.POOL));

    }

}

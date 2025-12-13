package com.github.axinger.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.axinger.domain.OpcServerPoint;
import com.github.axinger.service.OpcConnectService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName OpcConnectServiceImpl.java
 * description
 * @createTime 2022年04月17日 21:26:00
 */

@Service
@Slf4j
@DS("iot_edge_db")
public class OpcConnectServiceImpl implements OpcConnectService {

    @Override
    public boolean createConnect(OpcServerPoint point) {

        log.info("创建opc链接 OpcServerPoint={}", point);
        if (Optional.ofNullable(point).map(OpcServerPoint::getId).isEmpty()) {
            log.info("OpcServerPoint id 不能为空={}", point);
            return false;
        }

        if (POOL.containsKey(point.getId())) {
            log.info("opcUaClient 已经创建过 POOL={}", POOL);
            return true;
        }

        // 创建OPC UA客户端
        final OpcUaClient client = getCreateClient(point);
        if (ObjectUtil.isEmpty(client)) {
            return false;
        }
        POOL.put(point.getId(), client);
        log.info("opc连接池 POOL={}", POOL);
        return true;
    }

    /**
     * 创建OPC UA的服务连接对象
     */
    private OpcUaClient getCreateClient(OpcServerPoint opcServerPoint) {
        try {


            final OpcUaClient client = OpcUaClient.create(opcServerPoint.getUrl()
            );

//OpcUaClient.create(opcServerPoint.getUrl(),
//                    endpoints -> endpoints.stream()
//                            .filter(endpointDescription -> endpointDescription.getEndpointUrl().equals(opcServerPoint.getUrl().trim()))
//                            .findFirst(),
//                    configBuilder -> configBuilder.setIdentityProvider(new AnonymousProvider())
//                            .setIdentityProvider(new UsernameProvider(opcServerPoint.getUsername(), opcServerPoint.getPassword()))
//                            .setRequestTimeout(Unsigned.uint(5000)).build());
            final String msg = StrUtil.format("OPC UA客户端创建成功={}", client);
            log.info(msg);
            return client;
        } catch (Exception e) {
            final String msg = StrUtil.format("OPC UA客户端创建错误={}", e.getMessage());
            log.error(msg);
            return null;
        }

    }


    @Override
    public CompletableFuture<List<DataValue>> readOpcValues(OpcUaClient client, List<NodeId> ids) {
        CompletableFuture<List<DataValue>> resultFuture = new CompletableFuture<>();
        if (Optional.ofNullable(client).isEmpty()) {
            String msg = StrUtil.format("iot-opc: opc链接信息还未准备好,OpcUaClient为空");
            log.error(msg);
            resultFuture.complete(new ArrayList<>());
            return resultFuture;
        }

        client.connectAsync().whenComplete((data, err) -> {
            if (Optional.ofNullable(err).isPresent()) {
                log.error("iot-opc: OpcUaClient 链接失败={}", err.getMessage());
                resultFuture.completeExceptionally(err);
                return;
            }
            CompletableFuture<List<DataValue>> future = client.readValuesAsync(0, TimestampsToReturn.Both, ids);
            future.whenComplete((dataValueList, err2) -> {
                if (Optional.ofNullable(err2).isPresent()) {
                    log.error("iot-opc: OpcUaClient 读取数据失败={}", err2.getMessage());
                    resultFuture.completeExceptionally(err2);
                    return;
                }
                resultFuture.complete(dataValueList);
            });
        });
        return resultFuture;
    }
}

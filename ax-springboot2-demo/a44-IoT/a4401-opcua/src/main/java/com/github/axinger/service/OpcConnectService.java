package com.github.axinger.service;

import com.github.axinger.domain.OpcServerPoint;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xing
 * @date 2022/4/17 21:47
 */
public interface OpcConnectService {

    Map<String, OpcUaClient> POOL = new ConcurrentHashMap<>();

    /**
     * 创建opc链接
     *
     * @param opcServerPoint
     */
    boolean createConnect(OpcServerPoint opcServerPoint);


    CompletableFuture<List<DataValue>> readOpcValues(OpcUaClient client, List<NodeId> ids);
}

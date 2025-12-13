package com.github.axinger.service;


import com.github.axinger.domain.BaseNodeInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestReadNodeService.java
 * description
 * @createTime 2022年03月11日 11:12:00
 */

public interface OpcReadNodeService {

    CompletableFuture<Map<String, Object>> readValue(String deviceId, String opcServerPointId, List<BaseNodeInfo> nodeInfoList);
}

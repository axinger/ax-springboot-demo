package com.github.axinger.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.axinger.domain.BaseNodeInfo;
import com.github.axinger.domain.Constant;
import com.github.axinger.domain.MachineStatusEnum;
import com.github.axinger.service.OpcConnectService;
import com.github.axinger.service.OpcReadNodeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author xing
 */
@Service
@Slf4j
@DS("iot_edge_db")
public class OpcReadNodeServiceImpl implements OpcReadNodeService {


    @Resource
    private OpcConnectService opcConnectService;


    @Override
    public CompletableFuture<Map<String, Object>> readValue(String deviceId, String opcServerPointId, List<BaseNodeInfo> nodeInfoList) {

        CompletableFuture<Map<String, Object>> resultFuture = new CompletableFuture<>();
        OpcUaClient client = OpcConnectService.POOL.get(opcServerPointId);
        log.info("从连接池取client={},POOL={}", client, OpcConnectService.POOL);
        if (ObjectUtil.isEmpty(client)) {
            resultFuture.completeExceptionally(new Throwable(StrUtil.format("OpcConnectService.POOL连接池不存在{}OpcUaClient", opcServerPointId)));
            return resultFuture;
        }

        log.info("opc连接池={}", OpcConnectService.POOL);


        if (ObjectUtil.isEmpty(nodeInfoList)) {
            resultFuture.completeExceptionally(new Throwable(StrUtil.format("iot-opc: opc节点读取opc数据 nodeInfoList 节点为空,不需要读取数据")));
            return resultFuture;
        }

        final List<NodeId> nodeIdList = nodeInfoList.stream()
                .map(val -> new NodeId(2, val.getId()))
                .collect(Collectors.toList());


        // ids中的 identifier 和 nodeInfoList的id 顺序是一致的,
        // opc没有绑定关系,只能从下标查找,默认一一对应
        final CompletableFuture<List<DataValue>> future = opcConnectService.readOpcValues(OpcConnectService.POOL.get(opcServerPointId), nodeIdList);


        future.whenComplete((list, err) -> {
            log.info("读取opc数据={}", list);

            if (Optional.ofNullable(err).isPresent()) {
                log.error("iot-opc: 读取设备,异常={}", err.getMessage());
                resultFuture.completeExceptionally(err);
                return;
            }


            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("deviceId", deviceId);
            resultMap.put("createDate", LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));

            List<String> badKeyList = List.of(Constant.MACHINES_ONLINE_KEY,
                    Constant.ALARM_KEY,
                    Constant.RUNNING_KEY,
                    Constant.RUNNING_KEY_1,
                    Constant.RUNNING_KEY_2
            );

            for (int i = 0; i < nodeInfoList.size(); i++) {
                String attribute = nodeInfoList.get(i).getAttribute();
                DataValue value = list.get(i);

                Optional.ofNullable(value).map(DataValue::getStatusCode).ifPresent(status -> {
                    if (status.isBad()) {
                        if (ObjUtil.contains(badKeyList, attribute)) {
                            resultMap.put(attribute, -1);
                        } else {
                            resultMap.put(attribute, null);
                        }
                    } else if (status.isGood()) {
                        Optional.of(value).map(DataValue::getValue).map(Variant::getValue).ifPresent(variant -> {
                            if (variant instanceof Boolean a) {
                                resultMap.put(attribute, a ? 1 : 0);
                            } else {
                                resultMap.put(attribute, variant);
                            }
                        });
                    }
                });
            }

            var ref = new Object() {
                Integer machinesOnline = null;
                Integer alarm = null;
                Integer running = null;
            };


            Optional.ofNullable(resultMap.get(Constant.MACHINES_ONLINE_KEY)).ifPresentOrElse(val -> {
                if (val instanceof Boolean val2) {
                    ref.machinesOnline = val2 ? 1 : 0;
                } else {
                    ref.machinesOnline = Integer.parseInt(String.valueOf(val));
                }
            }, () -> ref.machinesOnline = 1);


            Optional.ofNullable(resultMap.get(Constant.ALARM_KEY)).ifPresentOrElse(val -> {
                if (val instanceof Boolean val2) {
                    ref.alarm = val2 ? 1 : 0;
                } else {
                    ref.alarm = Integer.parseInt(String.valueOf(val));
                }
            }, () -> ref.alarm = 0);


            Optional.ofNullable(resultMap.get(Constant.RUNNING_KEY)).ifPresentOrElse(val -> {

                if (val instanceof Boolean val2) {
                    ref.running = val2 ? 1 : 0;
                } else {
                    ref.running = Integer.parseInt(String.valueOf(val));
                }

            }, () -> Optional.ofNullable(resultMap.get(Constant.RUNNING_KEY_1)).ifPresentOrElse(val -> {
                Object r1 = Optional.ofNullable(resultMap.get(Constant.RUNNING_KEY_1)).orElse(0);
                Object r2 = Optional.ofNullable(resultMap.get(Constant.RUNNING_KEY_2)).orElse(0);

                int r1Int;
                int r2Int;

                if (r1 instanceof Boolean aBoolean) {
                    r1Int = aBoolean ? 1 : 0;
                } else {
                    r1Int = Integer.parseInt(String.valueOf(val));
                }

                if (r2 instanceof Boolean aBoolean) {
                    r2Int = aBoolean ? 1 : 0;
                } else {
                    r2Int = Integer.parseInt(String.valueOf(val));
                }

                ref.running = Math.max(r1Int, r2Int);
            }, () -> ref.running = 1));


            MachineStatusEnum statusEnum = MachineStatusEnum.of(ref.machinesOnline, ref.alarm, ref.running);
            resultMap.put(Constant.STATUS_KEY, statusEnum.getCode());

            //{running2=0, running1=0, machinesOnline=1, deviceId=CP-C016, createDate=2023-06-21 11:04:30}


//            OpcDeviceValueDto opcDeviceValueDto = new OpcDeviceValueDto();
//
//
//            List<OpcNodeValueDto> nodeValueDtoList = new ArrayList<>();
//            opcDeviceValueDto.setValues(nodeValueDtoList);

            // 在线
//            nodeInfoList.stream()
//                    .filter(val -> ObjUtil.equal(val.getAttribute(), Constant.MACHINES_ONLINE_KEY))
//                    .findFirst()
//                    .ifPresentOrElse(baseNodeInfo -> {
//
//                        int indexOf = nodeInfoList.indexOf(baseNodeInfo);
//                        DataValue dataValue = list.get(indexOf);
//
//                        Optional.ofNullable(dataValue).map(DataValue::getStatusCode).ifPresent(statusCode -> {
//
//                            if (statusCode.isBad()) {
//                                // 离线
//                                nodeValueDtoList.add(OpcNodeValueDto.builder()
//                                        .field(Constant.STATUS_KEY)
//                                        .value("-1")
//                                        .build());
//                                log.info("isBad 新增 status为-1数据");
//                            } else if (statusCode.isGood()) {
//
//                                // online 有数据
//                                var ref = new Object() {
//                                    Integer machinesOnline = null;
//                                    Integer alarm = null;
//                                    Integer running = null;
//                                };
//
//                                // 在线
//                                Optional.of(dataValue).map(DataValue::getValue).map(Variant::getValue).ifPresent(variant -> {
//                                    if (variant instanceof Boolean a) {
//                                        ref.machinesOnline = a ? 1 : 0;
//                                    } else {
//                                        ref.machinesOnline = (Integer.parseInt(String.valueOf(variant)) > 0) ? 1 : 0;
//                                    }
//                                });
//
//
//                                // 故障
//                                // 有的设备没有报警节点
//                                nodeInfoList.stream()
//                                        .filter(val -> ObjUtil.equal(val.getAttribute(), Constant.ALARM_KEY))
//                                        .findFirst()
//                                        .ifPresentOrElse(val -> {
//                                            DataValue value = list.get(nodeInfoList.indexOf(val));
//                                            Optional.ofNullable(value).map(DataValue::getStatusCode).ifPresent(status -> {
//                                                if (status.isBad()) {
//                                                    ref.alarm = null;
//                                                } else if (status.isGood()) {
//                                                    Optional.of(value).map(DataValue::getValue).map(Variant::getValue).ifPresent(variant -> {
//                                                        if (variant instanceof Boolean a) {
//                                                            ref.alarm = a ? 1 : 0;
//                                                        } else {
//                                                            ref.alarm = (Integer.parseInt(String.valueOf(variant)) > 0) ? 1 : 0;
//                                                        }
//
//                                                    });
//                                                }
//                                            });
//                                        }, () -> {
//                                            ref.alarm = 0;
//                                        });
//
//
//                                // 运行
//                                // running1 running2 running 3种情况
//                                nodeInfoList.stream()
//                                        .filter(val -> ObjUtil.equal(val.getAttribute(), Constant.RUNNING_KEY))
//                                        .findFirst()
//                                        .ifPresentOrElse(val -> {
//                                            DataValue value = list.get(nodeInfoList.indexOf(val));
//                                            Optional.ofNullable(value).map(DataValue::getStatusCode).ifPresent(status -> {
//                                                if (status.isBad()) {
//                                                    ref.running = null;
//                                                } else if (status.isGood()) {
//                                                    Optional.of(value).map(DataValue::getValue).map(Variant::getValue).ifPresent(variant -> {
//                                                        if (variant instanceof Boolean a) {
//                                                            ref.running = a ? 1 : 0;
//                                                        } else {
//                                                            ref.running = (Integer.parseInt(String.valueOf(variant)) > 0) ? 1 : 0;
//                                                        }
//                                                    });
//                                                }
//                                            });
//                                        }, () -> {
//                                            ref.running = 1;
//                                        });
//
//                                MachineStatusEnum statusEnum = MachineStatusEnum.of(ref.machinesOnline, ref.alarm, ref.running);
//                                log.info("判断设备状态={}", statusEnum);
//                                nodeValueDtoList.add(OpcNodeValueDto.builder()
//                                        .field(Constant.STATUS_KEY)
//                                        .value(statusEnum.getCode())
//                                        .build());
//                                // 只要online=1,就需要采集其他数据, 故障,待机, 不影响其他数据采集
//                                // if (ObjUtil.equal(statusEnum, MachineStatusEnum.RUNNING)) {
//                                if (ObjUtil.equal(ref.machinesOnline, 1)) {
//                                    nodeValueDtoList.addAll(this.dataList(nodeInfoList, list));
//                                }
//                            }
//                        });
//                    }, () -> {
//                        nodeValueDtoList.addAll(this.dataList(nodeInfoList, list));
//                    });
//
//
//
//
//
//            nodeValueDtoList.add((OpcNodeValueDto.builder()
//                    .field("deviceId")
//                    .value(deviceId)
//                    .build()));
//            nodeValueDtoList.add((OpcNodeValueDto.builder()
//                    .field("createDate")
//                    .value(LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"))
//                    .build()));
//            opcDeviceValueDto.setDeviceId(deviceId);
            log.info("读取opc resultMap={}", resultMap);
            resultFuture.complete(resultMap);
        });
        return resultFuture;
    }
}

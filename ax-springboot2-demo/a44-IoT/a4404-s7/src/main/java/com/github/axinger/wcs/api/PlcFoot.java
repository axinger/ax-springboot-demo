package com.github.axinger.wcs.api;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.axinger.model.annotation.PlcField;
import com.github.axinger.model.enums.PlcDataType;
import com.github.axinger.model.plc.PLCBaseReadDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcWriteRequest;
import org.apache.plc4x.java.api.messages.PlcWriteResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Slf4j
/*
 * 一个db,一个read
 */
public class PlcFoot {
    private final int dbNum;
    private final PlcReadRequest.Builder read;
    private Map<String, PlcDataType> nameMap;
    //    private List<String> nameList = new ArrayList<>();
    private PlcConnection plcConnection;

    public PlcFoot(int dbNum,
                   PlcConnection plcConnection,
                   PlcReadRequest.Builder read) {
        this.dbNum = dbNum;
        this.plcConnection = plcConnection;
        this.read = read;
        this.nameMap = new HashMap<>();
    }

    // 格式 "%DB1.DBW150:WORD"
    public String address(String offset, PlcDataType type) {
        return StrUtil.format("%DB{}.{}{}:{}", this.dbNum, type.getCode(), offset, type.getType());
    }

    public String offsetName(String offset) {
        String replace = offset.replace(".", "_");
        return StrUtil.format("filed{}", replace);
    }


    public void write(String offset, Object value) {
        PlcDataType type = nameMap.get(offset);
        write(offset, type, value);
    }

    //
    public boolean write(String offset, PlcDataType type, Object value) {

//        String dbName = dbName(this.dbNum);
        PlcWriteRequest.Builder written = plcConnection.writeRequestBuilder();

        String offsetName = offsetName(offset);
        String address = address(offset, type);

        switch (type) {
            case REAL: {
                written.addTagAddress(offsetName, address, (float) value);
                break;
            }

            case WORD: {
                if (value instanceof Number number) {
                    int newValue = number.intValue();
                    written.addTagAddress(offsetName, address, newValue);
                    break;
                }
            }
            default:
                written.addTagAddress(offsetName, address, value);
        }

        CompletableFuture<? extends PlcWriteResponse> writeFuture = written.build().execute();
        AtomicBoolean result = new AtomicBoolean(false);
        writeFuture.thenAccept(response -> {
            if (response != null) {
                if (response.getResponseCode(offsetName) == PlcResponseCode.OK) {
                    System.out.println("Write successful");
                    result.set(true);
                } else {
                    System.out.println("Write failed");
                }
            } else {
                System.out.println("Write failed");
            }
        }).exceptionally(e -> {
            System.out.println("Write failed");
            log.error("plc写入数据错误={}", e.getMessage());
            return null;
        }).join();

        return result.get();
    }


    public void register(Class<?> aClass) {
        // %DB1.DBW150:WORD"
        for (Field field : aClass.getFields()) {
            if (null != field.getAnnotation(PlcField.class)) {
                PlcField annotation = field.getAnnotation(PlcField.class);
                PlcDataType dataType = annotation.dataType();
                String offset = annotation.offset();
//                addTagAddress(offset, dataType);
                read.addTagAddress(field.getName(), address(offset, dataType));
            }
        }
    }

    public <T> CompletableFuture<PLCBaseReadDTO<T>> readClass(Class<T> aClass) {
        CompletableFuture<PLCBaseReadDTO<T>> resultFuture = new CompletableFuture<>();
        this.read.build().execute().whenComplete((response, throwable) -> {
            if (null != throwable) {
                log.error("读取错误: {}", throwable.getMessage());
                resultFuture.completeExceptionally(throwable);
                return;
            }
            PLCBaseReadDTO<T> baseReadDTO = new PLCBaseReadDTO<>();
            baseReadDTO.setDbNo(this.dbNum);
            T resultObj = ReflectUtil.newInstance(aClass);
            baseReadDTO.setData(resultObj);
            for (Field field : aClass.getFields()) {
                if (null != field.getAnnotation(PlcField.class)) {
                    PlcResponseCode responseCode = response.getResponseCode(field.getName());
                    if (Objects.equals(PlcResponseCode.OK, responseCode)) {
                        ReflectUtil.setFieldValue(resultObj, field.getName(), response.getObject(field.getName()));
                    }

                }
            }
            resultFuture.complete(baseReadDTO);
        });

        return resultFuture;
    }

    public <T> PLCBaseReadDTO<T> readClassSync(Class<T> aClass) {
        return readClass(aClass).join();
    }


    public boolean writeClassSync(Object bean) {
        PlcWriteRequest.Builder written = plcConnection.writeRequestBuilder();
        List<String> nameList = new ArrayList<>();
//        writeBuilder.addTagAddress("tag1", "%DB1.DBW150:WORD", 7);
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (!Objects.isNull(field.getAnnotation(PlcField.class))) {
                field.setAccessible(true);
                // 设置字段为可访问状态，否则无法获取到private或protected字段的值
                String propertyName = field.getName(); // 获取属性名
                try {
                    Object value = field.get(bean); // 获取属性值
                    if (!Objects.isNull(value)) {
                        PlcField annotation = field.getAnnotation(PlcField.class);
                        PlcDataType dataType = annotation.dataType();
                        String offset = annotation.offset();
                        String address = address(offset, dataType);
                        written.addTagAddress(propertyName, address, value);
                        nameList.add(propertyName);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Boolean result = written.build().execute().thenApply(response -> {
            if (null == response) {
                System.out.println("Write failed");
                throw new RuntimeException("plc全部写入数据失败");
            } else {
                boolean b = nameList.stream().allMatch(val -> response.getResponseCode(val) == PlcResponseCode.OK);
                log.info("看出全部写入是否正确={}", b);
                if (!b) {
                    throw new RuntimeException("plc全部写入数据失败");
                } else {
                    return true;
                }
            }
        }).exceptionally(e -> {
            System.out.println("Write failed");
            log.error("plc写入数据错误={}", e.getMessage());
            throw new RuntimeException(StrUtil.format("plc全部写入数据失败={}", e.getMessage()));
        }).join();

        System.out.println("nameList = " + nameList);
        log.info("写入数据={}", result ? "成功" : "失败");
        return result;
    }


}

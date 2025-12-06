//package com.github.axinger;
//
//import org.apache.iotdb.isession.pool.SessionDataSetWrapper;
//import org.apache.iotdb.rpc.IoTDBConnectionException;
//import org.apache.iotdb.rpc.StatementExecutionException;
//import org.apache.iotdb.session.Session;
//import org.apache.iotdb.session.pool.SessionPool;
//import org.apache.tsfile.enums.TSDataType;
//import org.apache.tsfile.write.record.Tablet;
//import org.apache.tsfile.write.schema.IMeasurementSchema;
//import org.apache.tsfile.write.schema.MeasurementSchema;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//class IotDBSessionConfigTest {
//
//    @Autowired
//    private IotDBSessionConfig iotDBSessionConfig;
//    @Autowired
//    private SessionPool sessionPool;
//
//    @Test
//    void test() {
//
//        for (int i = 0; i < 5; i++) {
//            String tableName = "root.cepai.CP109_1";
//            long currentTime = System.currentTimeMillis();
//            List<String> iotMeasurements = new ArrayList<>();
//            iotMeasurements.add("machinesOnline");
//
//            List<String> iotValues = new ArrayList<>();
//            iotValues.add("1");
//            iotDBSessionConfig.insertRecord(tableName, currentTime, iotMeasurements, iotValues);
//        }
//
//    }
//
//    @Test
//    void test2() throws IoTDBConnectionException, StatementExecutionException {
//
////        for (int i = 0; i < 5; i++) {
////            String tableName = "root.cepai.CP109_1";
////            long currentTime = System.currentTimeMillis();
////            List<String> iotMeasurements = new ArrayList<>();
////            iotMeasurements.add("machinesOnline");
////
////            List<String> iotValues = new ArrayList<>();
////            iotValues.add("1");
////            iotDBSessionConfig.insertRecord(tableName, currentTime, iotMeasurements, iotValues);
////        }
//
//        String tableName = "root.cepai.CP109_1";
//
//
//        List<Long> times = new ArrayList<>();
//        List<List<String>> measurementsList = new ArrayList<>();
//        List<List<TSDataType>> typesList = new ArrayList<>();
//        List<List<Object>> valuesList = new ArrayList<>();
//
//
////        for (int i = 0; i < 5; i++) {
//
//
////            long currentTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).getNano();
////            times.add((long) LocalDateTime.now().toInstant(ZoneOffset.UTC).getNano());
//        times.add(System.currentTimeMillis());
//
//        List<String> iotMeasurements = new ArrayList<>();
//        iotMeasurements.add("machinesOnline");
//        iotMeasurements.add("status");
//
//        measurementsList.add(iotMeasurements);
//
//
//        List<TSDataType> types = new ArrayList<>();
//        types.add(TSDataType.TEXT);
//        types.add(TSDataType.TEXT);
//        typesList.add(types);
//
//        List<Object> iotValues = new ArrayList<>();
//        iotValues.add(String.valueOf(1));
//        iotValues.add(String.valueOf(1));
//        valuesList.add(iotValues);
//
//
////        }
//
//
//        iotDBSessionConfig.sessionPool().insertOneDeviceRecords(tableName,
//                times,
//                measurementsList,
//                typesList,
//                valuesList);
//
//
//    }
//
//    @Test
//    void test3() {
//        List<String> iotMeasurements2 = new ArrayList<>();
//        iotMeasurements2.add("machinesOnline");
//        iotMeasurements2.add("status");
//
//
//        List<String> iotValues2 = new ArrayList<>();
//        iotValues2.add(String.valueOf(1));
//        iotValues2.add(String.valueOf(1));
//
//        iotDBSessionConfig.insertRecord("root.cepai.CP109_1"
//                , System.currentTimeMillis(),
//                iotMeasurements2,
//                iotValues2
//
//        );
//    }
//
//    @Test
//    void test_sessionPool_last_value() {
//
//        final List<Map<String, Object>> map = iotDBSessionConfig.executeQuery("select last_value(status) from root.cepai.CP109_1");
//
//        System.out.println("map = " + map);
//    }
//
//    @Test
//    void test_sessionPool() {
//
//
//        SessionDataSetWrapper wrapper = null;
//        try {
//            wrapper = sessionPool.executeQueryStatement("select * from root.cepai.CP109_1");
//
//            final List<String> columnNames = wrapper.getColumnNames();
//            Map<String, Object> map = new HashMap<>();
//            while (wrapper.hasNext()) {
//                for (int i = 0; i < columnNames.size() - 1; i++) {
//                    map.put(columnNames.get(i + 1), wrapper.next().getFields().get((i)));
//                }
//            }
//            System.out.println("map = " + map);
//
//        } catch (IoTDBConnectionException | StatementExecutionException e) {
//
//
//        } finally {
//            sessionPool.closeResultSet(wrapper);
//        }
//
//
//    }
//
//    @Test
//    void test_sessionPool_44() {
//
//
//        SessionDataSetWrapper wrapper = null;
//        try {
//            wrapper = sessionPool.executeQueryStatement("select * from root.cepai.CP109_1");
//
//            final List<String> columnNames = wrapper.getColumnNames();
//
//            List list = new ArrayList();
//            while (wrapper.hasNext()) {
//
//                Map<String, Object> map = new HashMap<>();
//                list.add(map);
//
//                final List<org.apache.tsfile.read.common.Field> fields = wrapper.next().getFields();
//
//                if (columnNames.size() > fields.size()) {
//
//                    System.out.println("不相同");
//                    for (int i = 0; i < columnNames.size() - 1; i++) {
//                        map.put(columnNames.get(i + 1), fields.get((i)));
//                    }
//
//                } else if (columnNames.size() == fields.size()) {
//
//                    System.out.println("相同");
//
//                    for (int i = 0; i < columnNames.size(); i++) {
//                        map.put(columnNames.get(i), fields.get((i)));
//                    }
//                } else {
//                    System.out.println("相同========");
//                }
//
//            }
//            System.out.println("list = " + list.size());
//
//        } catch (IoTDBConnectionException | StatementExecutionException e) {
//
//
//        } finally {
//            sessionPool.closeResultSet(wrapper);
//        }
//
//
//    }
//
//    /**
//     * 使用Session.insertTablet接口插入某一个设备的数据
//     */
//    private void insertTablet(Session session) throws IoTDBConnectionException, StatementExecutionException {
//        /*
//         * 一个Tablet例子:
//         * deviceID: root.ln.wf01.wt01
//         * time status, temperature, speed
//         * 1    true        1.0       1
//         * 2    false       2.0       2
//         * 3    true        3.0       3
//         */
//        // 设置设备名字，设备下面的传感器名字，各个传感器的类型
//        List<IMeasurementSchema> schemaList = new ArrayList<>();
//        schemaList.add(new MeasurementSchema("machinesOnline", TSDataType.BOOLEAN));
//        schemaList.add(new MeasurementSchema("temperature", TSDataType.DOUBLE));
//        schemaList.add(new MeasurementSchema("speed", TSDataType.INT64));
////
////        Tablet tablet = new Tablet("root.ln.wf01.wt01", schemaList, BATCH_INSERT_SIZE);
//
//        Tablet tablet = new Tablet("root.cepai.CP109_1", schemaList);
//
//        int TOTAL_INSERT_ROW_COUNT = 10;
//
//        // 以当前时间戳作为插入的起始时间戳
//        long timestamp = System.currentTimeMillis();
//
//        for (long row = 0; row < TOTAL_INSERT_ROW_COUNT; row++) {
//            int rowIndex = tablet.rowSize++;
//            tablet.addTimestamp(rowIndex, timestamp);
//            // 随机生成数据
//            tablet.addValue("machinesOnline", rowIndex, (row & 1) == 0);
//            tablet.addValue("temperature", rowIndex, (double) row);
//            tablet.addValue("speed", rowIndex, row);
//
//
////            session.insertRecord();
//
//
//            if (tablet.rowSize == tablet.getMaxRowNumber()) {
//                session.insertTablet(tablet);
//                tablet.reset();
//                System.out.println("已经插入了：" + (row + 1) + "行数据");
//            }
//            timestamp++;
//        }
//
//        // 插入剩余不足 BATCH_INSERT_SIZE的数据
//        if (tablet.rowSize != 0) {
//            session.insertTablet(tablet);
//            tablet.reset();
//        }
//    }
//
//}

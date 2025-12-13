// package com.ax.demo;
//
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import java.util.List;
// import java.util.Map;
//
//@SpringBootTest
// class A23DemoApplicationTest {
/// /    @Autowired
/// /    private IotDBSessionConfig iotDBSessionConfig;
/// /
/// /    @Test
/// /    void test() {
/// /
/// /        StringBuffer tableName = new StringBuffer();
/// /        tableName.append("root").append(".").append("CP108").append(".").append("CP108_dev");
/// /
/// /
/// /        long currentTime = System.currentTimeMillis();
/// /
/// /        List<String> iotMeasurements = new ArrayList<>();
/// /        iotMeasurements.add("absoluteCoordinateX");
/// /        iotMeasurements.add("absoluteCoordinateY");
/// /
/// /        List<String> iotValues = new ArrayList<>();
/// /        iotValues.add("5");
/// /        iotValues.add("6");
/// /
/// /        iotDBSessionConfig.insertRecord(tableName.toString(), currentTime, iotMeasurements, iotValues);
/// /
/// /    }
//
//    @Autowired
//    private IotDBConfig iotDBConfig;
//
//
//    @Test
//    void test_1() {
//
//
//        String sql = String.format("insert into root.CP108(timestamp,s1,s2) values(%s,5,6);", System.currentTimeMillis());
//
//        iotDBConfig.insert(sql);
//    }
//
//    @Test
//    void test_2() {
//
/// /        select last_value(absoluteCoordinateX) from root.CP108
/// /        String sql = "SELECT * from root.CP108";
//        String sql = "select * from root.a1.b1;";
//        List<Map<String, Object>> resultList = iotDBConfig.query(sql);
//        System.out.println("resultList = " + resultList);
//
//
//    }
//}

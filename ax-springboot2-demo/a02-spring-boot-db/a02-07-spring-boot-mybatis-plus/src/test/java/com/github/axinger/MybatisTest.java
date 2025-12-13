package com.github.axinger;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.axinger.sys.mapper.CommonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class MybatisTest {


    @Autowired
    private CommonMapper commonMapper;

    @Test
    void test5() {

        String sql = "select a,c,b,d from sys_alphabet";

        //data = [{a=1, c=3, b=2, d=4}, {a=11, c=31, b=21, d=41}]
        List<LinkedHashMap<String, Object>> data = commonMapper.queryTableLinkedHashMap(sql);
        System.out.println("data = " + data);

        // data1 = [{a=null, c=3, b=2, d=4}, {a=null, c=31, b=21, d=41}]
        List<Map<String, Object>> data1 = commonMapper.queryTableLinkedHashMap2(sql);
        System.out.println("data1 = " + data1);

        //data2 = [{a=1, b=2, c=3, d=4}, {a=11, b=21, c=31, d=41}]
        List<TreeMap<String, Object>> data2 = commonMapper.queryTableTreeMap(sql);
        System.out.println("data2 = " + data2);

        //data3 = [{a=1, b=2, c=3, d=4}, {a=11, b=21, c=31, d=41}]
        List<Map<String, Object>> data3 = commonMapper.queryTableMap(sql);
        System.out.println("data3 = " + data3);
    }

    @Test
    void test51() {

        String sql = "select * from sys_alphabet";

        // 不指定,就按照表字段顺序
        // data = [{id=1, b=2, c=3, d=4, a=1}, {id=2, b=21, c=31, d=41, a=11}],
        List<LinkedHashMap<String, Object>> data = commonMapper.queryTableLinkedHashMap(sql);
        System.out.println("data = " + data);

        // data1 = [{id=1, b=2, c=3, d=4, a=1}, {id=2, b=21, c=31, d=41, a=11}]
        List<Map<String, Object>> data1 = commonMapper.queryTableLinkedHashMap2(sql);
        System.out.println("data1 = " + data1);


        // data2 = [{a=1, b=2, c=3, d=4, id=1}, {a=11, b=21, c=31, d=41, id=2}]
        List<TreeMap<String, Object>> data2 = commonMapper.queryTableTreeMap(sql);
        System.out.println("data2 = " + data2);

        // data3 = [{a=1, b=2, c=3, d=4, id=1}, {a=11, b=21, c=31, d=41, id=2}]
        List<Map<String, Object>> data3 = commonMapper.queryTableMap(sql);
        System.out.println("data3 = " + data3);
    }

    @Test
    void test6() {

        //[java.lang.IllegalArgumentException: SQL 不能为空] ,进行校验
//        String sql = "";
        String sql = "delete from sys_alphabet";
        List<Map<String, Object>> data = commonMapper.queryTableMap(sql);
        System.out.println("data = " + data);
    }

    @Test
    void test7() {

        // 没有进行校验
//        String sql = "";
        String sql = "select a,c,b,d from sys_alphabet";
        List<Map<String, Object>> data = commonMapper.queryTableMap2(sql, "id");
        System.out.println("data = " + data);
    }

    @Test
    void test8() {

        //[java.lang.IllegalArgumentException: SQL 不能为空] ,进行校验
//        String sql = "";
        String sql = "select a,c,b,d from sys_alphabet";


        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            List<LinkedHashMap<String, Object>> data = commonMapper.queryTableHasId(sql, map);
            System.out.println("data = " + data);
        } catch (Exception e) {
            System.err.println("e" + ExceptionUtil.getMessage(e));
        }
        try {
            List<LinkedHashMap<String, Object>> data2 = commonMapper.queryTableHasId(sql, null);
            System.out.println("data2 = " + data2);


            /// 这个方式最简单
            data2.forEach(val -> val.replaceAll((key, value) -> {
                if (value instanceof LocalDateTime) {
                    return LocalDateTimeUtil.format((LocalDateTime) value, "yyyy-MM-dd HH:mm:ss");
                } else if (value instanceof Timestamp) { //Timestamp extends java.util.Date
                    return DateUtil.format((Timestamp) value, "yyyy-MM-dd HH:mm:ss");
                } else if (value == null) {
                    return "-";//自定义null返回值
                }
                return value;
            }));
            System.out.println("修改值后data2 = " + data2);
        } catch (Exception e) {
            System.err.println("data2" + ExceptionUtil.getMessage(e));
        }
    }

    @Test
    void test9() {

//        String sql = "INSERT INTO sys_alphabet(a, c) VALUES ('test', '" +
//                LocalDateTime.now() + "')";

        String sql = "INSERT INTO sys_alphabet(a, c) VALUES ('{a}','{c}')";
        Map<String, Object> map = new HashMap<>();
        map.put("a", "test");
        map.put("c", LocalDateTime.now());

        sql = StrUtil.format(sql, map);

        int affected = commonMapper.insertRawSql(sql);
        System.out.println("affected = " + affected);
    }


}

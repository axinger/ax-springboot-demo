package com.github.axinger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TableTests {

    @Test
    void test1() {

        Table<String, String, Double> tables = HashBasedTable.create();
        tables.put("小明", "数学", 100.0);
        tables.put("小明", "语文", 99.0);
        tables.put("小明", "英语", 98.0);


        tables.put("小红", "数学", 89.0);
        tables.put("小红", "语文", 88.0);
        tables.put("小红", "英语", 87.0);

        System.out.println("tables.rowKeySet() = " + tables.rowKeySet());
        System.out.println("tables.columnKeySet() = " + tables.columnKeySet());
        System.out.println("tables.rowMap() = " + tables.rowMap());
        System.out.println("tables.columnMap() = " + tables.columnMap());
    }

    @Test
    void test2() {

        List<String> name = List.of("小明", "小红");
        List<String> sub = List.of("数学", "语文", "英语");

        Table<String, String, Double> tables = HashBasedTable.create();
        // 补齐,矩阵空值
        for (String s : name) {
            for (String s1 : sub) {
                tables.put(s, s1, 0.0); // 默认0
            }
        }
        System.out.println("tables.rowKeySet() = " + tables.rowKeySet());
        System.out.println("tables.columnKeySet() = " + tables.columnKeySet());
        System.out.println("tables.rowMap() = " + tables.rowMap());
        System.out.println("tables.columnMap() = " + tables.columnMap());

        System.out.println("==========================================================");

        tables.put("小明", "数学", 100.0);
        tables.put("小明", "语文", 99.0);

        tables.put("小红", "英语", 87.0);

        System.out.println("tables.rowKeySet() = " + tables.rowKeySet());
        System.out.println("tables.columnKeySet() = " + tables.columnKeySet());
        System.out.println("tables.rowMap() = " + tables.rowMap());
        System.out.println("tables.columnMap() = " + tables.columnMap());


        tables.cellSet().forEach((k1) -> {
            System.out.println("k1.getRowKey() = " + k1.getRowKey());
            System.out.println("k1.getColumnKey() = " + k1.getColumnKey());
            System.out.println("k1.getValue() = " + k1.getValue());
            System.out.println("tables.get( k1.getRowKey(),k1.getColumnKey()) = " + tables.get(k1.getRowKey(), k1.getColumnKey()));
        });

    }
}

package com.github.axinger.model.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DemoConvertProperties 配置绑定测试
 * 验证自定义 Converter（DemoIntervalConverter）对 JSON 字符串的解析行为
 */
@SpringBootTest
public class VirtuallyCallPropertiesTest {

    @Autowired
    private DemoConvertProperties demoConvertProperties;

    @Test
    @DisplayName("基础数值配置绑定：timeOut 与 connectTimeout")
    public void testBasicProperties() {
        assertNotNull(demoConvertProperties);
        assertEquals(5000, demoConvertProperties.getTimeOut());
        assertEquals(5000, demoConvertProperties.getConnectTimeout());
    }

    @Test
    @DisplayName("intervalConfig 通过 DemoIntervalConverter 解析为有序列表")
    public void testIntervalConfig() {
        List<DemoConvertProperties.Item> intervals = demoConvertProperties.getIntervalConfig();
        assertNotNull(intervals);
        assertEquals(6, intervals.size());

        assertEquals(0, intervals.get(0).getLowerBound());
        assertEquals(720, intervals.get(0).getUpperBound());
        assertEquals("0", intervals.get(0).getValue());

        assertEquals(720, intervals.get(1).getLowerBound());
        assertEquals(1020, intervals.get(1).getUpperBound());
        assertEquals("1", intervals.get(1).getValue());

        assertEquals(1400, intervals.get(2).getLowerBound());
        assertEquals(1800, intervals.get(2).getUpperBound());
        assertEquals("2", intervals.get(2).getValue());

        assertEquals(1800, intervals.get(3).getLowerBound());
        assertEquals(2600, intervals.get(3).getUpperBound());
        assertEquals("3", intervals.get(3).getValue());

        assertEquals(2600, intervals.get(4).getLowerBound());
        assertEquals(3600, intervals.get(4).getUpperBound());
        assertEquals("4", intervals.get(4).getValue());

        assertEquals(3600, intervals.get(5).getLowerBound());
        assertEquals(Integer.MAX_VALUE, intervals.get(5).getUpperBound());
        assertEquals("5", intervals.get(5).getValue());
    }

    @Test
    @DisplayName("intervalConfigByTakeout Map 配置绑定与分组解析")
    public void testIntervalConfigByTakeout() {
        Map<String, List<DemoConvertProperties.Item>> map = demoConvertProperties.getIntervalConfigByTakeout();
        assertNotNull(map);
        assertTrue(map.containsKey("1"));
        assertTrue(map.containsKey("2"));

        List<DemoConvertProperties.Item> list1 = map.get("1");
        assertEquals(5, list1.size());
        assertEquals(0, list1.get(0).getLowerBound());
        assertEquals(600, list1.get(0).getUpperBound());
        assertEquals("0", list1.get(0).getValue());
        assertEquals(1800, list1.get(4).getLowerBound());
        assertEquals(Integer.MAX_VALUE, list1.get(4).getUpperBound());
        assertEquals("4", list1.get(4).getValue());

        List<DemoConvertProperties.Item> list2 = map.get("2");
        assertEquals(5, list2.size());
        assertEquals(0, list2.get(0).getLowerBound());
        assertEquals(480, list2.get(0).getUpperBound());
        assertEquals("0", list2.get(0).getValue());
        assertEquals(1500, list2.get(4).getLowerBound());
        assertEquals(Integer.MAX_VALUE, list2.get(4).getUpperBound());
        assertEquals("4", list2.get(4).getValue());
    }
}

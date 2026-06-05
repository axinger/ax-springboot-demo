package com.github.axinger;

import com.github.axinger.model.properties.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ConfigurationProperties 统一综合测试
 * 覆盖所有 @ConfigurationProperties 配置类的绑定场景
 */
@SpringBootTest
public class ConfigurationPropertiesTests {

    @Autowired
    private DemoProperties demoProperties;

    @Autowired
    private DemoUserProperties demoUserProperties;

    @Autowired
    private DemoBlockScalarProperties demoBlockScalarProperties;

    @Autowired
    private DemoConvertProperties demoConvertProperties;

    @Autowired
    private DemoPropertySourceProperties demoPropertySourceProperties;

    @Autowired
    private DemoConfigImportProperties demoConfigImportProperties;

    // ==================== 01. 基础类型与 List / Map / 嵌套对象 ====================

    @Test
    @DisplayName("01. 基础类型、List、Map、嵌套对象绑定")
    void test01_basicTypesAndCollections() {
        assertEquals("1.0.1", demoProperties.getVersion());
        assertEquals("dev环境", demoProperties.getDescription());
        assertTrue(demoProperties.isFeatureEnabled());
        assertEquals("development", demoProperties.getFeatureMode());
        assertNotNull(demoProperties.getFeatureList());
        assertEquals(3, demoProperties.getFeatureList().size());

        assertEquals(Long.valueOf(1), demoProperties.getPersonId());
        assertEquals("jim", demoProperties.getPersonName());
        assertEquals(Integer.valueOf(18), demoProperties.getPersonAge());

        assertNotNull(demoProperties.getHumidity());
        assertEquals(40.0, demoProperties.getHumidity().getMin());
        assertEquals(65.0, demoProperties.getHumidity().getMax());

        assertNotNull(demoProperties.getTemperature());
        assertEquals(150.0, demoProperties.getTemperature().getMin());
        assertEquals(300.0, demoProperties.getTemperature().getMax());
    }

    // ==================== 02. 日期时间全家桶 ====================

    @Test
    @DisplayName("02. 日期时间类型绑定：Date / LocalDate / LocalDateTime / LocalTime")
    void test02_dateTimeBindings() {
        assertNotNull(demoProperties.getCreatedDate());
        assertTrue(demoProperties.getCreatedDate() instanceof Date);

        assertNotNull(demoProperties.getUpdatedDateTime());
        assertTrue(demoProperties.getUpdatedDateTime() instanceof LocalDateTime);

        assertEquals(LocalDate.of(2000, 1, 2), demoProperties.getUpdatedDate());
        assertEquals(LocalDate.of(2024, 1, 1), demoProperties.getBirthDate());

        assertNotNull(demoProperties.getUpdatedTime());
        assertTrue(demoProperties.getUpdatedTime() instanceof LocalTime);

        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 30, 0), demoProperties.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 1, 2, 14, 30, 0), demoProperties.getUpdatedAt());

        assertEquals(LocalTime.of(9, 0, 0), demoProperties.getWorkStartTime());
        assertEquals(LocalTime.of(18, 0, 0), demoProperties.getWorkEndTime());
    }

    // ==================== 03. 嵌套对象绑定 ====================

    @Test
    @DisplayName("03. 嵌套对象绑定：DemoProperties.vacation")
    void test03_nestedObject() {
        assertNotNull(demoProperties.getVacation());
        assertEquals(LocalDate.of(2024, 7, 1), demoProperties.getVacation().getFrom());
        assertEquals(LocalDate.of(2024, 7, 15), demoProperties.getVacation().getTo());
    }

    // ==================== 04. record 嵌套 record ====================

    @Test
    @DisplayName("04. record 嵌套 record：DemoUserProperties(demo.user)")
    void test04_recordNestedRecord() {
        assertEquals("jim,\ntom", demoUserProperties.username());
        assertEquals("123\n456\n", demoUserProperties.password());
        assertNotNull(demoUserProperties.dog());
        assertEquals(1, demoUserProperties.dog().min());
        assertEquals(100, demoUserProperties.dog().max());
    }

    // ==================== 05. YAML 块标量绑定 ====================

    @Test
    @DisplayName("05. YAML 块标量绑定：DemoBlockScalarProperties(demo.block)")
    void test05_blockScalars() {
        assertEquals("Hello World\n", demoBlockScalarProperties.getFoldedDefault());
        assertEquals("Hello World", demoBlockScalarProperties.getFoldedStrip());
        assertTrue(demoBlockScalarProperties.getFoldedKeep().startsWith("Hello World"));
        assertTrue(demoBlockScalarProperties.getFoldedKeep().endsWith("\n"));

        assertEquals("Hello\nWorld\n", demoBlockScalarProperties.getLiteralDefault());
        assertEquals("Hello\nWorld", demoBlockScalarProperties.getLiteralStrip());
        assertTrue(demoBlockScalarProperties.getLiteralKeep().startsWith("Hello\nWorld"));
        assertTrue(demoBlockScalarProperties.getLiteralKeep().endsWith("\n"));

        assertNotNull(demoBlockScalarProperties.getJsonConfig());
        assertFalse(demoBlockScalarProperties.getJsonConfig().contains("\n"));
        assertTrue(demoBlockScalarProperties.getSqlScript().contains("\n"));
        assertTrue(demoBlockScalarProperties.getMarkdownText().contains("\n"));
        assertFalse(demoBlockScalarProperties.getLongDescription().contains("\n"));

        assertEquals(3, demoBlockScalarProperties.getMultiLineList().size());
        assertEquals(3, demoBlockScalarProperties.getMultiLineMap().size());
    }

    // ==================== 06. 自定义 Converter ====================

    @Test
    @DisplayName("06. 自定义 Converter 绑定：DemoConvertProperties(demo.convert)")
    void test06_customConverter() {
        assertEquals(5000, demoConvertProperties.getTimeOut());
        assertEquals(5000, demoConvertProperties.getConnectTimeout());

        assertNotNull(demoConvertProperties.getIntervalConfig());
        assertEquals(6, demoConvertProperties.getIntervalConfig().size());
        assertEquals(0, demoConvertProperties.getIntervalConfig().get(0).getLowerBound());
        assertEquals(720, demoConvertProperties.getIntervalConfig().get(0).getUpperBound());
        assertEquals(Integer.MAX_VALUE, demoConvertProperties.getIntervalConfig().get(5).getUpperBound());

        assertNotNull(demoConvertProperties.getIntervalConfigByTakeout());
        assertTrue(demoConvertProperties.getIntervalConfigByTakeout().containsKey("1"));
        assertTrue(demoConvertProperties.getIntervalConfigByTakeout().containsKey("2"));
        assertEquals(5, demoConvertProperties.getIntervalConfigByTakeout().get("1").size());
        assertEquals(5, demoConvertProperties.getIntervalConfigByTakeout().get("2").size());
    }

    // ==================== 07. @PropertySource 外部文件 ====================

    @Test
    @DisplayName("07. @PropertySource 外部文件绑定：DemoPropertySourceProperties(demo-source.yml)")
    void test07_propertySource() {
        assertNotNull(demoPropertySourceProperties.getUser());
        assertEquals("lili", demoPropertySourceProperties.getUser().getUsername());
        assertEquals("123456", demoPropertySourceProperties.getUser().getPassword());

        assertNotNull(demoPropertySourceProperties.getList());
        assertEquals(2, demoPropertySourceProperties.getList().size());
        assertEquals("jim", demoPropertySourceProperties.getList().get(0).getUsername());
        assertEquals("tom", demoPropertySourceProperties.getList().get(1).getUsername());
    }

    // ==================== 08. spring.config.import 外部文件 ====================

    @Test
    @DisplayName("08. spring.config.import 外部文件绑定：DemoConfigImportProperties(demo-config.yml)")
    void test08_configImport() {
        assertNull(demoConfigImportProperties.getUsername());
        assertEquals("123456", demoConfigImportProperties.getPassword());

        assertNotNull(demoConfigImportProperties.getTip());
        assertEquals(1, demoConfigImportProperties.getTip().size());
        assertEquals("1,2", demoConfigImportProperties.getTip().get(0));

        assertNotNull(demoConfigImportProperties.getDog());
        assertEquals(1, demoConfigImportProperties.getDog().size());
        assertEquals("dog", demoConfigImportProperties.getDog().get(0).getName());
    }

    // ==================== 09. @Component + @ConfigurationProperties ====================

    @Test
    @DisplayName("09. @Component + @ConfigurationProperties 双重注解：DemoProperties(demo)")
    void test09_componentConfigurationProperties() {
        assertEquals("axinger", demoProperties.getSysName());
        assertEquals(Integer.valueOf(18), demoProperties.getSysAge());
    }

    // ==================== 10. YAML 特殊语法 ====================

    @Test
    @DisplayName("10. YAML 特殊语法：null / 类型标签 / 引号差异 / 方括号 List")
    void test10_yamlSpecialSyntax() {
        assertNull(demoProperties.getEmpty());
        assertNull(demoProperties.getEmpty2());

        assertEquals("123", demoProperties.getNumber());
        assertTrue(demoProperties.getNumber() instanceof String);

        assertNotNull(demoProperties.getDate());
        assertTrue(demoProperties.getDate() instanceof Date);

        assertEquals(3, demoProperties.getFruits().size());
        assertEquals("apple", demoProperties.getFruits().get(0));

        assertEquals("12:00:00", demoProperties.getTime());
        assertEquals("13:00:00", demoProperties.getTime2());

        assertEquals(Boolean.TRUE, demoProperties.getEnabled());
        assertEquals(Boolean.FALSE, demoProperties.getDisabled());
    }

    // ==================== 11. @EnableConfigurationProperties 批量启用 ====================

    @Test
    @DisplayName("11. @EnableConfigurationProperties 批量启用验证")
    void test11_enableConfigurationProperties() {
        assertNotNull(demoProperties, "DemoProperties 应被正确启用");
        assertNotNull(demoUserProperties, "DemoUserProperties 应被正确启用");
        assertNotNull(demoBlockScalarProperties, "DemoBlockScalarProperties 应被正确启用");
        assertNotNull(demoPropertySourceProperties, "DemoPropertySourceProperties 应被正确启用");
        assertNotNull(demoConfigImportProperties, "DemoConfigImportProperties 应被正确启用");
    }

    // ==================== 12. 三级嵌套自定义对象 ====================

    @Test
    @DisplayName("12. 三级嵌套对象绑定：Company -> Department -> Team -> Member")
    void test12_threeLevelNestedObject() {
        assertNotNull(demoProperties.getCompany());
        assertEquals("AxingerTech", demoProperties.getCompany().getName());

        assertNotNull(demoProperties.getCompany().getDepartment());
        assertEquals("研发部", demoProperties.getCompany().getDepartment().getName());

        assertNotNull(demoProperties.getCompany().getDepartment().getTeam());
        assertEquals("后端组", demoProperties.getCompany().getDepartment().getTeam().getName());

        assertNotNull(demoProperties.getCompany().getDepartment().getTeam().getMembers());
        assertEquals(3, demoProperties.getCompany().getDepartment().getTeam().getMembers().size());
        assertEquals("jim", demoProperties.getCompany().getDepartment().getTeam().getMembers().get(0).getName());
        assertEquals(Integer.valueOf(28), demoProperties.getCompany().getDepartment().getTeam().getMembers().get(0).getAge());
    }

    // ==================== 13. Map 嵌套 List<对象> ====================

    @Test
    @DisplayName("13. Map 嵌套 List<对象>：groupMembers")
    void test13_mapNestedListObject() {
        assertNotNull(demoProperties.getGroupMembers());
        assertTrue(demoProperties.getGroupMembers().containsKey("backend"));
        assertTrue(demoProperties.getGroupMembers().containsKey("frontend"));

        assertEquals(2, demoProperties.getGroupMembers().get("backend").size());
        assertEquals("jim", demoProperties.getGroupMembers().get("backend").get(0).getName());
        assertEquals(Integer.valueOf(28), demoProperties.getGroupMembers().get("backend").get(0).getAge());

        assertEquals(2, demoProperties.getGroupMembers().get("frontend").size());
        assertEquals("lucy", demoProperties.getGroupMembers().get("frontend").get(0).getName());
        assertEquals(Integer.valueOf(24), demoProperties.getGroupMembers().get("frontend").get(0).getAge());
    }

    // ==================== 14. List 嵌套 Map ====================

    @Test
    @DisplayName("14. List 嵌套 Map：metadataList")
    void test14_listNestedMap() {
        assertNotNull(demoProperties.getMetadataList());
        assertEquals(2, demoProperties.getMetadataList().size());

        assertEquals("dev", demoProperties.getMetadataList().get(0).get("env"));
        assertEquals("cn-north", demoProperties.getMetadataList().get(0).get("region"));
        assertEquals("a", demoProperties.getMetadataList().get(0).get("zone"));

        assertEquals("test", demoProperties.getMetadataList().get(1).get("env"));
        assertEquals("cn-south", demoProperties.getMetadataList().get(1).get("region"));
        assertEquals("b", demoProperties.getMetadataList().get(1).get("zone"));
    }

    // ==================== 15. Map 嵌套 Map ====================

    @Test
    @DisplayName("15. Map 嵌套 Map：nestedMap")
    void test15_mapNestedMap() {
        assertNotNull(demoProperties.getNestedMap());
        assertTrue(demoProperties.getNestedMap().containsKey("db"));
        assertTrue(demoProperties.getNestedMap().containsKey("redis"));

        assertEquals("localhost", demoProperties.getNestedMap().get("db").get("host"));
        assertEquals("3306", demoProperties.getNestedMap().get("db").get("port"));
        assertEquals("root", demoProperties.getNestedMap().get("db").get("username"));

        assertEquals("localhost", demoProperties.getNestedMap().get("redis").get("host"));
        assertEquals("6379", demoProperties.getNestedMap().get("redis").get("port"));
        assertEquals("123456", demoProperties.getNestedMap().get("redis").get("password"));
    }
}

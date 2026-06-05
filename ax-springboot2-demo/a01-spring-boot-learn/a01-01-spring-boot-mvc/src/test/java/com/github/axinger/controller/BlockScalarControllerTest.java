package com.github.axinger.controller;

import com.github.axinger.model.properties.DemoBlockScalarProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DemoBlockScalarProperties 直接测试
 * 验证 YAML 块标量绑定到 Spring Boot Properties 的解析行为
 */
@SpringBootTest
public class BlockScalarControllerTest {

    @Autowired
    private DemoBlockScalarProperties demoBlockScalarProperties;

    @Test
    @DisplayName("> 折叠样式：文中换行变空格，文末行为因后缀不同")
    public void testFoldedBlockScalars() {
        assertEquals("Hello World\n", demoBlockScalarProperties.getFoldedDefault());
        assertEquals("Hello World", demoBlockScalarProperties.getFoldedStrip());

        String foldedKeep = demoBlockScalarProperties.getFoldedKeep();
        assertTrue(foldedKeep.startsWith("Hello World"));
        assertTrue(foldedKeep.endsWith("\n"), "Keep 样式应至少保留1个尾随换行");
    }

    @Test
    @DisplayName("| 字面样式：保留文中换行符，文末行为因后缀不同")
    public void testLiteralBlockScalars() {
        assertEquals("Hello\nWorld\n", demoBlockScalarProperties.getLiteralDefault());
        assertEquals("Hello\nWorld", demoBlockScalarProperties.getLiteralStrip());

        String literalKeep = demoBlockScalarProperties.getLiteralKeep();
        assertTrue(literalKeep.startsWith("Hello\nWorld"));
        assertTrue(literalKeep.endsWith("\n"), "Keep 样式应至少保留1个尾随换行");
    }

    @Test
    @DisplayName("实际应用场景：JSON、SQL、Markdown、长文本")
    public void testPracticalConfigs() {
        String jsonConfig = demoBlockScalarProperties.getJsonConfig();
        assertNotNull(jsonConfig);
        assertFalse(jsonConfig.contains("\n"), "JSON 配置应为单行");
        assertTrue(jsonConfig.contains("\"name\""));
        assertTrue(jsonConfig.contains("\"age\""));

        String sqlScript = demoBlockScalarProperties.getSqlScript();
        assertNotNull(sqlScript);
        assertTrue(sqlScript.contains("\n"), "SQL 脚本应保留换行");
        assertTrue(sqlScript.contains("SELECT"));
        assertTrue(sqlScript.contains("LIMIT 100;"));

        String markdownText = demoBlockScalarProperties.getMarkdownText();
        assertNotNull(markdownText);
        assertTrue(markdownText.contains("\n"), "Markdown 应保留换行");
        assertTrue(markdownText.contains("# "), "应包含标题");
        assertTrue(markdownText.contains("- "), "应包含列表");

        String longDescription = demoBlockScalarProperties.getLongDescription();
        assertNotNull(longDescription);
        assertFalse(longDescription.contains("\n"), "长描述应解析为单行");
        assertTrue(longDescription.contains("YAML"));
    }

    @Test
    @DisplayName("集合类型中的块标量：List 和 Map 多行文本")
    public void testCollectionBlockScalars() {
        List<String> multiLineList = demoBlockScalarProperties.getMultiLineList();
        assertNotNull(multiLineList);
        assertEquals(3, multiLineList.size());

        assertFalse(multiLineList.get(0).contains("\n"), "List 第一项应为单行");
        assertTrue(multiLineList.get(0).contains("第一项"));

        assertTrue(multiLineList.get(1).contains("\n"), "List 第二项应保留换行");
        assertTrue(multiLineList.get(1).contains("保留换行的"));

        assertFalse(multiLineList.get(2).contains("\n"), "List 第三项应为单行");
        assertTrue(multiLineList.get(2).contains("\"key\""));

        Map<String, String> multiLineMap = demoBlockScalarProperties.getMultiLineMap();
        assertNotNull(multiLineMap);
        assertTrue(multiLineMap.containsKey("folded"));
        assertTrue(multiLineMap.containsKey("literal"));
        assertTrue(multiLineMap.containsKey("json"));

        assertFalse(multiLineMap.get("folded").contains("\n"), "Map folded 应为单行");
        assertTrue(multiLineMap.get("literal").contains("\n"), "Map literal 应保留换行");
        assertFalse(multiLineMap.get("json").contains("\n"), "Map json 应为单行");
        assertTrue(multiLineMap.get("json").contains("\"status\""));
    }
}

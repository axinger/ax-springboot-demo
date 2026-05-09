package com.github.axinger;

import com.github.axinger.groovy.GroovyUtils;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * 测试从文件加载并执行 Groovy 脚本
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// 按照顺序执行
public class GroovyFileTests {

    @BeforeEach
    public void setUp() {
        // 每次测试前清除缓存，避免变量名冲突
        GroovyUtils.clearCache();
    }

    private String loadScript(String fileName) throws Exception {
        String path = "src/main/resources/groovy/" + fileName;
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    @Test
    @Order(1)
    @DisplayName("01测试加载基本变量脚本")
    public void testBasicVariablesScript() throws Exception {
        String script = loadScript("01-basic-variables.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("基本变量脚本执行完成" + result);
    }

    @Test
    @Order(2)
    @DisplayName("02测试加载List操作脚本")
    public void testListOperationsScript() throws Exception {
        String script = loadScript("02-list-operations.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("List操作脚本执行完成" + result);
    }

    @Test
    @Order(3)
    @DisplayName("03测试加载Map操作脚本")
    public void testMapOperationsScript() throws Exception {
        String script = loadScript("03-map-operations.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("Map操作脚本执行完成" + result);
    }

    @Test
    @Order(4)
    @DisplayName("04测试加载闭包脚本")
    public void testClosuresScript() throws Exception {
        String script = loadScript("04-closures.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("闭包脚本执行完成" + result);
    }

    @Test
    @Order(5)
    @DisplayName("05测试加载条件循环脚本")
    public void testConditionalsLoopsScript() throws Exception {
        String script = loadScript("05-conditionals-loops.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("条件循环脚本执行完成" + result);
    }

    @Test
    @Order(6)
    @DisplayName("06测试加载异常处理脚本")
    public void testExceptionHandlingScript() throws Exception {
        String script = loadScript("06-exception-handling.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("异常处理脚本执行完成" + result);
    }

    @Test
    @Order(7)
    @DisplayName("07测试加载正则表达式脚本")
    public void testRegexScript() throws Exception {
        String script = loadScript("07-regex.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("正则表达式脚本执行完成" + result);
    }

    @Test
    @Order(8)
    @DisplayName("08测试加载日期时间脚本")
    public void testDateTimeScript() throws Exception {
        String script = loadScript("08-date-time.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("日期时间脚本执行完成" + result);
    }

    @Test
    @Order(9)
    @DisplayName("09测试加载JSON处理脚本")
    public void testJsonProcessingScript() throws Exception {
        String script = loadScript("09-json-processing.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("JSON处理脚本执行完成" + result);
    }

    @Test
    @Order(10)
    @DisplayName("10测试加载类对象脚本")
    public void testClassesObjectsScript() throws Exception {
        String script = loadScript("10-classes-objects.groovy");
        Object result = GroovyUtils.execute(script, new HashMap<>());
        System.out.println("类对象脚本执行完成" + result);
    }
}

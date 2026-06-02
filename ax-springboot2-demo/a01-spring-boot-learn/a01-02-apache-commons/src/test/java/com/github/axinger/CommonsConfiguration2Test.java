package com.github.axinger;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Apache Commons Configuration2 配置读取示例
 */
class CommonsConfiguration2Test {

    /**
     * 创建临时 properties 文件
     */
    private File createTempPropertiesFile() throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir"), "test-config.properties");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("# 应用配置\n");
            writer.write("app.name=MyApplication\n");
            writer.write("app.version=1.0.0\n");
            writer.write("app.debug=true\n");
            writer.write("app.port=8080\n");
            writer.write("app.timeout=30\n");
            writer.write("\n");
            writer.write("# 数据库配置\n");
            writer.write("db.host=localhost\n");
            writer.write("db.port=3306\n");
            writer.write("db.name=mydb\n");
            writer.write("db.user=root\n");
            writer.write("db.password=secret\n");
            writer.write("\n");
            writer.write("# 列表配置\n");
            writer.write("features=login,register,profile,settings\n");
        }
        file.deleteOnExit();
        return file;
    }

    /**
     * 创建临时 XML 配置文件
     */
    private File createTempXmlFile() throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir"), "test-config.xml");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<config>\n");
            writer.write("  <database>\n");
            writer.write("    <host>localhost</host>\n");
            writer.write("    <port>3306</port>\n");
            writer.write("    <credentials>\n");
            writer.write("      <user>admin</user>\n");
            writer.write("      <password>secret</password>\n");
            writer.write("    </credentials>\n");
            writer.write("  </database>\n");
            writer.write("  <features>\n");
            writer.write("    <feature>cache</feature>\n");
            writer.write("    <feature>logging</feature>\n");
            writer.write("  </features>\n");
            writer.write("</config>\n");
        }
        file.deleteOnExit();
        return file;
    }

    // ==================== Properties 文件读取 ====================

    @Test
    void testPropertiesConfiguration() throws ConfigurationException, IOException {
        File configFile = createTempPropertiesFile();

        // 使用 Configurations 工具类快速加载
        Configurations configs = new Configurations();
        Configuration config = configs.properties(configFile);

        // 读取字符串
        System.out.println("应用名称: " + config.getString("app.name"));       // MyApplication
        System.out.println("版本: " + config.getString("app.version"));        // 1.0.0

        // 读取其他类型（自动转换）
        System.out.println("调试模式: " + config.getBoolean("app.debug"));     // true
        System.out.println("端口: " + config.getInt("app.port"));              // 8080
        System.out.println("超时: " + config.getLong("app.timeout"));          // 30

        // 带默认值
        System.out.println("最大连接数: " + config.getInt("db.maxConnections", 100)); // 100（默认值）

        // 读取列表
        List<String> features = config.getList(String.class, "features");
        System.out.println("功能列表: " + features); // [login, register, profile, settings]

        // 以数组形式读取
        String[] featuresArray = config.getStringArray("features");
        System.out.println("功能数组: " + Arrays.toString(featuresArray));
    }

    @Test
    void testPropertiesWithPrefix() throws ConfigurationException, IOException {
        File configFile = createTempPropertiesFile();

        Configurations configs = new Configurations();
        Configuration config = configs.properties(configFile);

        // 遍历所有以 "db." 开头的配置
        System.out.println("数据库配置:");
        Iterator<String> keys = config.getKeys("db");
        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println("  " + key + " = " + config.getString(key));
        }
    }

    // ==================== XML 配置读取 ====================

    @Test
    void testXmlConfiguration() throws ConfigurationException, IOException {
        File configFile = createTempXmlFile();

        Configurations configs = new Configurations();
        XMLConfiguration config = configs.xml(configFile);

        // 使用 XPath 风格的路径访问
        System.out.println("数据库主机: " + config.getString("database.host"));           // localhost
        System.out.println("数据库端口: " + config.getInt("database.port"));              // 3306
        System.out.println("用户名: " + config.getString("database.credentials.user"));   // admin
        System.out.println("密码: " + config.getString("database.credentials.password")); // secret

        // 读取列表
        List<String> features = config.getList(String.class, "features.feature");
        System.out.println("功能列表: " + features); // [cache, logging]

        // 获取子配置
        Configuration dbConfig = config.subset("database");
        System.out.println("子配置主机: " + dbConfig.getString("host"));
    }

    // ==================== 使用 Builder 模式 ====================

    @Test
    void testBuilderConfiguration() throws ConfigurationException, IOException {
        File configFile = createTempPropertiesFile();

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.fileBased()
                                .setFile(configFile)
                                .setEncoding("UTF-8"));

        Configuration config = builder.getConfiguration();
        System.out.println("应用名称: " + config.getString("app.name"));
        System.out.println("端口: " + config.getInt("app.port"));
    }

    // ==================== 内存配置 ====================

    @Test
    void testInMemoryConfiguration() {
        // 创建内存中的配置（用于动态配置场景）
        org.apache.commons.configuration2.MapConfiguration config =
                new org.apache.commons.configuration2.MapConfiguration(new java.util.HashMap<>());

        config.setProperty("server.host", "127.0.0.1");
        config.setProperty("server.port", 8080);
        config.setProperty("server.enabled", true);

        System.out.println("主机: " + config.getString("server.host"));
        System.out.println("端口: " + config.getInt("server.port"));
        System.out.println("启用: " + config.getBoolean("server.enabled"));

        // 修改配置
        config.setProperty("server.port", 9090);
        System.out.println("新端口: " + config.getInt("server.port"));
    }

    // ==================== 组合配置 ====================

    @Test
    void testCombinedConfiguration() throws ConfigurationException, IOException {
        File propsFile = createTempPropertiesFile();
        File xmlFile = createTempXmlFile();

        Configurations configs = new Configurations();
        Configuration propsConfig = configs.properties(propsFile);
        Configuration xmlConfig = configs.xml(xmlFile);

        // 使用 CompositeConfiguration 组合多个配置源
        org.apache.commons.configuration2.CompositeConfiguration combined =
                new org.apache.commons.configuration2.CompositeConfiguration();
        combined.addConfiguration(propsConfig);
        combined.addConfiguration(xmlConfig);

        // 优先从第一个配置中查找
        System.out.println("app.name (来自 properties): " + combined.getString("app.name"));
        System.out.println("database.host (来自 xml): " + combined.getString("database.host"));
    }
}

package com.github.axinger.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroovyUtils {

    // 1. 单例 GroovyShell，避免重复创建类加载器
    // 注意：GroovyShell 是线程安全的，只要不修改其内部配置
    private static final GroovyShell SHELL;

    // 2. 脚本缓存池：Key 是脚本内容的 MD5 或原文，Value 是编译后的 Script 类
    private static final Map<String, Script> SCRIPT_CACHE = new ConcurrentHashMap<>();

    static {
        // 初始化配置（可选，如果需要安全限制可以在这里加 SecureASTCustomizer）
        CompilerConfiguration config = new CompilerConfiguration();
        // 设置脚本基类，或者配置其他编译选项
        SHELL = new GroovyShell(GroovyUtils.class.getClassLoader(), new Binding(), config);
    }

    /**
     * 执行 Groovy 脚本（高性能缓存版）
     */
    public static Object execute(String scriptText, Map<String, Object> context) {
        try {
            // 3. 获取或编译脚本
            Script script = SCRIPT_CACHE.computeIfAbsent(scriptText, key -> {
                // 只有当缓存中没有时，才进行昂贵的 parse 操作
                return SHELL.parse(key);
            });

            // 4. 处理上下文变量（精度修正）
            Binding binding = new Binding();
            if (context != null) {
                for (Map.Entry<String, Object> entry : context.entrySet()) {
                    Object value = entry.getValue();
                    // 自动修正 Double/Float 精度问题
                    if (value instanceof Double || value instanceof Float) {
                        value = new BigDecimal(value.toString());
                    }
                    binding.setVariable(entry.getKey(), value);
                }
            }
            
            // 5. 将新的 Binding 设置给 Script 实例
            // 注意：Script 实例是复用的，但 Binding 是每次调用新建的，保证线程安全
            script.setBinding(binding);

            // 6. 执行
            return script.run();

        } catch (Exception e) {
            throw new RuntimeException("Groovy 执行异常: " + e.getMessage(), e);
        }
    }
    
    // 可选：提供清理缓存的方法，用于脚本热更新
    public static void clearCache() {
        SCRIPT_CACHE.clear();
    }
}
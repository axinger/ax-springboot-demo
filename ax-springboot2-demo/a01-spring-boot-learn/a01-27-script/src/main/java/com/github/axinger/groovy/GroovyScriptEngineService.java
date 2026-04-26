package com.github.axinger.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于 Apache Groovy 的动态脚本引擎
 * 模拟 QLExpress4 的行为
 */
@Component
public class GroovyScriptEngineService {

    /**
     * 执行 Groovy 脚本
     *
     * @param scriptText 脚本内容
     * @param context    上下文变量 (Map)
     * @param options    执行选项
     * @return 执行结果包装类
     */
    public GroovyResult execute(String scriptText, Map<String, Object> context, GroovyOptions options) {
        try {
            // 1. 准备上下文 Binding
            // 注意：Groovy 的 Binding 会持有变量的引用，脚本内修改会直接影响传入的 Map
            Map<String, Object> processedContext = context != null ? context : new HashMap<>();

            // --- 精度配置 ---
            // 当开启精确模式时，将 Double/Float 自动转换为 BigDecimal
            if (options.isPrecise()) {
                processedContext = convertToBigDecimal(processedContext);
            }

            Binding binding = new Binding(processedContext);

            // 2. 配置编译器
            CompilerConfiguration config = new CompilerConfiguration();

            // --- 安全配置 (模拟 QLExpress 的安全策略) ---
            if (options.getSecurityStrategy() != SecurityStrategy.OPEN) {
                SecureASTCustomizer secure = new SecureASTCustomizer();
                secure.setClosuresAllowed(true); // 允许闭包
                secure.setMethodDefinitionAllowed(false); // 禁止在脚本中定义新方法
                secure.setAllowedImports(Collections.emptyList()); // 默认禁止导入
                secure.setAllowedStarImports(Collections.emptyList());

                if (options.getSecurityStrategy() == SecurityStrategy.WHITE_LIST) {
                    // 白名单模式：仅允许 java.lang 和 java.math 等基础包
                    secure.setAllowedImports(Collections.singletonList("java.math.BigDecimal"));
                }
                config.addCompilationCustomizers(secure);
            }

            // 3. 执行脚本
            GroovyShell shell = new GroovyShell(binding, config);
            Script script = shell.parse(scriptText);
            Object result = script.run();

            return new GroovyResult(result, binding.getVariables());

        } catch (Exception e) {
            throw new RuntimeException("Groovy 执行异常: " + e.getMessage(), e);
        }
    }

    /**
     * 将 Map 中的 Double/Float 转换为 BigDecimal，确保精确计算
     *
     * @param context 原始上下文
     * @return 转换后的上下文
     */
    private Map<String, Object> convertToBigDecimal(Map<String, Object> context) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Double) {
                // 使用字符串构造避免精度丢失
                result.put(entry.getKey(), new BigDecimal(value.toString()));
            } else if (value instanceof Float) {
                result.put(entry.getKey(), new BigDecimal(value.toString()));
            } else {
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

}
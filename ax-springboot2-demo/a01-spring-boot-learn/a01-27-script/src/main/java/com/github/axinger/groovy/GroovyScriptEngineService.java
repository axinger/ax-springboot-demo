package com.github.axinger.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.stereotype.Component;

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
            Binding binding = new Binding(context != null ? context : new HashMap<>());

            // 2. 配置编译器
            CompilerConfiguration config = new CompilerConfiguration();

            // --- 安全配置 (模拟 QLExpress 的安全策略) ---
            if (options.getSecurityStrategy() != SecurityStrategy.OPEN) {
                SecureASTCustomizer secure = new SecureASTCustomizer();
                secure.setClosuresAllowed(true); // 允许闭包
                secure.setMethodDefinitionAllowed(false); // 禁止在脚本中定义新方法
                secure.setImportsWhitelist(Collections.emptyList()); // 默认禁止导入
                secure.setStarImportsWhitelist(Collections.emptyList());

                if (options.getSecurityStrategy() == SecurityStrategy.WHITE_LIST) {
                    // 白名单模式：仅允许 java.lang 和 java.math 等基础包
                    secure.setImportsWhitelist(Collections.singletonList("java.math.BigDecimal"));
                }
                config.addCompilationCustomizers(secure);
            }

            // --- 精度配置 ---
            // Groovy 默认对字面量小数使用 BigDecimal，但对 Double 类型运算会使用 double
            // 如果需要强制高精度，建议在传入 context 前将数字转为 BigDecimal
            if (options.isPrecise()) {
                // 这里可以添加 AST 转换，强制将所有数字运算转为 BigDecimal 调用
                // 为简化演示，此处依赖 Groovy 默认行为 + 用户传入 BigDecimal 类型
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

}
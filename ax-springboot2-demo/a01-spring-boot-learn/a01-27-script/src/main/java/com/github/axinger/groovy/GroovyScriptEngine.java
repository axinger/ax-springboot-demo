package com.github.axinger.groovy;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Groovy 脚本引擎单例封装类
 * 提供类似 QLExpress4 的静态方法访问方式，同时支持 Spring 注入
 */
@Component
public class GroovyScriptEngine {

    private static GroovyScriptEngineService staticEngine;

    private final GroovyScriptEngineService engine;

    public GroovyScriptEngine(GroovyScriptEngineService engine) {
        this.engine = engine;
    }

    @PostConstruct
    public void init() {
        staticEngine = this.engine;
    }

    /**
     * 静态方法执行脚本（类似 QLExpress 的单例模式）
     *
     * @param scriptText 脚本内容
     * @param context    上下文变量
     * @return 执行结果
     */
    public static GroovyResult execute(String scriptText, Map<String, Object> context) {
        if (staticEngine == null) {
            throw new IllegalStateException("GroovyScriptEngine 尚未初始化，请确保 Spring 上下文已加载");
        }
        return staticEngine.execute(scriptText, context, GroovyOptions.builder().build());
    }

    /**
     * 静态方法执行脚本（带选项）
     *
     * @param scriptText 脚本内容
     * @param context    上下文变量
     * @param options    执行选项
     * @return 执行结果
     */
    public static GroovyResult execute(String scriptText, Map<String, Object> context, GroovyOptions options) {
        if (staticEngine == null) {
            throw new IllegalStateException("GroovyScriptEngine 尚未初始化，请确保 Spring 上下文已加载");
        }
        return staticEngine.execute(scriptText, context, options);
    }

    /**
     * 快速执行表达式并返回结果
     *
     * @param expression 表达式
     * @param context    上下文变量
     * @return 结果值
     */
    public static Object eval(String expression, Map<String, Object> context) {
        return execute(expression, context).result();
    }

    /**
     * 快速执行表达式并返回指定类型
     *
     * @param expression 表达式
     * @param context    上下文变量
     * @param type       返回类型
     * @param <T>        泛型类型
     * @return 结果值
     */
    public static <T> T eval(String expression, Map<String, Object> context, Class<T> type) {
        Object result = execute(expression, context).result();
        if (result == null) {
            return null;
        }
        if (type.isInstance(result)) {
            return type.cast(result);
        }
        throw new ClassCastException("无法将 " + result.getClass() + " 转换为 " + type);
    }
}

package com.github.axinger.groovy;

import java.util.Map;

// 结果包装类
public class GroovyResult {
    private final Object result;
    private final Map<String, Object> context;

    public GroovyResult(Object result, Map<String, Object> context) {
        this.result = result;
        this.context = context;
    }

    public Object getResult() {
        return result;
    }

    public Map<String, Object> getContext() {
        return context;
    }
}
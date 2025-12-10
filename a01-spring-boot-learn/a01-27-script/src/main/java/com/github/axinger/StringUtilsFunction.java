package com.github.axinger;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * 字符串处理函数
 */
public class StringUtilsFunction extends AbstractVariadicFunction {

    @Override
    public String getName() {
        return "strUtil";
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("至少需要2个参数：操作类型和字符串");
        }

        String operation = FunctionUtils.getStringValue(args[0], env);
        String str = FunctionUtils.getStringValue(args[1], env);

        switch (operation) {
            case "reverse":
                return new AviatorString(new StringBuilder(str).reverse().toString());
            case "capitalize":
                return new AviatorString(
                    str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase());
            case "repeat":
                if (args.length < 3) {
                    throw new IllegalArgumentException("repeat操作需要重复次数参数");
                }
                int times = FunctionUtils.getNumberValue(args[2], env).intValue();
                return new AviatorString(str.repeat(times));
            default:
                throw new IllegalArgumentException("不支持的操作: " + operation);
        }
    }
}

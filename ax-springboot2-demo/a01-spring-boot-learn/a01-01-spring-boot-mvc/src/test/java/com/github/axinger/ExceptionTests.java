package com.github.axinger;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.junit.jupiter.api.Test;

public class ExceptionTests {

    @Test
    public void test() {
        try {
            try {
                int a = 1 / 0;
            } catch (Exception e) {
                throw ExceptionUtil.wrapRuntime("分母不能是0");
//                throw new RuntimeException("分母不能是0");

            }

        } catch (Exception e) {
//            System.out.println("e=" + e.getMessage());

            String rootCauseMessage = ExceptionUtil.getRootCauseMessage(e);
            System.out.println("rootCauseMessage = " + rootCauseMessage); //RuntimeException: 分母不能是0
            String simpleMessage = ExceptionUtil.getSimpleMessage(e);
            System.out.println("simpleMessage = " + simpleMessage); //分母不能是0
            String message = ExceptionUtil.getMessage(e);
            System.out.println("message = " + message); //RuntimeException: 分母不能是0

            String stacktracedToString = ExceptionUtil.stacktraceToString(e);
            System.out.println("stacktracedToString = " + stacktracedToString);
            String stacktraced = ExceptionUtil.stacktraceToString(e, 100);
            System.out.println("stacktraced = " + stacktraced);

//            StackTraceElement[] traceElements = ExceptionUtil.getStackElements();
//            for (StackTraceElement traceElement : traceElements) {
//                System.out.println("traceElement = " + traceElement);
//            }
        }
    }
}

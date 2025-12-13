package com.github.axinger;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class 表达式_qlexpress3Tests {

    @SneakyThrows
    @Test
    public void test1() {
        String rule = """
                    if (orderAmount >= 200) {
                        return orderAmount * 0.8;
                    } else if (orderAmount >= 100) {
                        return orderAmount * 0.9;
                    } else {
                        return orderAmount;
                    }
                """;

        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("orderAmount", 250.0);

        Object result = runner.execute(rule, context, null, true, false);
        System.out.println("finalPrice = " + result); // 输出 200.0
    }

}

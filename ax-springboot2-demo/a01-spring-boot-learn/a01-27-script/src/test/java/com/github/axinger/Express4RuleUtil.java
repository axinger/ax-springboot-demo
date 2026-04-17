package com.github.axinger;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.QLResult;
import com.alibaba.qlexpress4.runtime.trace.ExpressionTrace;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
public class Express4RuleUtil {

    /**
     * QLExpress4 Runner 单例
     */
    private static final Express4Runner EXPRESS_RUNNER = new Express4Runner(InitOptions.DEFAULT_OPTIONS);


    public static Object execute(String expression, Map<String, Object> context) {
        if (expression == null) {
            log.debug("未找到规则类型: expression");
            return null;
        }

        try {
            QLResult result = EXPRESS_RUNNER.execute(
                    expression,
                    context,
                    QLOptions.builder().cache(true).build()
            );

            Object obj = result.getResult();

            List<ExpressionTrace> traces = result.getExpressionTraces();
            if (traces != null && !traces.isEmpty()) {
            }

            if (obj == null) {
                return null;
            }

//            if (obj instanceof BigDecimal) {
//                log.debug("规则匹配成功: expression={}, value={}, score={}", expression, context.keySet(), obj);
//                return (BigDecimal) obj;
//            } else if (obj instanceof Number) {
//                BigDecimal score = new BigDecimal(obj.toString());
//                log.debug("规则匹配成功(数值转换): expression={}, value={}, score={}", expression, context.keySet(), score);
//                return score;
//            } else {
//                log.warn("规则返回非数值类型: expression={}, result={}", expression, obj);
//                return null;
//            }
            return obj;
        } catch (Exception e) {
            log.error("规则执行异常: expression={}, value={}, error={}", expression, context.keySet(), e.getMessage(), e);
            return null;
        }
    }


}

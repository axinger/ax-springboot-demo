package com.github.axinger.service;

import com.alibaba.qlexpress4.Express4Runner;
import com.alibaba.qlexpress4.InitOptions;
import com.alibaba.qlexpress4.QLOptions;
import com.alibaba.qlexpress4.security.QLSecurityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QLExecuteService {
    
    private final Express4Runner runner =
        new Express4Runner(InitOptions.builder().securityStrategy(QLSecurityStrategy.open()).build());
    
    @Autowired
    private ApplicationContext applicationContext;
    
    public Object execute(String script, Map<String, Object> context) {
        QLSpringContext springContext = new QLSpringContext(context, applicationContext);
        return runner.execute(script, springContext, QLOptions.DEFAULT_OPTIONS).getResult();
    }
}

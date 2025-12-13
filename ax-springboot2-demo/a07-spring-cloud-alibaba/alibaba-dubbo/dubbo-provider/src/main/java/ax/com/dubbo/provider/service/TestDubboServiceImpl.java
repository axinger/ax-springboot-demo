package ax.com.dubbo.provider.service;

import com.github.axinger.api.service.TestDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName AccountServiceImpl.java
 * @Description TODO
 * @createTime 2021年12月18日 21:37:00
 */

//@DubboService(version = "1.0.0", interfaceClass = TestDubboService.class)
@Component
@DubboService(
        version = "1.0.0",
        interfaceClass = TestDubboService.class
//        , loadbalance = "roundrobin"
)
public class TestDubboServiceImpl implements TestDubboService {

    @Override
    public Object test1(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "Dubbo调用，" + name);
        return map;
    }
}

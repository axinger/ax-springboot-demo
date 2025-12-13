package axing.cloud.gateway.handler;

import axing.cloud.gateway.bean.FetchGatewayProperties;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class FetchGatewayInterceptor implements HandlerInterceptor {

    private final FetchGatewayProperties properties;

    public FetchGatewayInterceptor(FetchGatewayProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!properties.getMustFetchGateway()) {
            return true;
        }
        String token = request.getHeader(properties.getFetchGatewayKey());
        String gatewayToken = properties.getFetchGatewayValue();
        if (ObjectUtil.equals(gatewayToken, token)) {
            return true;
        } else {
            throw new RuntimeException("请通过网关访问资源");
        }
    }

    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    //     HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    // }
    //
    // @Override
    // public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    //     HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    // }
}


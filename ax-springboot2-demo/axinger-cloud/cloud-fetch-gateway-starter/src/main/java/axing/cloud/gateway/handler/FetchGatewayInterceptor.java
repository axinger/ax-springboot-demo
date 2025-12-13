package axing.cloud.gateway.handler;

import axing.cloud.gateway.bean.FetchGatewayProperties;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FetchGatewayInterceptor implements HandlerInterceptor {

    private final FetchGatewayProperties properties;

    public FetchGatewayInterceptor(FetchGatewayProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
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

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}


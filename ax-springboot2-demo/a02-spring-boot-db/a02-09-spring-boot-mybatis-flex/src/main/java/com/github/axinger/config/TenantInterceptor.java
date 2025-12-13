package com.github.axinger.config;

import com.mybatisflex.core.tenant.TenantManager;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

        //通过 request 去获取租户 ID
//        Long tenantId = getTenantIdByReuqest(request);
        Long tenantId = 100L;

        //设置租户ID到 request 的 attribute
        request.setAttribute("tenantId", tenantId);


        TenantManager.setTenantFactory(() -> {
            //通过这里返回当前租户 ID
            return new Object[]{tenantId};
        });

        return true;
    }
}

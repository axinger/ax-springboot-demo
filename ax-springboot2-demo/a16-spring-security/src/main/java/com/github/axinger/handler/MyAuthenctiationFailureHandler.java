//package com.github.axinger.handler;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@Component
//public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//
//    public MyAuthenticationFailureHandler() {
//        this.setDefaultFailureUrl("/loginPage");
//    }
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        logger.info("进入认证失败处理类");
//        HttpSession session = request.getSession();
//        AuthenticationException attributes = (AuthenticationException) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
//        logger.info("aaaa " + attributes);
//        super.onAuthenticationFailure(request, response, exception);
//    }
//}

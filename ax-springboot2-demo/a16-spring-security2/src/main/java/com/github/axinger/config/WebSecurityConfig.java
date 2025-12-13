package com.github.axinger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Configuration 注解表示将该类以配置类的方式注册到spring容器中
 */
@Configuration
/**
 * @EnableWebSecurity 注解表示启动spring security
 */
@EnableWebSecurity
/**
 * @EnableMethodSecurity 注解表示启动全局函数权限
 */
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * 权限不足处理逻辑
     */
    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    /**
     * 未授权处理逻辑
     */
    @Autowired
    private MyAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 访问统一处理器
     */
    @Autowired
    private TokenFilter authenticationTokenFilter;

    /**
     * 自定义权限校验逻辑
     */
    @Autowired
    private MyAuthorizationManager myAuthorizationManager;


    /**
     * 明文密码加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 忽略权限校验
     *
     * @return
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web -> web
//                .ignoring().requestMatchers("/error",
//                        "/resources/**",
//                        "/hello",
//                        "/login",
//                        "/auth/**",
//                        "/static/**",
//                        "/public/**",
//                        "/h2-console/**",
//                        "/swagger-ui.html",
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**",
//                        "/webjars/**",
//                        "/v2/api-docs/**",
//                        "/doc.html",
//                        "/swagger-resources/**")
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//        );
//    }


//    @Bean
//    public PersistentTokenRepository tokenRepository() {
//        // 使用内存存储令牌（生产环境应使用数据库）
//        return new InMemoryTokenRepositoryImpl();
//    }

//    @Bean
//    public TokenService tokenService()  throws Exception{
//        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
//        tokenService.setSecureRandom(SecureRandom.getInstanceStrong()); // 强随机数生成器
//        tokenService.setServerSecret("mySecretKey"); // 服务器密钥，应保持安全
//        tokenService.setServerInteger(12345); // 服务器标识整数
//        tokenService.setPseudoRandomNumberBytes(16); // 伪随机数字节数
//        return tokenService;
//    }

    /**
     * spring security的核心过滤器链
     *
     * @param http
     * @return
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // 定义安全请求拦截规则
        http.authorizeHttpRequests(registry -> {
                    registry
//                            .requestMatchers("/hello").permitAll() // hello 接口放行，不进行权限校验
//                            .requestMatchers("/auth/**", "/auth/refreshToken").permitAll()
//                            .requestMatchers("/login", "/auth/refreshToken").permitAll()
//                            .requestMatchers("/", "/auth/refreshToken").permitAll()
                            .requestMatchers("/error",
                                    "/resources/**",
                                    "/hello",
                                    "/login",
                                    "/auth/**",
                                    "/static/**",
                                    "/public/**",
                                    "/h2-console/**",
                                    "/swagger-ui.html",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/webjars/**",
                                    "/v2/api-docs/**",
                                    "/doc.html",
                                    "/swagger-resources/**").permitAll()
                            .anyRequest()
//                             .hasRole()//他接口不进行role具体校验，进行动态权限校验
                            .access(myAuthorizationManager); // 动态权限校验逻辑
                })
//                .authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//
//                .and()
                // 前后端分离，关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //默认的HTTP Basic Auth认证
                .httpBasic(Customizer.withDefaults())
                //默认的表单登录
//                .formLogin(Customizer.withDefaults())
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/auth/login")
//                )                // 前后端分离架构禁用session
                .formLogin()
                .disable()  // 禁用默认的登录表单
                .logout()
                .disable().sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // 访问异常处理
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
                })
                // 未授权异常处理
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
                })
                .headers(httpSecurityHeadersConfigurer -> {
                    // 禁用缓存
                    httpSecurityHeadersConfigurer.cacheControl(HeadersConfigurer.CacheControlConfig::disable);
                    httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                });
        // 添加入口filter， 前后端分离的时候，可以进行token解析操作
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
//
//
////                 .addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http.authorizeHttpRequests(auth -> auth
////                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
////                .anyRequest().authenticated()
//                        //
//                        .anyRequest()
////                 .hasRole() //其他接口不进行role具体校验，进行动态权限校验
//                        .access(myAuthorizationManager) // 动态权限校验逻辑
//        );
//        http
//                .cors()//新加入
//                .and()
//                .csrf().disable() // 取消跨站请求伪造防护
////                .authorizeRequests()
//                .authorizeHttpRequests()
//                //"/login"不进行权限验证
////                .antMatchers("/login").permitAll()
////                .antMatchers("/favicon.ico").permitAll()
//
//                .anyRequest()
//                .authenticated()   //其他的需要登陆后才能访问
////                .accessDecisionManager(customAccessDecisionManager())  // 自定义权限决策管理器
//                .and()
//
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
////                        .anyRequest().authenticated()
////                )
////                .formLogin(form -> form
////                        .loginPage("/login")
////                        .loginProcessingUrl("/auth/login")
////                )
////                .formLogin()
//                // 表示登录页的地址，例如当你访问一个需要登录后才能访问的资源时，系统就会自动给你通过【重定向】跳转到这个页面上来
////                .loginPage("/login")
////                // 表示处理登录请求的接口地址，默认为 /login
////                .loginProcessingUrl("/auth/login")
////                // 定义登录时，用户名的 key，默认为 username
////                .usernameParameter("username")
////                // 定义登录时，密码的 key，默认为 password
////                .passwordParameter("password")
////                .and()
//                .formLogin()
//                .disable()  // 禁用默认的登录表单
//                .logout()
//                .disable()
//                // 异常处理配置
//                .exceptionHandling()
//                // 自定义未认证处理逻辑
//                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
//                // 自定义无权限处理逻辑
//                .accessDeniedHandler(new MyAccessDeniedHandler())
//        ;
//        return http.build();
    }

}

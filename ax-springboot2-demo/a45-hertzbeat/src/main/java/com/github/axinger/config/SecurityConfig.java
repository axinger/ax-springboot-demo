//package com.github.axinger.config;
//
///*
//注意：如果你的项目里还引入了认证相关的依赖，比如springboot-security,那么SpringBoot Actuator暴露出的接口可能会被拦截，此时需要你手动放开这些接口，以springboot-security为例，需要在SecurityConfig配置类中加入以下代码：
// */
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity
//                // 配置要放开的接口 -----------------------------------
//                .antMatchers("/actuator/**").permitAll()
//                .antMatchers("/metrics/**").permitAll()
//                .antMatchers("/trace").permitAll()
//                .antMatchers("/heapdump").permitAll()
//                // 。。。
//                // 其他接口请参考：https://blog.csdn.net/JHIII/article/details/126601858 -----------------------------------
//    }
//}
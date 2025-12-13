# 跨域

```text

 * 全局跨域
 * https://www.cnblogs.com/antLaddie/p/14751540.html
 * 五种解决方式：
 * ①：返回新的CorsFilter
 * ②：重写WebMvcConfigurer
 * ③：使用注解@CrossOrigin
 * ④：手动设置响应头（HttpServletResponse）参考第一章第四节注意：
 * CorFilter / WebMvConfigurer / @CrossOrigin 需要 SpringMVC 4.2以上版本才支持，对应springBoot 1.3版本以上
 * 上面前两种方式属于全局 CORS 配置，后两种属于局部 CORS配置。如果使用了局部跨域是会覆盖全局跨域的规则，所以可以通过 @CrossOrigin 注解来进行细粒度更高的跨域资源控制。
 * 其实无论哪种方案，最终目的都是修改响应头，向响应头中添加浏览器所要求的数据，进而实现跨域
     
```

# 单点登录

```text
不同域名,比如,淘宝和天猫,spring session 不支持,需要使用SSO解决方案,
```

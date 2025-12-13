package com.github.axinger.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName IndexController.java
 * @Description TODO
 * @createTime 2022年01月23日 00:34:00
 */

@RestController
public class IndexController {

    @RequestMapping("/")
    public Object index() {
        Map map = new HashMap(2);
        map.put("msg", "index");
        return map;
    }


    @RequestMapping("/login")
    public Object login(String username, String password) {

        Subject subject = SecurityUtils.getSubject();
        // 提前登出一次,解决缓存问题,缺点:用户误操作,需要重新登录
        subject.logout();
        // 用户是否认证过
        if (!subject.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            try {
                // 登录失败报错各种异常
                subject.login(token);
                System.out.println("验证成功==============");
            } catch (Exception e) {
                System.out.println("login e = " + e);
            }

        }
        Map map = new HashMap(2);
        map.put("msg", "登录成功");
        return map;
    }

    @RequestMapping("/logout")
    public Object logout(String username, String password) {
        Map map = new HashMap(2);
        map.put("msg", "logout");

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return map;
    }

    @RequestMapping("/success")
    public Object success() {
        Map map = new HashMap(2);
        map.put("msg", "success");
        return map;
    }

    @RequestMapping("/noPermission")
    public Object noPermission() {
        Map map = new HashMap(2);
        map.put("msg", "noPermission");
        return map;
    }

    @RequestMapping("/admin/test1")
    @RequiresAuthentication
    public Object test1() {
        Map map = new HashMap(2);
        map.put("msg", "admin/test1");
        return map;
    }

    @RequestMapping("/admin/test2")
    @RequiresRoles(value = {"admin"})
    public Object test2() {

        Map map = new HashMap(2);
        map.put("msg", "admin/test2 需要角色才能返回");
        return map;
    }

    @RequestMapping("/admin/test3")
    @RequiresRoles(value = {"admin"})
    @RequiresPermissions(value = {"add"}, logical = Logical.OR)
    public Object test3() {

        /* 方法调用 验证权限 和注解效果一样*/

        /**
         Subject subject = SecurityUtils.getSubject();
         subject.checkRole("admin");
         subject.checkPermission("add");
         **/


        Map map = new HashMap(2);
        map.put("msg", "admin/test3 权限");
        return map;
    }
}


/**
 * shiro注解权限控制-5个权限注解
 * <p>
 * Shiro共有5个注解，接下来我们就详细说说吧
 * <p>
 * RequiresAuthentication:
 * <p>
 * 使用该注解标注的类，实例，方法在访问或调用时，当前Subject必须在当前session中已经过认证。
 * <p>
 * RequiresGuest:
 * <p>
 * 使用该注解标注的类，实例，方法在访问或调用时，当前Subject可以是“gust”身份，不需要经过认证或者在原先的session中存在记录。
 * <p>
 * RequiresPermissions:
 * <p>
 * 当前Subject需要拥有某些特定的权限时，才能执行被该注解标注的方法。如果当前Subject不具有这样的权限，则方法不会被执行。
 * <p>
 * RequiresRoles:
 * <p>
 * 当前Subject必须拥有所有指定的角色时，才能访问被该注解标注的方法。如果当天Subject不同时拥有所有指定角色，则方法不会执行还会抛出AuthorizationException异常。
 * <p>
 * RequiresUser
 * <p>
 * 当前Subject必须是应用的用户，才能访问或调用被该注解标注的类，实例，方法。
 *
 * @RequiresAuthentication 验证用户是否登录，跟subject.isAuthenticated() 结果为true时一样。
 * @RequiresUser 验证用户是否被记忆，user有两种含义：
 * <p>
 * 成功登录的（subject.isAuthenticated() 结果为true）；
 * <p>
 * 被记忆的（subject.isRemembered()结果为true）。
 * @RequiresGuest 验证是否是一个guest的请求，与@RequiresUser完全相反。
 * <p>
 * 就是RequiresUser  == !RequiresGuest。
 * <p>
 * 此时subject.getPrincipal() 结果为null.
 * ————————————————
 * 版权声明：本文为CSDN博主「superboy@.」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/weixin_46504244/article/details/120382383
 * <p>
 * <p>
 * ————————————————
 * 版权声明：本文为CSDN博主「Stronger丶We」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/w_stronger/article/details/73109248
 */

package com.github.axinger.aop;

/// jdk 方法

import com.github.axinger.aop.dao.UserDao;
import com.github.axinger.aop.dao.UserDaoImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/***
 *  java.lang.reflect.Proxy; 这个包的
 * 参数1,类加载器
 * 2,增强方法所在的类,这个类实现的接口,支持多个接口
 * 3,实现这个接口,创建代理对象,写增强部分
 */

public class JDKProxy {

    public static void main(String[] args) {

        Class[] interfaces = {UserDao.class};
//        Proxy.newProxyInstance(JDKProxy.class.getClassLoader(), interfaces, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                return null;
//            }
//        });

        UserDaoImpl userDao = new UserDaoImpl();

        UserDao dao = (UserDao) Proxy.newProxyInstance(JDKProxy.class.getClassLoader(), interfaces, new UserDaoProxy(userDao));

        Integer result = dao.add(1, 2);

        System.out.println("add = " + result);
    }
}

// 创建代理对象
class UserDaoProxy implements InvocationHandler {

    // 1. 把创建的是谁的代理对象,把谁传递过来
    // 常用,有参构造

    private final Object obj;

    public UserDaoProxy(Object obj) {
        this.obj = obj;

    }


    // 增强逻辑
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /// 方法前
        System.out.println("方法前........." + obj + " method = " + method.getName() + " 参数 = " + Arrays.toString(args));

        Object invoke = method.invoke(obj, args);

        if (method.getName().equals("add")) {
            System.out.println("判断方法...........add");
        }
        /// 方法后
        System.out.println("方法后........" + obj);

        return invoke;
    }
}

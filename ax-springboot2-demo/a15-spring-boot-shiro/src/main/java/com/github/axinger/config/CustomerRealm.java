package com.github.axinger.config;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 4 自定义realm
 */
public class CustomerRealm extends AuthorizingRealm {

//
//    // 设置realm的名称
//    @Override
//    public void setName(String name) {
//        super.setName("customRealm");
//    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
//    @Override
//    public boolean supports(AuthenticationToken token) {
//        return token instanceof JwtToken;
//    }


    /**
     * 用户认证, 存放用户的账号密码
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("进来了用户认证");

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 获取login请求的 username
        String username = token.getUsername();
        // 获取login请求的 Password 基本不需要获取
        String password = new String(token.getPassword());


        /// 查询数据库
        if (username.equals("tom")) {
            throw new UnknownAccountException("未查询到用户");
        }

        // 模拟从数据库查询到密码, 这个是校验  subject.login 的密码是否一致
        String password2 = "111112";
        // 如果查询到返回认证信息AuthenticationInfo
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, this.getName());
        return info;
    }


    /**
     * 授权,用户认证后通过后,访问需要授权的请求 进行验证
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        Object o = principalCollection.getPrimaryPrincipal();
        System.out.println("进来了授权 = " + o);

        // 从数据库中查询角色
        Set<String> roles = new HashSet<>(16);
        roles.add("admin");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 追加
//        info.addRoles(roles);

        // 设置
        info.setRoles(roles);


        // 从数据库中查询角色权限
        if (o instanceof String && o.equals("tom")) {
            Set<String> set = new HashSet<>(16);
            set.add("add");
            info.setStringPermissions(set);
        }

        return info;
    }


    /**
     * 自定义密码匹配器
     * @param credentialsMatcher
     */
//    @Override
//    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
//        //自定义密码匹配器
//        CurrentCredentialsMatcher currentCredentialsMatcher = new CurrentCredentialsMatcher();
//
//        super.setCredentialsMatcher(currentCredentialsMatcher);
//    }


}

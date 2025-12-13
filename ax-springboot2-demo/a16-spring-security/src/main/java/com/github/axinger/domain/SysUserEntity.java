package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 系统用户表
 *
 * @TableName sys_user
 */
@TableName(value = "sys_user")
@Data
public class SysUserEntity implements Serializable, UserDetails {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     *
     */
    @TableField(value = "username")
    private String username;
    /**
     *
     */
    @TableField(value = "password")
    private String password;
    /**
     *
     */
    @TableField(value = "email")
    private String email;
    /**
     *
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 0-禁用 1-启用
     */
    @TableField(value = "status")
    private Integer status;
    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     *
     */
    @TableField(value = "update_time")
    private Date updateTime;
    /**
     *
     */
    @TableField(value = "version")
    private Long version;
    /**
     * 0-未删除 1-已删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;
    @TableField(exist = false)
    private List<SysRoleEntity> roles; // 用户拥有的角色集合

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        /// 添加角色
        List<SimpleGrantedAuthority> authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
        /// 添加权限
        List<SimpleGrantedAuthority> authorities2 = roles.stream().flatMap(val -> val.getPermissions().stream())
                .map(val -> new SimpleGrantedAuthority(val.getName())).toList();

        ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
        list.addAll(authorities);
        list.addAll(authorities2);
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /*
     * 表示判断账户是否过期
     * */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /*
     * 表示判断账户是否被锁定
     * */
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    /*
     * 表示凭证{密码}是否过期
     * */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /*
     * 是否可用
     * */
    @Override
    public boolean isEnabled() {
        return status == 1;
    }
}
